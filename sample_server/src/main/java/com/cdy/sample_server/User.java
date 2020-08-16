package com.cdy.sample_server;

/**
 * todo
 * Created by 陈东一
 * 2020/8/16 0016 17:59
 */
public class User {
    private String name;
    private Integer age;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
