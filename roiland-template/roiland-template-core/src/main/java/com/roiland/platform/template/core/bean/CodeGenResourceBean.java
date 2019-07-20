package com.roiland.platform.template.core.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/4/8
 */
public class CodeGenResourceBean {

    private String resource;

    private String method;

    private List<String> paramName = new ArrayList<>();

    private String returnType;

    private List<String> paramType = new ArrayList<>();

    private List<CodeGenStructBean> structBean = new ArrayList<>();

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

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<String> getParamType() {
        return paramType;
    }

    public void addParamType(String paramType) {
        this.paramType.add(paramType);
    }

    public List<CodeGenStructBean> getStructBean() {
        return structBean;
    }

    public void addStructBean(CodeGenStructBean structBean) {
        this.structBean.add(structBean);
    }

    @Override
    public String toString() {
        return "CodeGenResourceBean{" +
                "resource='" + resource + '\'' +
                ", method='" + method + '\'' +
                ", paramName=" + paramName +
                ", returnType='" + returnType + '\'' +
                ", paramType=" + paramType +
                ", structBean=" + structBean +
                '}';
    }
}
