package com.cdy.sample_config.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * restful config api
 *  需要转义  -0 => /   -1 => -
 * Created by 陈东一
 * 2018/6/30 13:51
 */
@RestController
@RequestMapping("config")
public class ConfigureController {
    private Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    ZKUtil zkUtil;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new CustomStringEditor());
    }
    
    @GetMapping("/list/{path}")
    public List list(@PathVariable String path) {
        log.info(path);
        return zkUtil.getChild(path);
    }
    
    
    @GetMapping("/{path}")
    public String data(@PathVariable String path) {
        log.info(path);
        return zkUtil.readData(path);
    }
    
    @PutMapping("/{path}")
    public Boolean changeData(@PathVariable String path, @RequestParam String data){
        log.info(path);
        log.info(data);
        boolean exists = zkUtil.isExists(path);
        if (exists) {
            return zkUtil.writeData(path, data);
        }
        return false;
    }
    
    @PostMapping("/{path}")
    public Boolean createData(@PathVariable String path, @RequestParam String data){
        log.info(path);
        log.info(data);
        boolean exists = zkUtil.isExists(path);
        if (exists) {
            return false;
        } else {
            return zkUtil.createPath(path, data);
        }
    }
    
    @DeleteMapping("/{path}")
    public Boolean delData(@PathVariable String path){
        log.info(path);
        boolean exists = zkUtil.isExists(path);
        if (exists) {
            return zkUtil.deletePath(path);
        }
        return true;
    }
    
    
    
    
}
