package com.roiland.platform.restful.request.method.impl;

import com.roiland.platform.restful.request.method.HttpRequest;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
public class GetHttpRequest extends HttpRequest {

    public GetHttpRequest(ContainerRequestContext context) {
        super(context);
    }

    @Override
    public void setParameter(String key, String value) {
        super.nettyHttpRequest.getUri().getQueryParameters().add(key, value);
    }

    @Override
    public String getFirstParameter(String key) {
        return super.nettyHttpRequest.getUri().getQueryParameters().getFirst(key);
    }

    @Override
    public List<String> getParameter(String key) {
        return super.nettyHttpRequest.getUri().getQueryParameters().get(key);
    }
}
