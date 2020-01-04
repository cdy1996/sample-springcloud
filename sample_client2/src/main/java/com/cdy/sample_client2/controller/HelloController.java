package com.cdy.sample_client2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * todo
 * Created by 陈东一
 * 2018/6/18 15:49
 */
@RestController
@RefreshScope
public class HelloController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${client.port:12000}")
    private String port;
    
    @Value("${client.service.id:client}")
    private String client;
    
    @RequestMapping("/hello")
    public Mono<String> hello() {
        String body = restTemplate.getForEntity("http://" + client + "/world", String.class).getBody();
        return Mono.just(body);
    }
    
    @RequestMapping("/world")
    public Mono<String> world(ServerHttpResponse response) {
        System.out.println(response);
        return Mono.just(client + ":" + port);
    }
    
    @RequestMapping("/file")
    public Mono<Void> file(ServerHttpResponse response) throws IOException {
        DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
        System.out.println(response);
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=pom.xml");
        response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        File file = new File("D:\\workspace\\ideaworkspace\\blog_project\\sample-springcloud\\sample_client2\\pom.xml");
        response.getHeaders().setContentLength(file.length());
        if (response instanceof ZeroCopyHttpOutputMessage) {
            System.out.println("零拷贝");
            return ((ZeroCopyHttpOutputMessage) response).writeWith(file, 0, file.length());
        } else {
            System.out.println("堆外内存");
            FileChannel open = FileChannel.open(file.toPath(), StandardOpenOption.READ);
            ByteBuffer allocate = ByteBuffer.allocate((int) file.length());
            open.read(allocate);
            return response.writeWith(Mono.just(factory.wrap(allocate))).doFinally(e->{
                try {
                    open.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }
    
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        String B = "𝄞"; // 这个就是那个音符字符，只不过由于当前的网页没支持这种编码，所以没显示。"
        String C = "\uD834\uDD1E";// 这个就是音符字符的UTF-16编码"
        System.out.println(C);
        System.out.println(B.length());
        System.out.println(Arrays.toString(B.getBytes("UTF-8")));
        System.out.println(Arrays.toString(B.getBytes("UTF-16")));
        System.out.println(Arrays.toString(B.getBytes("UTF-32")));
        System.out.println(B.codePointCount(0,B.length()));
    }
    
    
}
