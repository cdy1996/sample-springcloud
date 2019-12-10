package com.cdy.sample_client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * todo
 * Created by 陈东一
 * 2018/6/18 15:49
 */
@RestController
@RefreshScope
public class HelloController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${client2.port:12001}")
    private String port;
    
    @Value("${client2.service.id:client2}")
    private String client;
    
    @RequestMapping("/hello")
    public String hello() {
        String body = restTemplate.getForEntity("http://" + client + "/world", String.class).getBody();
        return body;
    }
    
    @RequestMapping("/world")
    public String world() {
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return client + ":" + port ;
    }
    
    
}
