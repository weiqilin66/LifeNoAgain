package com.lwq.hr.service;

import com.google.gson.internal.LinkedTreeMap;
import com.lwq.hr.entity.*;
import com.lwq.hr.mapper.*;
import com.lwq.hr.utils.DateFormatUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Description:
 * @author: LinWeiQi
 */
@Service
public class WarningService {

    @Resource
    GoodsMapper goodsMapper;
    @Resource
    GoodStockMapper goodStockMapper;
    @Resource
    WarnKeywordNosetMapper warnKeywordNosetMapper;
    @Resource
    WarnLowerPriceMapper warnLowerPriceMapper;
    @Resource
    ShopMapper shopMapper;
    @Resource
    WarnHunterMapper warnHunterMapper;
    @Resource
    GoodSalesMapper goodSalesMapper;
    @Resource
    WarnSalesMapper warnSalesMapper;
    /**
     * 市场价低于自身预警 底价策略
     * 有库存商品未设置关键词预警
     * 比猎人价低大于10预警
     */
    public void warning001() throws Exception {
        List<List<Goods>> lowerList = Collections.synchronizedList(new ArrayList<>());
        List<List<Goods>> hunterList = Collections.synchronizedList(new ArrayList<>());
        List<HashMap> noKeyWordList = Collections.synchronizedList(new ArrayList<>());
        List<WarnSales> salesList = Collections.synchronizedList(new ArrayList<>());
        //关注的店铺
        List<String> othersLowerShops = shopMapper.selByType("othersLower");
        List<String> hunterShops = shopMapper.selByType("hunter");
        if (othersLowerShops.isEmpty()||hunterShops.isEmpty()){
            throw new Exception("预警店铺设置出错");
        }
        final List<HashMap> mapList = goodStockMapper.queryAllWithKeyWord();
        final String date = goodsMapper.selMaxDate();
        final String nowDate = DateFormatUtil.formatStr(new Date(), "yyyyMMdd");
        mapList.parallelStream().forEach(map->{
            int gid = Integer.parseInt(map.get("gid").toString());
            float price =map.get("price")!=null? (float) map.get("price"):0;
            final Object baseO = map.get("base");
            String base ="";
            if (baseO!=null) {
                base = baseO.toString();
            }else {
                noKeyWordList.add(map);
            }
            //销量走势预警
            //销量1天剧增减 超0.1
            final float resRate = goodSalesMapper.selByGid2(gid, nowDate);
            if (resRate>=1.1) {
                final WarnSales e = new WarnSales();
                e.setGid(gid);
                e.setType("1");//1天剧增
                salesList.add(e);
            }else if(resRate<=0.9){
                final WarnSales e = new WarnSales();
                e.setGid(gid);
                e.setType("2");//1天骤降
                salesList.add(e);
            }
            //7天内5天增加 减少
            int j = 7;
            int n = 0;
            int m = 0;
            for (int i = 0; i < j; i++) {
                final String lastDay = DateFormatUtil.formatStr(
                        DateFormatUtil.getOtherDate(new Date(), -i), "yyyyMMdd");
                float rate = goodSalesMapper.selByGid2(gid, lastDay);
                if (rate>1) {
                    m++;
                }else if(rate<1){
                    n++;
                }
            }
            if (m>=5) {
                final WarnSales e = new WarnSales();
                e.setGid(gid);
                e.setType("3");//一周5天涨
                salesList.add(e);
            }else if(n>=5){
                final WarnSales e = new WarnSales();
                e.setGid(gid);
                e.setType("4");//一周5天降
                salesList.add(e);
            }

            String include1 = map.get("include1")!=null?map.get("include1").toString():"";
            String include2 = map.get("include2")!=null?map.get("include2").toString():"";
            String include3 = map.get("include3")!=null?map.get("include3").toString():"";
            String enclude1 = map.get("enclude1")!=null?map.get("enclude1").toString():"";
            String enclude2 = map.get("enclude2")!=null?map.get("enclude2").toString():"";
            String enclude3 = map.get("enclude3")!=null?map.get("enclude3").toString():"";
            //我的商品价格>关注店铺的商品价格 底价预警
            List<Goods> res = goodsMapper.selWarningLower(date,othersLowerShops,gid,price,base,
                    include1,include2,include3,enclude1,enclude2,enclude3);
            if (res.size()>0) {
                lowerList.add(res);
            }
            //比猎人价低大于10预警
            List<Goods> hunterRes = goodsMapper.selHunter(date,hunterShops,gid,price,base,
                    include1,include2,include3,enclude1,enclude2,enclude3);
            if (hunterRes.size()>0) {
                hunterList.add(hunterRes);
            }
        });
        //数据入库
        warnKeywordNosetMapper.delAll();
        if (noKeyWordList.size()>0) {
            warnKeywordNosetMapper.batchInsert(noKeyWordList);
        }
        warnLowerPriceMapper.delAll();
        if (lowerList.size()>0) {
            warnLowerPriceMapper.batchInsert(lowerList);
        }
        warnHunterMapper.delAll();
        if (hunterList.size()>0) {
            warnHunterMapper.batchInsert(hunterList);
        }
        warnSalesMapper.delAll();
        if (salesList.size()>0) {
            warnSalesMapper.batchInsert(salesList);
        }
    }

    /**
     * 智能建议底价
     */
    public void computeLowerPrice() throws Exception {
        //先刷新入库预警
        warning001();
        final List<HashMap> mapList = goodStockMapper.queryAllWithKeyWord();
        mapList.parallelStream().forEach(map->{
            int gid = Integer.parseInt(map.get("gid").toString());
            float price =map.get("price")!=null? (float) map.get("price"):0;
            //低价建议
            WarnLowerPrice entity = warnLowerPriceMapper.selectLowestByGid(gid);
            //更新最低建议价格
            if (entity!=null) {
                final float lowerPrice = entity.getPrice();
                final String shop = entity.getShop();
                String comment = ""+shop +" 售价:"+lowerPrice+" 建议价:"+(lowerPrice -1)+" 降价:"+(price-lowerPrice);//比最低价低1元
                if ("宁波老猎人电玩店".equals(shop)) {
                    comment = ""+shop +" 售价:"+lowerPrice+" 建议价:"+(lowerPrice -5)+" 降价:"+(price-lowerPrice);;//比最低价低5元
                }
                goodStockMapper.updateCommentById(gid,comment);
            }else {
                goodStockMapper.updateCommentById(gid,"");
            }
        });
    }

    public List<HashMap> getHunter(){
        return warnHunterMapper.queryAll();
    }

    public List<HashMap> getOthersLower() {
        return warnLowerPriceMapper.queryAll();
    }
    public List<HashMap> sales() {
        return warnSalesMapper.queryAll();
    }

}
