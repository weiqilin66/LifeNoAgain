package org.wayne.consumer.openfeignandhystrix;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.wayne.commons.entity.User;
import org.wayne.consumer.openfeign.HelloService;

/**
 * @Description: 工厂方式返回 FallbackFactory<熔断的接口> +配置文件中配置开启
 * implements FallbackFactory<HelloService>
 * # 开启feign服务降级
 * feign.hystrix.enabled=true
 * @author: LinWeiQi
 */
@Component
public class HelloFallBackFactory implements FallbackFactory<HelloService> {
    @Override
    public HelloService create(Throwable throwable) {
        return new HelloService() {
            @Override
            public String hello() {
                return "error";
            }

            @Override
            public String hello2(String name) {
                return null;
            }

            @Override
            public User user2(User user) {
                return null;
            }

            @Override
            public User user1(String username, String password) {
                return null;
            }

            @Override
            public String del(Integer id) {
                return null;
            }
        };
    }
}
