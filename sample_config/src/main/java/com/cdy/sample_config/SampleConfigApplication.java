package com.cdy.sample_config;

import com.cdy.sample_config.controller.ZKUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class SampleConfigApplication {

    @Bean(destroyMethod = "releaseConnection")
    public ZKUtil zkUtil(){
        ZKUtil zkWatchAPI = new ZKUtil();
        zkWatchAPI.connectionZookeeper("127.0.0.1:2181");
        return zkWatchAPI;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(SampleConfigApplication.class, args);
    }
}
