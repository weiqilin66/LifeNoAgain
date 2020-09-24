package com.lwq.hr.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lwq.hr.jedis.CallWithJedis;
import com.lwq.hr.jedis.JedisConfig;
import com.lwq.hr.utils.RespBean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@Service
public class JedisService {
    JedisConfig jedisConfig = new JedisConfig();

    List res;
    public List getCache(String cacheName){

        jedisConfig.excute(jedis -> {
            final String js = jedis.get(cacheName);
            //json字符串转集合
            res= new Gson().fromJson(js, new TypeToken<List<Object>>(){}.getType());
        });
        return res;
    }
}
