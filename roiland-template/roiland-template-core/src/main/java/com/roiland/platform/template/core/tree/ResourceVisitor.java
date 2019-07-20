package com.roiland.platform.template.core.tree;

import com.roiland.platform.template.core.ast.Resource;

import com.roiland.platform.template.core.ast.Field;
import com.roiland.platform.template.core.ast.Struct;
import com.roiland.platform.template.core.ast.Variable;

/**
 * @author leon.chen
 * @since 2016/4/5
 */
public interface ResourceVisitor {

    void applyResource(Resource resource);

    void applyStruct(Struct struct);

    void applyVariable(Variable variable);

    void applyField(Field field);
}
