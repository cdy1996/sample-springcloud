package com.cdy.sample_gateway.filter;

/**
 * todo
 * Created by 陈东一
 * 2019/2/11 0011 21:54
 */
public class Response {
    
    private  String data;
    private String code;
    private String message;
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
