package org.wayne.consumer.resttemplate;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: RestTemplate+ribbon 消费服务
 * 启动类注入一个restTemplate开启负载均衡
 * 分辨开启负载均衡后的的区别
 * <p>
 * RestTemplate 是Spring 3.0的一个 http请求工具 --->类比前端的axios
 * 提供了常见的GET POST DELETE PUT等请求方式及通用的exchange,execute方法
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/restTemplate")
public class RestTemplateGetController {

    @Resource
    RestTemplate restTemplate;

    /**
     *  getForObject() 返回一个对象,既服务端返回的具体值
     *  getForEntity() 返回一个ResponseEntity,不仅包含具体值,还包含http请求头信息
     */
    @RequestMapping("/hello")
    public String hello() throws UnsupportedEncodingException {
        //如果没有开启负载均衡 地址不能写 http://provide/hello 必须是完整的地址 参考hello1()
        //开启负载均衡会自动被拦截,(开启负载均衡本质是添加了拦截器) discoveryClient.getInstances(provide) 从注册表中获得provide地址列表
        //根据定义的负载均衡策略取访问服务,封装类似如下hello1()方法

        //3种重载方法
        String s = restTemplate.getForObject("http://provide/hello", String.class);//不传参 Object... uriVariables

        String url = "http://provide/hello2?name=" + URLEncoder.encode("李四", "UTF-8");
        URI uri = URI.create(url);
        s = restTemplate.getForObject(uri, String.class);//URI 传中文

        HashMap<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("name","wayne");
        uriVariables.put("age",18);
        s = restTemplate.getForObject("http://provide/hello2?name={1}", String.class,"wayne");//占位传参
        s=restTemplate.getForObject("http://provide/hello3?name={name}&age={age}",String.class, uriVariables);//map传参
        return s;
    }


    /**
     * hello1()不可用
     * 开启负载均衡后,请求被拦截并进入
     * discoveryClient.getInstances(provide) 从注册表中获得 host/port信息 在发送请求
     * 如果传入discoveryClient.getInstances(地址)获取不到报错
     */
    @Resource
    DiscoveryClient discoveryClient;

    @RequestMapping("/hello1")
    public String hello1() {
        //从注册中心获取到的注册表信息
        List<ServiceInstance> list = discoveryClient.getInstances("provide");
        ServiceInstance serviceInstance = list.get(0);
        String hostName = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        StringBuilder sb = new StringBuilder();
        sb.append("http://")
                .append(hostName)
                .append(":")
                .append(port)
                .append("/hello");
        System.out.println(sb);
        String s = restTemplate.getForObject(sb.toString(), String.class);

        return s;
    }
}
