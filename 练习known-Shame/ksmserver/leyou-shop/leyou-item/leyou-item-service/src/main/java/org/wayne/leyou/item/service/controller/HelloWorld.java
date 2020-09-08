package org.wayne.leyou.item.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/hello")
public class HelloWorld {

    @GetMapping("/world")
    public String hello(){

        return "hello world";
    }
}
