package com.cdy.sample_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SampleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleServerApplication.class, args);
    }
}
