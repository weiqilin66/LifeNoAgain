package org.wayne.leyou.item.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description:
 * @author: LinWeiQi
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LeyouItemService {
    public static void main(String[] args) {

        SpringApplication.run(LeyouItemService.class,args);
    }
}
