package com.lwq.hr.jedis;

import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @author: LinWeiQi
 */
public class ExampleJedis {
        public static void main(String[] args) {
//        Jedis jedis = new Jedis("192.168.45.45");
//        jedis.auth("123");
//        String ping = jedis.ping();
//        System.out.println(ping);

            final JedisConfig jedis = new JedisConfig();
            jedis.excute(new CallWithJedis() {//这里实现了接口的call,而excute方法中调用了call()
                @Override
                public void call(Jedis jedis) {
//                    jedis.auth("123");
                    System.out.println(jedis.ping());
                }
            });
            //lambda
            jedis.excute(jedis1 -> {
//                jedis1.auth("123");
                System.out.println(jedis1.ping());
            });
        }

}

