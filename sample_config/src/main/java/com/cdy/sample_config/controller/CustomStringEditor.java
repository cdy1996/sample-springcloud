package com.cdy.sample_config.controller;

import java.beans.PropertyEditorSupport;

/**
 * todo
 * Created by 陈东一
 * 2018/6/30 14:24
 */
public class CustomStringEditor extends PropertyEditorSupport {
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text.replaceAll("-0", "/")
                .replaceAll("-1", "-"));
    }
    
    @Override
    public String getAsText() {
        return getAsText().toString();
    }
}
