package com.roiland.platform.restful.chain.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
public class PathFilterConfig {

    private Map<String, String> paths = null;

    public PathFilterConfig() {
        this.paths = Collections.synchronizedMap(new HashMap<String, String>());
    }

    public void put(String name, String filters) {
        this.paths.put(name, filters);
    }

    public String get(String name) {
        return this.paths.get(name);
    }

    public boolean containsKey(String name) {
        return this.paths.containsKey(name);
    }

    public Map<String, String> findAll() {
        return paths;
    }
}
