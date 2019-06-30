package com.cdy.customribbonloadbalancer.interceptor;

import com.cdy.customribbonloadbalancer.api.RibbonFilterContext;
import com.cdy.customribbonloadbalancer.support.RibbonFilterContextHolder;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;

import static com.cdy.customribbonloadbalancer.support.DefaultRibbonFilterContext.VERSION;

public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(feign.RequestTemplate template) {
        RibbonFilterContext currentContext = RibbonFilterContextHolder.getCurrentContext();
      
        String version = currentContext.getAttributes().get(VERSION);
        if (StringUtils.isNotBlank(version)) {
            template.header(VERSION, version);
    
        }
    }
}