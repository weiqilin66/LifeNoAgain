package com.lwq.hr.init;

import com.google.gson.Gson;
import com.lwq.hr.entity.GoodLabel;
import com.lwq.hr.entity.GoodMain;
import com.lwq.hr.entity.ShopWarning;
import com.lwq.hr.jedis.CallWithJedis;
import com.lwq.hr.jedis.JedisConfig;
import com.lwq.hr.mapper.GoodLabelMapper;
import com.lwq.hr.mapper.GoodMainMapper;
import com.lwq.hr.mapper.ShopWarningMapper;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 系统启动初始化参数
 * @author: LinWeiQi
 */
@Service
public class JedisInit {

    final JedisConfig jedis = new JedisConfig();
    @Resource
    GoodMainMapper goodMainMapper;
    @Resource
    GoodLabelMapper goodLabelMapper;
    @Resource
    ShopWarningMapper shopWarningMapper;
    @PostConstruct
    public void initGoodMain(){
        jedis.excute(new CallWithJedis() {
            @Override
            public void call(Jedis jedis) {
                final String goodMain = jedis.get("goodMain");
                if (goodMain==null) {
                    jedis.set("goodMain",new Gson().toJson(goodMainMapper.queryAll()));
                }
            }
        });
    }
    @PostConstruct
    public void initLabel(){
        jedis.excute(new CallWithJedis() {
            @Override
            public void call(Jedis jedis) {
                final String goodLabel = jedis.get("goodLabel");
                if (goodLabel==null) {
                    jedis.set("goodLabel",new Gson().toJson(goodLabelMapper.queryAll()));
                }
            }
        });
    }
    /**
     * 预警店铺
     */
    @PostConstruct
    public void initWarningShop(){
        jedis.excute(jedis -> {
            final String warningShop = jedis.get("shopWarning");
            if (warningShop==null) {
                jedis.set("shopWarning",new Gson().toJson(shopWarningMapper.queryAll()));
            }
        });
    }
}
