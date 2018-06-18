package com.cdy.sample_zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class SampleZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleZuulApplication.class, args);
    }
}
