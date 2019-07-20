package com.roiland.platform.sync.plugins.core;

import java.io.Serializable;

/**
 * @author leon.chen
 * @since 2016/9/21
 */
public class SyncRecord implements Serializable {
    private String key;
    private Object value;
    private SyncRecordType type;

    public SyncRecord(String key, Object value, SyncRecordType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SyncRecordType getType() {
        return type;
    }

    public void setType(SyncRecordType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SyncRecord{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
