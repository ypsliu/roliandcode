package com.roiland.platform.template.core.ast;

import com.roiland.platform.template.core.tree.ResourceVisitor;

/**
 * @author leon.chen
 * @since 2016/4/5
 */
public class Variable {
    private String name;
    private String type;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void accept(ResourceVisitor visitor){
        visitor.applyVariable(this);
    }
}
