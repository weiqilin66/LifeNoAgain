package org.wayne.consumer.openfeignandhystrix;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wayne.commons.entity.User;
import org.wayne.consumer.openfeign.HelloService;

/**
 * @Description: openfeign的服务降级 +配置文件中配置开启
 * @author: LinWeiQi
 */
@Component
@RequestMapping("/openFeignAndHystrix")//配置路径防止和feign接口中的地址重复
public class FeignFallBack implements HelloService {
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
}
