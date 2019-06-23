package com.cdy.sample_client.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 * Created by 陈东一
 * 2019/3/18 0018 23:55
 */
@RestController
public class UpController {
    
    @Autowired
    MyHealth myHealthChecker;
    
    @RequestMapping("/up")
    public String up(@RequestParam("up") Boolean up) {
        myHealthChecker.setUp(up);
        
        return up.toString();
    }

}
