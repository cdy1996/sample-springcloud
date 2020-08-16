package com.cdy.sample_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@EnableEurekaServer
public class SampleServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SampleServerApplication.class, args);
        ConfigurableEnvironment environment = run.getEnvironment();
    
    
        BindResult<User> user = Binder.get(environment).bind("user", User.class);
        System.out.println(user.get());
    
    }
}
