package com.cdy.sample_zuul.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 动态刷新路由
 * Created by 陈东一
 * 2018/7/8 9:23
 */

@Service
public class RefreshRouteService {
    @Autowired
    ApplicationEventPublisher publisher;
    
    @Autowired
    RouteLocator routeLocator;
    
    public void refreshRoute() {
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }
}