package com.cdy.sample_zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringCloudApplication
@EnableZuulProxy
public class SampleZuulApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SampleZuulApplication.class, args);
    }
}

