package com.lwq.hr.service;

import com.google.gson.internal.LinkedTreeMap;
import com.lwq.hr.entity.GoodStock;
import com.lwq.hr.entity.GoodStockVo;
import com.lwq.hr.entity.Shop;
import com.lwq.hr.mapper.GoodStockMapper;
import com.lwq.hr.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * 市场价低于自身预警
     * 底价策略
     */
    public List<GoodStock> othersLower(){
        List<GoodStock> list = new ArrayList<>();
        List warnShopList = jedisService.getCache("shopWarning");
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
        mapList.parallelStream().forEach(map->{
            String kw = map.get("label").toString()+map.get("name").toString();//对应goods表kw字段
            float price =map.get("price")!=null? (float) map.get("price"):0;
            System.out.println(map.get("base"));
            System.out.println(price);
        });
        return list;
    }
}
