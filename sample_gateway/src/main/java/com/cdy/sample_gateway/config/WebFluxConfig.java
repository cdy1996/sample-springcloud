//package com.cdy.sample_gateway.config;
//
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.web.reactive.result.view.ViewResolver;
//
//import java.util.Collections;
//import java.util.List;
//
//@Configuration
//public class WebFluxConfig {
//
//    private final List<ViewResolver> viewResolvers;
//    private final ServerCodecConfigurer serverCodecConfigurer;
//
//    public WebFluxConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider,
//                         ServerCodecConfigurer serverCodecConfigurer) {
//        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
//        this.serverCodecConfigurer = serverCodecConfigurer;
//    }
//
//    @Bean
//    @Order(-1)
//    public SentinelBlockExceptionHandler sentinelBlockExceptionHandler() {
//        // Register the block exception handler for Spring WebFlux.
//        return new SentinelBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
//    }
//
//    @Bean
//    @Order(-1)
//    public SentinelWebFluxFilter sentinelWebFluxFilter() {
//        // Register the Sentinel WebFlux filter.
//        return new SentinelWebFluxFilter();
//    }
//}