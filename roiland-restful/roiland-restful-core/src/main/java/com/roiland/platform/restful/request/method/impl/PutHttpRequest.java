package com.roiland.platform.restful.request.method.impl;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
public class PutHttpRequest extends PostHttpRequest {

    public PutHttpRequest(ContainerRequestContext context) {
        super(context);
    }
}
