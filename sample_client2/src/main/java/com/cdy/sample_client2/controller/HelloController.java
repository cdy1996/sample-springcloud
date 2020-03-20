package com.cdy.sample_client2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    
    @Value("${client.port:12000}")
    private String port;
    
    @Value("${client.service.id:client}")
    private String client;
    
    @RequestMapping("/hello")
    public String hello() {
        String body = restTemplate.getForEntity("http://" + client + "/world", String.class).getBody();
        return body;
    }
    
    @RequestMapping("/world")
    public String world() {
        return client + ":" + port;
    }

    @GetMapping("/feign")
    public String feign(String param) {
        return param;
    }

    @PostMapping("/feignPost")
    public String feignPost(String param) {
        return param;
    }

    @RequestMapping("/feignJson")
    public String feignJson(@RequestBody String param) {
        return param;
    }
}
