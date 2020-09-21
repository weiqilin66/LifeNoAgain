package com.lwq.hr.controller.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lwq.hr.entity.GoodMain;
import com.lwq.hr.init.JedisInit;
import com.lwq.hr.jedis.CallWithJedis;
import com.lwq.hr.jedis.JedisConfig;
import com.lwq.hr.utils.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/noRight/cache")
public class JedisController {

    JedisConfig jedisConfig = new JedisConfig();
    List res;
    @Resource
    JedisInit jedisInit;
    @GetMapping("/refresh")
    public RespBean refreshCache(){
        try {
            jedisConfig.excute(new CallWithJedis() {
                @Override
                public void call(Jedis jedis) {
                    //jedis.flushAll(); //删除所有库
                    jedis.flushDB();//删除单个库
                }
            });
            /**
             * 反射得到类中的方法并invoke执行
             */
            //final Method refresh = jedisInit.getClass().getDeclaredMethod("refresh");
            //refresh.invoke(jedisInit);

            //获取类所有声明的方法
            Method[] declaredMethods = JedisInit.class.getDeclaredMethods();
            for (Method m : declaredMethods) {
                m.invoke(jedisInit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error();
        }
        return RespBean.ok();
    }

    /**
     * 根据缓存名 返回缓存集合
     * @param cacheName
     * @return
     */
    @GetMapping("/{cacheName}")
    public RespBean getCache(@PathVariable String cacheName){
        jedisConfig.excute(new CallWithJedis() {
            @Override
            public void call(Jedis jedis) {
                final String js = jedis.get(cacheName);
                //json字符串转集合
                res= new Gson().fromJson(js, new TypeToken<List<Object>>(){}.getType());
            }
        });
        if (res!=null) {

            return RespBean.build().setData(res);
        }
        return RespBean.error("key: "+cacheName +" 的缓存不存在");
    }
}
