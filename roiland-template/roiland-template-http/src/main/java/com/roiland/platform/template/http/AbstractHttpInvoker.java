package com.roiland.platform.template.http;

import com.roiland.platform.template.core.AbstractInvoker;
import com.roiland.platform.template.core.bean.ResourceBean;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/11
 */
public abstract class AbstractHttpInvoker extends AbstractInvoker {

    protected AbstractHttpInvoker(Map<String, Object> globalParam, ResourceBean resourceBean) {
        super(globalParam, resourceBean);
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> getHeader(ResourceBean bean) {
        int index = getIndex(bean.getParamName(), "#headers");
        if (index == -1) {
            return Collections.emptyMap();
        }
        return (Map<String, String>) bean.getParamValue().get(index);
    }

    protected String getSecretKey(ResourceBean bean){
        int index = getIndex(bean.getParamName(),"#secretKey");
        if (index == -1) {
            return null;
        }
        return (String)bean.getParamValue().get(index);
    }

    protected Map<String, Object> getParam(ResourceBean bean) {
        Map<String, Object> map = new HashMap<>();
        List<String> paramNames = bean.getParamName();
        for(int i=0;i<paramNames.size();i++){
            if(!paramNames.get(i).startsWith("#")){
                map.put(bean.getParamName().get(i), bean.getParamValue().get(i));
            }
        }
        return map;
    }

    protected String getContentType(Map<String, Object> object) {
        String contentType = (String) object.get("content-type");
        if (contentType == null) {
            throw new UnsupportedOperationException("content-type invalid");
        }
        return contentType;
    }

    protected String getHost(Map<String, Object> object) {
        String host = (String) object.get("host");
        if (host == null) {
            throw new UnsupportedOperationException("host invalid");
        }
        return host.endsWith("/") ? host : host + "/";
    }

    protected String getMethod(ResourceBean bean){
        return bean.getMethod();
    }

    protected String getResource(ResourceBean bean) {
        String resource = bean.getResource();
        return resource.startsWith("/") ? resource.substring(1) : resource;
    }

    private int getIndex(List<String> paramName,String name) {
        return paramName.indexOf(name);
    }
}
