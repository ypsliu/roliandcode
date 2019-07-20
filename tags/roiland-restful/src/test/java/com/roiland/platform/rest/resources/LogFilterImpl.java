package com.roiland.platform.rest.resources;

import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;
import org.jboss.resteasy.plugins.server.netty.NettyHttpRequest;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * Created by user on 2015/12/4.
 */
@LogFilter
@Priority(3000)
public class LogFilterImpl implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println(getPath((PostMatchContainerRequestContext) requestContext));
    }

    private String getPath(PostMatchContainerRequestContext requestContext) {
        PostMatchContainerRequestContext ctx = requestContext;
        NettyHttpRequest servletRequest = (NettyHttpRequest) ctx.getHttpRequest();
        return servletRequest.getUri().getMatchingPath();
    }
}
