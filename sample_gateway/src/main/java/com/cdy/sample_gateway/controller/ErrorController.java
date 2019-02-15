package com.cdy.sample_gateway.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.cdy.sample_gateway.filter.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 * Created by 陈东一
 * 2019/2/11 0011 21:58
 */
@RestController
public class ErrorController {
    
    @NacosValue(value = "${test}", autoRefreshed = true)
    private String test;
    
    @GetMapping("/hello")
    public String hello() {
        return test;
    }
    
    @GetMapping("/fallback")
    public String fallback() {
        Response response = new Response();
        response.setCode("100");
        response.setMessage("服务暂时不可用");
        return response.getMessage();
    }
}
