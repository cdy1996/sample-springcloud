package com.cdy.customribbonloadbalancer.interceptor;

import com.cdy.customribbonloadbalancer.api.RibbonFilterContext;
import com.cdy.customribbonloadbalancer.support.RibbonFilterContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class HttpRequestInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Enumeration<String> headers = request.getHeaders("version");
        if (headers.hasMoreElements()) {
            RibbonFilterContext currentContext = RibbonFilterContextHolder.getCurrentContext();
            currentContext.getAttributes().put("version", headers.nextElement());
        }
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RibbonFilterContextHolder.clearCurrentContext();
    }
}