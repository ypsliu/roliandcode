package com.roiland.platform.template.core.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/4/8
 */
public class CodeGenStructBean {

    private String beanName;

    private List<String> fieldName = new ArrayList<>();

    private List<String> fieldType = new ArrayList<>();

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

    public List<String> getFieldType() {
        return fieldType;
    }

    public void addFieldType(String fieldType) {
        this.fieldType.add(fieldType);
    }

    @Override
    public String toString() {
        return "CodeGenStructBean{" +
                "beanName='" + beanName + '\'' +
                ", fieldName=" + fieldName +
                ", fieldType=" + fieldType +
                '}';
    }

}
