package com.lwq.hr.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description:
 * @author: LinWeiQi
 */
public class JedisConfig {
    private JedisPool jedisPool;

    public JedisConfig(){
        jedisPool=new JedisPool("127.0.0.1");
    }
    //逼王核心
    public void excute(CallWithJedis callWithJedis){//传入接口则要实现接口
        try(Jedis jedis = jedisPool.getResource()){//try with source被隐藏了
            jedis.auth("123");
            callWithJedis.call(jedis);//call为传入时实现的接口
        }

    }
}
