package com.cdy.sample_server;

import java.util.Collection;

/**
 * 容器类
 *
 * @author pancc
 * @version 1.0
 */

public class Container {
    /**
     * 容器的位置，可以为  null 或者空字符串
     */
    private String location;
    /**
     * 容器中的所有数值，可以为 null 或者空
     */
    private Collection<Integer> integers;
    
    
    public Container() {
    }
    
    public Container(String location, Collection<Integer> integers) {
        this.location = location;
        this.integers = integers;
    }
    
    public String getLocation() {
        return location;
    }
    
    public Container setLocation(String location) {
        this.location = location;
        return this;
    }
    
    public Collection<Integer> getIntegers() {
        return integers;
    }
    
    public Container setIntegers(Collection<Integer> integers) {
        this.integers = integers;
        return this;
    }
}