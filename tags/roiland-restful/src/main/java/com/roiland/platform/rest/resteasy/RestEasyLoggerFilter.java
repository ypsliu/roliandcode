package com.roiland.platform.rest.resteasy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class RestEasyLoggerFilter implements ContainerResponseFilter {

    private static final Log LOGGER = LogFactory.getLog(RestEasyLoggerFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(responseContext.getEntity());
        }
    }
}
