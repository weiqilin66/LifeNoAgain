package org.wayne.leyou.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Description: zuul进行权限的管理,网关路由,负载均衡
 * @author: LinWeiQi
 */
@SpringBootApplication
@EnableZuulProxy
public class LeyouZuulApp {
    public static void main(String[] args) {
        SpringApplication.run(LeyouZuulApp.class,args);
    }
}
