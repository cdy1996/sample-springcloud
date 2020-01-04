package com.cdy.sample_client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
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

    @Autowired
    private CircuitBreakerFactory cbFactory;
    @Autowired
    @LoadBalanced
    private RestTemplate rest;
//    @Autowired
//    private WebClient webClient;
    @Autowired
    @LoadBalanced
    private WebClient.Builder webClient;
    @Autowired
    private ReactiveCircuitBreakerFactory rcbFactory;

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    Environment environment;
    
    @RequestMapping("/circuitbreaker")
    public String slow() {
        return cbFactory.create("slow").run(() -> rest.getForObject("http://" + client + "/world", String.class), throwable -> "fallback");
    }

    @RequestMapping("/circuitbreaker2")
    public String slow1() {
        return webClient.build().get().uri("http://" + client + "/world").retrieve().bodyToMono(String.class)
                .doOnNext(e -> System.out.println(e))
                .block(Duration.ofMillis(1000L));
    }

    @RequestMapping("/circuitbreaker3")
    public String slow2() {
        return webClient.build().get().uri("http://" + client + "/world").retrieve().bodyToMono(String.class).transform(
                it -> rcbFactory.create("slow").run(it, throwable -> {
                    return Mono.just("fallback");
                })).block();
    }

    @RequestMapping("/world")
    public String world(HttpServletResponse response) {
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return client + ":" + port;
    }


}
