package com.roiland.platform.restful.spring.config;

import com.roiland.platform.restful.chain.FilterChain;

import java.util.Map;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/12/18
 */
public class SpringPathFilterConfig {
    private String defaultFilters;
    private Map<String,String> pathFilters;
    private Map<String,Class<? extends FilterChain>> registerFilters;

    public String getDefaultFilters() {
        return defaultFilters;
    }

    public void setDefaultFilters(String defaultFilters) {
        this.defaultFilters = defaultFilters;
    }

    public Map<String, String> getPathFilters() {
        return pathFilters;
    }

    public void setPathFilters(Map<String, String> pathFilters) {
        this.pathFilters = pathFilters;
    }

    public Map<String, Class<? extends FilterChain>> getRegisterFilters() {
        return registerFilters;
    }

    public void setRegisterFilters(Map<String, Class<? extends FilterChain>> registerFilters) {
        this.registerFilters = registerFilters;
    }
}
