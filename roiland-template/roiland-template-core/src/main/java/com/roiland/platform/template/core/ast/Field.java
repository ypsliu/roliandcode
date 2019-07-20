package com.roiland.platform.template.core.ast;

import com.roiland.platform.template.core.tree.ResourceVisitor;

/**
 * @author leon.chen
 * @since 2016/4/5
 */
public class Field {
    private String name;
    private String type;

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

    public void accept(ResourceVisitor visitor){
        visitor.applyField(this);
    }
}
