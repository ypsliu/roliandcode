package com.roiland.platform.restful.factory;

import com.roiland.platform.restful.request.method.HttpRequest;
import com.roiland.platform.restful.request.method.impl.GetHttpRequest;
import com.roiland.platform.restful.request.method.impl.PostHttpRequest;
import com.roiland.platform.restful.request.method.impl.PutHttpRequest;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;

public class HttpRequestFactory {

    public static HttpRequest getMethodRequest(ContainerRequestContext context) {
        final String method = context.getMethod();
        if (HttpMethod.GET.equals(method)) {
            return new GetHttpRequest(context);
        } else if (HttpMethod.PUT.equals(method)) {
            return new PutHttpRequest(context);
        } else if (HttpMethod.POST.equals(method)) {
            return  new PostHttpRequest(context);
        } else {
            return null;
        }
    }
}
