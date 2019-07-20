package com.roiland.platform.template.core.ast;

import com.roiland.platform.template.core.tree.ResourceVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/4/5
 */
public class Struct {

    private String bean;
    private List<Field> fields = new ArrayList<>();

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void accept(ResourceVisitor visitor){
        visitor.applyStruct(this);
    }
}
