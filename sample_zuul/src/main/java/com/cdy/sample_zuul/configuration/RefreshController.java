package com.cdy.sample_zuul.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 刷新路由api
 * Created by 陈东一
 * 2018/7/8 9:24
 */
@RestController
public class RefreshController {
    @Autowired
    RefreshRouteService refreshRouteService;
    @Autowired
    ZuulHandlerMapping zuulHandlerMapping;
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @GetMapping("/refreshRoute")
    public String refresh() {
        refreshRouteService.refreshRoute();
        return "refresh success";
    }
    
    @RequestMapping("/watchRoute")
    public Object watchNowRoute() {
        //可以用debug模式看里面具体是什么
        Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
        Set<Map.Entry<String, Object>> entries = handlerMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    
        List<ZuulRouteVO> results = jdbcTemplate
                .query("select * from gateway_api_define where enabled = true ",
                        new BeanPropertyRowMapper<>(ZuulRouteVO.class));
        
        
    
        return results;
    }
    
}