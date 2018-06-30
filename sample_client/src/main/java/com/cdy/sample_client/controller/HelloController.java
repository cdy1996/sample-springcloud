package com.cdy.sample_client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * todo
 * Created by 陈东一
 * 2018/6/18 15:49
 */
@RestController
@RequestMapping("/zookeeper")
@RefreshScope
public class HelloController {
    
    @Autowired
    private Environment environment;
    
    @Value("${name}")
    private String client2;
    
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${spring.application.name}")
    private String instanceName;
    
    @Autowired
    private  DiscoveryClient discoveryClient;
    
    
    @GetMapping("/env")
    public String test() {
        return "client2 serviceId：" + environment.getProperty("name");
    }
    
//
//    public HelloController(DiscoveryClient discoveryClient) {
//        this.discoveryClient = discoveryClient;
//    }


//    @Value("${client2.port}")
//    private String port;
//
//    @Value("${client2.service.id}")
//    private String client;
    
    
    @GetMapping("/services")
    public List<String> serviceUrl() {
        List<ServiceInstance> list;
        List<String> services = new ArrayList<>();;
        List<String> services1 = discoveryClient.getServices();
        System.out.println(services1.toString());
        for (String s : services1) {
            list = discoveryClient.getInstances(s);
            for (ServiceInstance serviceInstance : list) {
                System.out.println(serviceInstance.getUri().toString());
                services.add(serviceInstance.getUri().toString());
            }
        }
        return services;
    }
    
    
//    @RequestMapping("/hello")
//    public String hello() {
//        String body = restTemplate.getForEntity("http://" + client + "/world", String.class).getBody();
//        return body;
//    }
//
    
    @RequestMapping("/from2")
    public String world() {
        return restTemplate.getForEntity("http://"+client2+"/zookeeper/env", String.class).getBody();
    }
    
    
}
