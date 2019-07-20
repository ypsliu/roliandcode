package com.roiland.platform.template.core.tree;

import com.roiland.platform.template.core.ast.Field;
import com.roiland.platform.template.core.ast.Resource;
import com.roiland.platform.template.core.ast.Struct;
import com.roiland.platform.template.core.ast.Variable;

/**
 * @author leon.chen
 * @since 2016/4/5
 */
public abstract class AbstractResourceVisitor implements ResourceVisitor {

    @Override
    public void applyResource(Resource resource) {
        for(Struct struct : resource.getStruct()){
            struct.accept(this);
        }
        for(Variable variable : resource.getVariables()){
            variable.accept(this);
        }
    }

    @Override
    public void applyStruct(Struct struct) {
        for(Field field : struct.getFields()){
            field.accept(this);
        }
    }

    @Override
    public void applyVariable(Variable variable) {

    }

    @Override
    public void applyField(Field field) {

    }
}
