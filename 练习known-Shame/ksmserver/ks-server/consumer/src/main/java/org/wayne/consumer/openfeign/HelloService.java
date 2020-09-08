package org.wayne.consumer.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.wayne.commons.entity.User;
import org.wayne.consumer.openfeignandhystrix.HelloFallBackFactory;

/**
 * @Description: openFeign
 * 可配置服务降级 需在配置文件中开启
 * 2种方式
 * @author: LinWeiQi
 */
//@FeignClient(value = "provide",fallback = FeignFallBack.class)
@FeignClient(value = "provide",fallbackFactory = HelloFallBackFactory.class)
public interface HelloService {

    //restTemplate.getForObject(uri, String.class) 底层
    @GetMapping("/hello")
    String hello();

    //传参
    // GET 必须加@requestParam("xx") 指定变量名
    // POST 必须@requestBody
    // DELETE 必须加@PathVariable
    @GetMapping("/hello2")
    String hello2(@RequestParam("name") String name);

    @PostMapping("/user2")//json形式
    User user2(@RequestBody User user);

    @PostMapping("/user1")//key value形式
    User user1(@RequestParam("username") String username,@RequestParam("password") String password);

    @DeleteMapping("/user3/{id}")
    String del(@PathVariable Integer id);
}
