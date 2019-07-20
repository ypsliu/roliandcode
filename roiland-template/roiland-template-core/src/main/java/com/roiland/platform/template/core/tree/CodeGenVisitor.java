package com.roiland.platform.template.core.tree;

import com.roiland.platform.template.core.ast.Field;
import com.roiland.platform.template.core.ast.Resource;
import com.roiland.platform.template.core.ast.Struct;
import com.roiland.platform.template.core.ast.Variable;
import com.roiland.platform.template.core.bean.CodeGenResourceBean;
import com.roiland.platform.template.core.bean.CodeGenStructBean;
import com.roiland.platform.template.core.support.TypeSupport;

/**
 * @author leon.chen
 * @since 2016/4/8
 */
public class CodeGenVisitor extends AbstractResourceVisitor {

    private final CodeGenResourceBean resource = new CodeGenResourceBean();

    public void applyResource(Resource resource) {
        this.resource.setResource(resource.getResource());
        this.resource.setMethod(resource.getMethod());
        this.resource.setReturnType(TypeSupport.normalize(resource.getReturnType()));
        super.applyResource(resource);
    }

    public void applyStruct(Struct struct) {
        CodeGenStructBean structBean = new CodeGenStructBean();
        structBean.setBeanName(struct.getBean());
        for (Field field : struct.getFields()) {
            structBean.addFieldName(field.getName());
            structBean.addFieldType(TypeSupport.normalize(field.getType()));
        }
        this.resource.addStructBean(structBean);
        super.applyStruct(struct);
    }

    public void applyVariable(Variable variable) {
        this.resource.addParamName(variable.getName());
        this.resource.addParamType(TypeSupport.normalize(variable.getType()));
    }

    public CodeGenResourceBean getCodeGenResource() {
        return resource;
    }
}
