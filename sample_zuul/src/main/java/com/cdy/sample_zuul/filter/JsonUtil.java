package com.cdy.sample_zuul.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * json工具类
 * Created by 陈东一
 * 2018/5/15 22:30
 */
public class JsonUtil {
    
    /**
     * 对象转json
     * @param o Object
     * @return String
     */
    public static String toString(Object o){
        return object2StringByJackson(o);
    }
    
    /**
     * 解析json
     * @param json String
     * @param clazz Class<T> 类型
     * @param <T> T
     * @return T
     */
    public static <T> T parseObject(String json, Class<T> clazz){
        return string2ObjectByJackson(json, clazz);
    }
    
    /**
     * 解析json到数组
     * @param json String String
     * @param clazz Class<T> clazz 成员的类型
     * @param <T> T
     * @return T
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz){
        return string2ArrayByJackson(json);
    }
    
    private static String object2StringByJackson(Object o){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static <T> T string2ObjectByJackson(String json, Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static <T> List<T> string2ArrayByJackson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json,new TypeReference<List<T>>() { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    

    
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("789");
        String s = toString(list);
        List<String> strings = JsonUtil.string2ArrayByJackson(s);
        System.out.println(strings);
    
        
    }
    
}
