package com.cdy.sample_zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class SampleGatewayApplication {
    

    public static void main(String[] args) {
        SpringApplication.run(SampleGatewayApplication.class, args);
    }
}

