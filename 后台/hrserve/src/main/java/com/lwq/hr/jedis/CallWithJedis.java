package com.lwq.hr.jedis;

import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @author: LinWeiQi
 */
public interface CallWithJedis {
    void call(Jedis jedis);
}

