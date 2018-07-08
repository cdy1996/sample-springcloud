package com.cdy.sample_zuul.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * todo
 * Created by 陈东一
 * 2018/7/8 9:22
 */

@Configuration
public class CustomZuulConfig {
    
    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Bean
    public CustomRouteLocator routeLocator() {
        CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServletPrefix(),
                this.zuulProperties);
        routeLocator.setJdbcTemplate(jdbcTemplate);
        return routeLocator;
    }
    
}
