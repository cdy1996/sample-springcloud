package com.cdy.customribbonloadbalancer.support;

import com.cdy.customribbonloadbalancer.api.RibbonFilterContext;

public class RibbonFilterContextHolder {
    
    /**
     * Stores the {@link RibbonFilterContext} for current thread.
     */
    private static final ThreadLocal<RibbonFilterContext> contextHolder = new InheritableThreadLocal<RibbonFilterContext>() {
        @Override
        protected RibbonFilterContext initialValue() {
            return new DefaultRibbonFilterContext();
        }
    };
    
    /**
     * Retrieves the current thread bound instance of {@link RibbonFilterContext}.
     *
     * @return the current context
     */
    public static RibbonFilterContext getCurrentContext() {
        return contextHolder.get();
    }
    
    /**
     * Clears the current context.
     */
    public static void clearCurrentContext() {
        contextHolder.remove();
    }
}