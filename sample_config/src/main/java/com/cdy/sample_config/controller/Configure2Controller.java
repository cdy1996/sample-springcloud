package com.cdy.sample_config.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 不用转义的接口
 * Created by 陈东一
 * 2018/6/30 13:51
 */
@RestController
@RequestMapping("config2")
public class Configure2Controller {
    private Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    ZKUtil zkUtil;
    
    
    @GetMapping("/list")
    public List list(@RequestParam String path) {
        log.info(path);
        return zkUtil.getChild(path);
    }
    
    
    @GetMapping("/get")
    public String data(@RequestParam String path) {
        log.info(path);
        return zkUtil.readData(path);
    }
    
    @PostMapping("/change")
    public Boolean changeData(@PathVariable String path, @RequestParam String data){
        log.info(path);
        log.info(data);
        boolean exists = zkUtil.isExists(path);
        if (exists) {
            return zkUtil.writeData(path, data);
        }
        return false;
    }
    
    @PostMapping("/add")
    public Boolean createData(@RequestParam String path, @RequestParam String data){
        log.info(path);
        log.info(data);
        boolean exists = zkUtil.isExists(path);
        if (exists) {
            return false;
        } else {
            return zkUtil.createPath(path, data);
        }
    }
    
    @GetMapping("/delete")
    public Boolean delData(@RequestParam String path){
        log.info(path);
        boolean exists = zkUtil.isExists(path);
        if (exists) {
            return zkUtil.deletePath(path);
        }
        return true;
    }
    
    
    
    
}
