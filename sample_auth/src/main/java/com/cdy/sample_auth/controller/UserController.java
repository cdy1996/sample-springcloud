package com.cdy.sample_auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * todo
 * Created by 陈东一
 * 2018/7/8 14:38
 */
@RestController
public class UserController {
    
    
    @RequestMapping("/login")
    public ApiResult login(@RequestParam String username, @RequestParam String password) {
        if ("cdy".equals(username) && "123456".equals(password)) {
            return ApiResult.success(Base64Util.encode((username + password).getBytes()));
        }
        return ApiResult.fail("用户名或密码不对");
    }
    
    
    @RequestMapping("/check")
    public ApiResult login(@RequestParam String token) {
        try {
            if (Arrays.equals(("cdy" + "123456").getBytes(), Base64Util.decode(token))) {
                return ApiResult.success();
            }
        } catch (Exception e) {
            return ApiResult.fail("token不正确或者过期");
        }
        return ApiResult.fail("token不正确或者过期");
    }
}
