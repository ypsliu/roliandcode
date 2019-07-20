package com.roiland.platform.restful.chain.config;

import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.chain.FilterChain;
import com.roiland.platform.spi.RoilandServiceLoader;

import java.util.Map;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
public class FilterConfig {

    private Map<String, Class<? extends FilterChain>> filters = null;

    public FilterConfig() {
        this.filters = RoilandServiceLoader.getProviders(Constants.FILTER, FilterChain.class, this.getClass().getClassLoader());
    }

    public <T extends FilterChain> void put(String name, Class<T> clazz) {
        this.filters.put(name, clazz);
    }

    public <T extends FilterChain> void add(Class<T> clazz) {
        this.filters.put(clazz.getSimpleName(), clazz);
    }

    public boolean containsKey(String name) {
        return filters.containsKey(name);
    }

    public Class<? extends FilterChain> get(String name) {
        return filters.get(name);
    }
}
