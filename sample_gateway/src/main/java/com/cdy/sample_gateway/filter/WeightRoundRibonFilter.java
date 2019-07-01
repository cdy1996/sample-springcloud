package com.cdy.sample_gateway.filter;

import com.cdy.customribbonloadbalancer.api.RibbonFilterContext;
import com.cdy.customribbonloadbalancer.support.RibbonFilterContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * todo
 * Created by 陈东一
 * 2019/2/11 0011 21:55
 */

@Component
public class WeightRoundRibonFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println(Thread.currentThread().getName());
        String version = exchange.getRequest().getHeaders().getFirst("version");
        if (StringUtils.isNotBlank(version)) {
            RibbonFilterContext currentContext = RibbonFilterContextHolder.getCurrentContext();
            currentContext.add("version", version);
        }
        Mono<Void> mono = chain.filter(exchange)
                .subscriberContext(ctx -> ctx.put("version", version))
                //reactor  对所有请求都正常处理完成后加一个响应参数，或者是打印日志。
                .doFinally(signal ->  RibbonFilterContextHolder.clearCurrentContext());
        return mono;
        
    }
    
    @Override
    public int getOrder() {
        return 10000;
    }
}