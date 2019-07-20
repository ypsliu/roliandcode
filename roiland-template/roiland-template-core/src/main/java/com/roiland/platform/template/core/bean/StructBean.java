package com.roiland.platform.template.core.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/4/7
 */
public class StructBean {

    private String beanName;

    private List<String> fieldName = new ArrayList<>();

    private List<Class<?>> fieldType = new ArrayList<>();

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public List<String> getFieldName() {
        return fieldName;
    }

    public void addFieldName(String fieldName) {
        this.fieldName.add(fieldName);
    }

    public List<Class<?>> getFieldType() {
        return fieldType;
    }

    public void addFieldType(Class<?> fieldType) {
        this.fieldType.add(fieldType);
    }

    @Override
    public String toString() {
        return "StructBean{" +
                "beanName='" + beanName + '\'' +
                ", fieldName=" + fieldName +
                ", fieldType=" + fieldType +
                '}';
    }
}
