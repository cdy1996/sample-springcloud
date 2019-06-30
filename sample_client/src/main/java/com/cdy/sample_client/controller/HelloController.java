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

//    @NacosInjected
//    private NamingService namingService;
    
    @Value("${client2.port:12001}")
    private String port;

//    @NacosValue(value = "${client2.port}", autoRefreshed = true)
//    private boolean port2;
    
    @Value("${client2.service.id:client2}")
    private String client;


//    @RequestMapping(value = "/get", method = GET)
//    @ResponseBody
//    @SentinelResource(blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class)
//    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
//        return namingService.getAllInstances(serviceName);
//    }
    
    @RequestMapping("/hello")
    public String hello() {
        String body = restTemplate.getForEntity("http://" + client + "/world", String.class).getBody();
        return body;
    }
    
    @RequestMapping("/world")
//    @SentinelResource("word")
    public String world(Long time) {
        System.out.println(time);
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return client + ":" + port;
    }
    
    
}
