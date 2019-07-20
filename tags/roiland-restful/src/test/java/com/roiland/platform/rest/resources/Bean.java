package com.roiland.platform.rest.resources;

import com.roiland.platform.annotation.iface.beanutils.BeanProperty;

/**
 * Created by user on 2015/12/7.
 */
public class Bean {

    @BeanProperty("app_id")
    private String appId;

    private String version;

    private String[] fields;

    public Bean() {

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}