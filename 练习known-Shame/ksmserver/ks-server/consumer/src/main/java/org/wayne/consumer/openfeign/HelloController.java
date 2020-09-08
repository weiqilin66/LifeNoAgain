package org.wayne.consumer.openfeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wayne.commons.entity.User;

import javax.annotation.Resource;

/**
 * @Description: openfeign调用服务
 * @author: LinWeiQi
 */
@RestController
public class HelloController {

    @Resource
    HelloService helloService;

    @GetMapping("/feignHello")
    public String hello(){
        return helloService.hello();
    }
    @GetMapping("/feignHello2")
    public String hello2(){
        return helloService.hello2("wayne");
    }

    @GetMapping("/feignHello3")
    public User user1(){
        return helloService.user1("wayne","123");
    }
    @GetMapping("/feignHello4")
    public User user2(){
        User user = new User();
        user.setId(111);
        return helloService.user2(user);

    }
    @GetMapping("/feignHello5")
    public String user3(){
        return helloService.del(3);

    }
}
