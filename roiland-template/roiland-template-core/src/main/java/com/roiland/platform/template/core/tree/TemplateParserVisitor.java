package com.roiland.platform.template.core.tree;

import com.fasterxml.jackson.databind.JavaType;
import com.roiland.platform.template.core.ast.Field;
import com.roiland.platform.template.core.ast.Resource;
import com.roiland.platform.template.core.ast.Struct;
import com.roiland.platform.template.core.ast.Variable;
import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.bean.StructBean;
import com.roiland.platform.template.core.support.TypeSupport;

/**
 * @author leon.chen
 * @since 2016/4/5
 */
public class TemplateParserVisitor extends AbstractResourceVisitor {

    private final ResourceBean resource = new ResourceBean();

    public TemplateParserVisitor() {
    }

    public void applyResource(Resource resource) {
        this.resource.setResource(resource.getResource());
        this.resource.setMethod(resource.getMethod());
        this.resource.setReturnType(TypeSupport.getJavaType(resource.getReturnType()).getRawClass());
        super.applyResource(resource);
    }

    public void applyStruct(Struct struct) {
        StructBean structBean = new StructBean();
        structBean.setBeanName(struct.getBean());
        for (Field field : struct.getFields()) {
            structBean.addFieldName(field.getName());
            structBean.addFieldType(TypeSupport.getJavaType(field.getType()).getRawClass());
        }
        this.resource.addStructBean(structBean);
        super.applyStruct(struct);
    }

    public void applyVariable(Variable variable) {
        JavaType type = TypeSupport.getJavaType(variable.getType());
        this.resource.addParamName(variable.getName());
        this.resource.addParamType(type.getRawClass());
        this.resource.addParamJavaType(type);
        this.resource.addParamValue(variable.getValue());
    }

    public ResourceBean getResource() {
        return resource;
    }
}
