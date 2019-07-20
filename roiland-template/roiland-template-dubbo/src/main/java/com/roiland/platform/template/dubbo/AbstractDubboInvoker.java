package com.roiland.platform.template.dubbo;

import com.roiland.platform.template.core.AbstractInvoker;
import com.roiland.platform.template.core.bean.ResourceBean;

import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/11
 */
public abstract class AbstractDubboInvoker extends AbstractInvoker {

    protected AbstractDubboInvoker(Map<String, Object> globalParam, ResourceBean resourceBean) {
        super(globalParam, resourceBean);
    }
}
