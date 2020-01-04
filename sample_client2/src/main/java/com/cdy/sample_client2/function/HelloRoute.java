package com.cdy.sample_client2.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * todo
 * Created by 陈东一
 * 2020/1/4 0004 11:48
 */
@Configuration(proxyBeanMethods = false)
public class HelloRoute {
    
    @Bean
    public RouterFunction<? extends ServerResponse> hello() {
        return route(GET("/hello/{id}"),
                request -> {
                    String id = request.pathVariable("id");
                    return ServerResponse.ok().body(id, String.class);
                });
        
    }
}
