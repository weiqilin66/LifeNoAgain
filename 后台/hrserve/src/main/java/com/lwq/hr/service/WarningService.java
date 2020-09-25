package com.lwq.hr.service;

import com.google.gson.internal.LinkedTreeMap;
import com.lwq.hr.entity.GoodStock;
import com.lwq.hr.entity.GoodStockVo;
import com.lwq.hr.entity.Goods;
import com.lwq.hr.entity.Shop;
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
    /**
     * 市场价低于自身预警 底价策略
     * 有库存商品未设置关键词预警
     * 比猎人价低大于10预警
     */
    public void warning001(){
        List<List<Goods>> resList = Collections.synchronizedList(new ArrayList<>());
        List<HashMap> noKeyWordList = Collections.synchronizedList(new ArrayList<>());
        //关注的店铺
        List<String> othersLowerShops = shopMapper.selByType("othersLower");
        List<String> hunterShops = shopMapper.selByType("hunter");

        final List<HashMap> mapList = goodStockMapper.queryAllWithKeyWord();
        final String date = goodsMapper.selMaxDate();
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
            String include1 = map.get("include1")!=null?map.get("include1").toString():"";
            String include2 = map.get("include2")!=null?map.get("include2").toString():"";
            String include3 = map.get("include2")!=null?map.get("include2").toString():"";
            String enclude1 = map.get("enclude1")!=null?map.get("enclude1").toString():"";
            String enclude2 = map.get("enclude2")!=null?map.get("enclude2").toString():"";
            String enclude3 = map.get("enclude3")!=null?map.get("enclude3").toString():"";
            //我的商品价格>关注店铺的商品价格
            List<Goods> res = goodsMapper.selWarningLower(date,othersLowerShops,gid,price,base,
                    include1,include2,include3,enclude1,enclude2,enclude3);
            if (res.size()>0) {
                resList.add(res);
            }
            //比猎人价低大于10预警
            List<Goods> hunterRes = goodsMapper.selHunter(date,hunterShops,gid,price,base,
                    include1,include2,include3,enclude1,enclude2,enclude3);
        });
        //数据入库
        warnKeywordNosetMapper.delAll();
        if (noKeyWordList.size()>0) {
            warnKeywordNosetMapper.batchInsert(noKeyWordList);
        }
        warnLowerPriceMapper.delAll();
        if (resList.size()>0) {
            warnLowerPriceMapper.batchInsert(resList);
        }
        }

}
