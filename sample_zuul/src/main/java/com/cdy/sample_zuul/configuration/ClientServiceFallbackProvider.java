package com.cdy.sample_zuul.configuration;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * todo
 * Created by 陈东一
 * 2018/6/23 10:41
 */
//@Component
public class ClientServiceFallbackProvider implements FallbackProvider {
    
    @Override
    public String getRoute() {
        return "client";
    }
    
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause != null && cause.getCause() != null) {
            String reason = cause.getCause().getMessage();
            System.err.printf("Excption %s route %s", reason,route);
            System.out.println();
        }
        return fallbackResponse();
    }
    
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }
        
            @Override
            public int getRawStatusCode() throws IOException {
                return this.getStatusCode().value();
            }
        
            @Override
            public String getStatusText() throws IOException {
                return this.getStatusCode().getReasonPhrase();
            }
        
            @Override
            public void close() {
            
            }
        
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("client不可用".getBytes());
            }
        
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                return headers;
            }
        };
    }
}
