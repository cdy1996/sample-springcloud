package com.cdy.sample_client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *  1.当参数比较复杂时，feign即使声明为get请求也会强行使用post请求
 *
 *  2.不支持@GetMapping类似注解声明请求，需使用@RequestMapping(value = "url",method = RequestMethod.GET)
 *
 *  3.使用@RequestParam注解时必须要在后面加上参数名
 *
 * https://faceghost.com/article/217761
 */
@FeignClient("client2") // service-id
public interface HelloOpenfeignSpringcloud {
    
    @RequestMapping(name=" /feign",method = RequestMethod.GET, produces="application/x-www-form-urlencoded")
    String feign(@RequestParam("feign") String feign);
    
    @RequestMapping(name="/feignPost",method = RequestMethod.POST, produces="application/x-www-form-urlencoded")
    String feignPost(@RequestParam("feign") String feign);
    
    @RequestMapping(name = "/feignJson",method = RequestMethod.POST , produces="application/json")
    String feignJson(@RequestBody String feign);

}
