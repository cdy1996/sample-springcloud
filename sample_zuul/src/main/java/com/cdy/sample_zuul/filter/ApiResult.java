package com.cdy.sample_zuul.filter;

/**
 * todo
 * Created by 陈东一
 * 2018/7/8 14:44
 */
public class ApiResult {
    /** 错误码. */
    private Integer code;
    
    /** 提示信息. */
    private String msg;
    
    /** 具体内容. */
    private Object data;
    
    
    public static ApiResult success() {
        return new ApiResult().setCode(200).setMsg("success");
    }
    
    public static ApiResult success(Object data) {
        return new ApiResult().setCode(200).setMsg("success").setData(data);
    }
    
    public static ApiResult fail() {
        return new ApiResult().setCode(400).setMsg("fail");
    }
    
    public static ApiResult fail(String msg) {
        return new ApiResult().setCode(400).setMsg("msg");
    }
    
    
    public Integer getCode() {
        return code;
    }
    
    public ApiResult setCode(Integer code) {
        this.code = code;
        return this;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public ApiResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    
    public Object getData() {
        return data;
    }
    
    public ApiResult setData(Object data) {
        this.data = data;
        return this;
    }
}
