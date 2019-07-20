package com.roiland.platform.rest.filter;

import java.util.Map;

/**
 * Created by user on 2015/12/10.
 */
public class FilterBean {

    public FilterBean(String path, Map<String, Object> params, Map<String, String> extension) {
        this.path = path;
        this.params = params;
        this.extension = extension;
    }

    private String path;

    private Map<String, Object> params;

    private Map<String, String> extension;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, String> getExtension() {
        return extension;
    }

    public void addExtension(String key, String value) {
        this.extension.put(key, value);
    }
}
