package com.roiland.platform.template.core.ast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roiland.platform.template.core.tree.ResourceVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/4/5
 */
public class Resource {

    private String resource;

    private String method;

    @JsonProperty("return")
    private String returnType;

    private List<Variable> variables = new ArrayList<>();

    private List<Struct> struct = new ArrayList<>();

    public void accept(ResourceVisitor visitor) {
        visitor.applyResource(this);
    }

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

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public List<Struct> getStruct() {
        return struct;
    }

    public void setStruct(List<Struct> struct) {
        this.struct = struct;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
