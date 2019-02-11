package com.cdy.sample_zuul.controller;

import com.cdy.sample_zuul.filter.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * todo
 * Created by 陈东一
 * 2019/2/11 0011 21:58
 */
@Controller
public class ErrorController {
    @GetMapping("/fallback")
    public Response fallback() {
        Response response = new Response();
        response.setCode("100");
        response.setMessage("服务暂时不可用");
        return response;
    }
}
