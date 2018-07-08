package com.cdy.sample_zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * todo
 * Created by 陈东一
 * 2018/7/8 14:56
 */
@Component
public class AccessFilter extends ZuulFilter {
    
    @Autowired
    RestTemplate restTemplate;
    
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        
        System.out.println(String.format("%s %s", request.getMethod(), request.getRequestURL().toString()));
        
        String requestURI = request.getRequestURI();
        
        //请求的是登陆地址
        if (requestURI.toLowerCase().contains("auth/login")) {
            String username = request.getParameter("username");// 获取请求的参数
            String password = request.getParameter("password");// 获取请求的参数
            if (username == null || password == null) {
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(400);// 返回错误码
                ctx.setResponseBody("{\"result\":\"username 或 password 不能为空!\"}");// 返回错误内容
                ctx.set("isSuccess", false);
                return null;
            }
        } else if (requestURI.toLowerCase().contains("auth/check")) {
            String token = request.getParameter("token");
            if (token == null) {
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(401);// 返回错误码
                ctx.setResponseBody("{\"result\":\"token 不能为空 !\"}");// 返回错误内容
                ctx.getResponse().setContentType("application/json;charset=UTF-8");
                ctx.set("isSuccess", false);
                return null;
            }
        } else {
            String token = request.getHeader("token");
            if (token == null) {
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(401);// 返回错误码
                ctx.setResponseBody("{\"result\":\"token 不能为空 !\"}");// 返回错误内容
                ctx.getResponse().setContentType("application/json;charset=UTF-8");
                ctx.set("isSuccess", false);
                return null;
            }
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            String url = "http://auth/check?token={token}";
            String response = restTemplate.getForEntity(url, String.class, map).getBody();
            ApiResult body = JsonUtil.parseObject(response, ApiResult.class);
            if (body.getCode() != 200) {
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(body.getCode());// 返回错误码
                ctx.setResponseBody("{\"result\":\"" + body.getMsg() + "\"}");// 返回错误内容
                ctx.getResponse().setContentType("application/json;charset=UTF-8");
                ctx.set("isSuccess", false);
                return null;
            }
        }
        ctx.setSendZuulResponse(true);// 对该请求进行路由
        ctx.setResponseStatusCode(200);
        ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
        return null;
    }
    
    @Override
    public boolean shouldFilter() {
        return true;// 是否执行该过滤器，此处为true，说明需要过滤
    }
    
    @Override
    public int filterOrder() {
        return 0;// 优先级为0，数字越大，优先级越低
    }
    
    @Override
    public String filterType() {
        return "pre";// 前置过滤器
    }
    
}
