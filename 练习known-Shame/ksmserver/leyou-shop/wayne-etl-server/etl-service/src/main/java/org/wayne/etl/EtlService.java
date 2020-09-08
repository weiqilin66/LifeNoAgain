package org.wayne.etl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description:
 * @author: LinWeiQi
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("org.wayne.etl.mapper")
public class EtlService {
    public static void main(String[] args) {
        SpringApplication.run(EtlService.class,args);
    }
}
