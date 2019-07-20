package com.roiland.platform.template.core.bean;

import com.fasterxml.jackson.databind.JavaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/4/6
 */
public class ResourceBean implements Cloneable {

    private String resource;

    private String method;

    private List<String> paramName = new ArrayList<>();

    private Class<?> returnType;

    private List<Class<?>> paramType = new ArrayList<>();

    private List<JavaType> paramJavaType = new ArrayList<>();

    private List<Object> paramValue = new ArrayList<>();

    private List<StructBean> structBean = new ArrayList<>();

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getParamName() {
        return paramName;
    }

    public void addParamName(String paramName) {
        this.paramName.add(paramName);
    }

    public List<Class<?>> getParamType() {
        return paramType;
    }

    public void addParamType(Class<?> paramType) {
        this.paramType.add(paramType);
    }

    public List<JavaType> getParamJavaType() {
        return paramJavaType;
    }

    public void addParamJavaType(JavaType paramType) {
        this.paramJavaType.add(paramType);
    }

    public List<Object> getParamValue() {
        return paramValue;
    }

    public void addParamValue(Object paramValue) {
        this.paramValue.add(paramValue);
    }

    public void setParamValue(List<Object> paramValue) {
        this.paramValue = paramValue;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public List<StructBean> getStructBean() {
        return structBean;
    }

    public void addStructBean(StructBean structBean) {
        this.structBean.add(structBean);
    }

    public void setParamName(List<String> paramName) {
        this.paramName = paramName;
    }

    public void setParamType(List<Class<?>> paramType) {
        this.paramType = paramType;
    }

    public void setParamJavaType(List<JavaType> paramJavaType) {
        this.paramJavaType = paramJavaType;
    }

    public void setStructBean(List<StructBean> structBean) {
        this.structBean = structBean;
    }

    @Override
    public String toString() {
        return "ResourceBean{" +
                "resource='" + resource + '\'' +
                ", method='" + method + '\'' +
                ", paramName=" + paramName +
                ", returnType=" + returnType +
                ", paramType=" + paramType +
                ", paramJavaType=" + paramJavaType +
                ", paramValue=" + paramValue +
                ", structBean=" + structBean +
                '}';
    }

    public ResourceBean clone() {
        try {
            //shadow clone
            return (ResourceBean) super.clone();
        } catch (CloneNotSupportedException e) {
            ResourceBean bean = new ResourceBean();
            bean.setMethod(this.getMethod());
            bean.setResource(this.getResource());
            bean.setReturnType(this.getReturnType());
            bean.setStructBean(this.getStructBean());
            bean.setParamName(this.getParamName());
            bean.setParamType(this.getParamType());
            bean.setParamJavaType(this.getParamJavaType());
            bean.setParamValue(this.getParamValue());
            return bean;
        }
    }
}
