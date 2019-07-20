package com.roiland.platform.template.core;

import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.support.TemplateSupport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/11
 */
public abstract class AbstractInvoker implements Invoker {

    protected final ResourceBean resourceBean;

    protected final Map<String, Object> globalParam;

    public ResourceBean getResourceBean() {
        return resourceBean;
    }

    public Map<String, Object> getGlobalParam() {
        return globalParam;
    }

    protected AbstractInvoker(Map<String, Object> globalParam, ResourceBean resourceBean) {
        this.globalParam = globalParam;
        this.resourceBean = resourceBean;
    }

    @Override
    public Object invoke(String param,InvokerCallback callback) throws Exception {
        ResourceBean bean = getTemplateSupport().bind(param, resourceBean);
        return handle(globalParam, bean,callback);
    }

    @Override
    public Object invoke(byte[] param,InvokerCallback callback) throws Exception {
        ResourceBean bean = getTemplateSupport().bind(param, resourceBean);
        return handle(globalParam, bean,callback);
    }

    @Override
    public Object invoke(InputStream param,InvokerCallback callback) throws Exception {
        ResourceBean bean = getTemplateSupport().bind(param, resourceBean);
        return handle(globalParam, bean,callback);
    }

    @Override
    public Object invoke(Reader param,InvokerCallback callback) throws Exception {
        ResourceBean bean = getTemplateSupport().bind(param, resourceBean);
        return handle(globalParam, bean,callback);
    }

    @Override
    public Object invoke(File param,InvokerCallback callback) throws Exception {
        ResourceBean bean = getTemplateSupport().bind(param, resourceBean);
        return handle(globalParam, bean,callback);
    }

    @Override
    public Object invoke(Map<String, Object> param,InvokerCallback callback) throws Exception {
        ResourceBean bean = getTemplateSupport().bind(param, resourceBean);
        return handle(globalParam, bean,callback);
    }

    protected abstract Object handle(Map<String, Object> object, ResourceBean bean,InvokerCallback callback) throws Exception;

    protected TemplateSupport getTemplateSupport() {
        return new TemplateSupport();
    }

    @Override
    public void close() throws IOException {
        //NOP
    }
}
