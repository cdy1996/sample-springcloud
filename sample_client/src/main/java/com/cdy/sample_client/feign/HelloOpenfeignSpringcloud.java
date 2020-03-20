package com.cdy.sample_client.feign;

import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient("client2") // service-id
public interface HelloOpenfeignSpringcloud {

    @GetMapping(name=" /feign", produces="application/x-www-form-urlencoded")
    String feign(@RequestParam("feign") String feign);

    @PostMapping(name="/feignPost", produces="application/x-www-form-urlencoded")
    String feignPost(@RequestParam("feign") String feign);

    @PostMapping(name = "/feignJson" , produces="application/json")
    String feignJson(@RequestParam("feign") String feign);


}
