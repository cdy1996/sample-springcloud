package com.cdy.sample_client.feign;

import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface HelloOpenfeign {
    @RequestLine("GET /feign?feign={feign}")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    String feign(@Param("feign") String feign);

    @RequestLine("POST /feignPost")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    String feignPost(@Param("feign") String feign);

    @RequestLine("POST /feignJson")
    @Headers("Content-Type: application/json")
    String feignForm(@Param("feign") String feign);

    public static void main(String[] args) {
        HelloOpenfeign client = Feign.builder().target(HelloOpenfeign.class, "http://localhost:12001");
        String result = client.feign("YourBatman");
        System.out.println(result);
    }
}
