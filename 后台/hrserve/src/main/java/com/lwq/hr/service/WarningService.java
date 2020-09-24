package com.lwq.hr.service;

import com.google.gson.internal.LinkedTreeMap;
import com.lwq.hr.entity.GoodStock;
import com.lwq.hr.entity.GoodStockVo;
import com.lwq.hr.entity.Goods;
import com.lwq.hr.entity.Shop;
import com.lwq.hr.mapper.GoodStockMapper;
import com.lwq.hr.mapper.GoodsMapper;
import com.lwq.hr.mapper.ShopMapper;
import com.lwq.hr.mapper.WarnKeywordNosetMapper;
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
    JedisService jedisService;
    @Resource
    WarnKeywordNosetMapper warnKeywordNosetMapper;
    /**
     * 市场价低于自身预警 底价策略
     * 有库存商品未设置关键词预警
     */
    public void othersLower(){
        List warnShopList = jedisService.getCache("shopWarning");
        List<List<Goods>> resList = Collections.synchronizedList(new ArrayList<>());
        List<HashMap> noKeyWordList = Collections.synchronizedList(new ArrayList<>());
        //关注的店铺
        List<String> shopList = new ArrayList<>();
        for (Object o : warnShopList) {
            LinkedTreeMap map = (LinkedTreeMap) o;
            String shopNameWangWang = (String) map.get("name");
            //String shopName = (String) map.get("comment");
            shopList.add(shopNameWangWang);
        }
        //遍历我的商品对比关注店铺的商品价格
        final List<HashMap> mapList = goodStockMapper.queryAllWithKeyWord();
        final String date = DateFormatUtil.formatStr(new Date(), "yyyyMMdd");
        mapList.parallelStream().forEach(map->{
            String kw = map.get("label").toString()+map.get("name").toString();//对应goods表kw字段 kw类似布隆过滤器
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

            List<Goods> res = goodsMapper.selWarningLower(date,shopList,kw,price,base,
                    include1,include2,include3,enclude1,enclude2,enclude3);
           resList.add(res);
        });
        //数据入库
        warnKeywordNosetMapper.delAll();
        warnKeywordNosetMapper.batchInsert(noKeyWordList);

        }


}
