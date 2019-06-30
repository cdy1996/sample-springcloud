package com.cdy.customribbonloadbalancer.support;

import com.cdy.customribbonloadbalancer.api.RibbonFilterContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultRibbonFilterContext implements RibbonFilterContext {
    
    public static final String VERSION = "version";
    
    /**
     * Filter attributes.
     */
    private final Map<String, String> attributes = new HashMap<>();
    
    /**
     * Adds the context attribute.
     *
     * @param key   the attribute key
     * @param value the attribute value
     */
    @Override
    public RibbonFilterContext add(String key, String value) {
        attributes.put(key, value);
        return this;
    }
    
    /**
     * Retrieves the context attribute.
     *
     * @param key the attribute key
     * @return the attribute value
     */
    @Override
    public String get(String key) {
        return attributes.get(key);
    }
    
    /**
     * Removes the context attribute.
     *
     * @param key the context attribute
     */
    @Override
    public RibbonFilterContext remove(String key) {
        attributes.remove(key);
        return this;
    }
    
    /**
     * Retrieves the attributes.
     *
     * @return the attributes
     */
    @Override
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}