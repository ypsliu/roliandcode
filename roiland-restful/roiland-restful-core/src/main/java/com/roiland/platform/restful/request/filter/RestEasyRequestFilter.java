package com.roiland.platform.restful.request.filter;

import com.roiland.platform.lang.UUIDUtils;
import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.chain.ChainSingleton;
import com.roiland.platform.restful.chain.FilterChain;
import com.roiland.platform.restful.factory.HttpRequestFactory;
import com.roiland.platform.restful.factory.ResponseFactory;
import com.roiland.platform.restful.request.method.HttpRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/11/18
 */
@Provider
@Priority(Priorities.USER)
public class RestEasyRequestFilter implements ContainerRequestFilter {

    private static final Log LOGGER = LogFactory.getLog(RestEasyRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 更正为addFirst解决污染问题
        requestContext.getHeaders().putSingle(Constants.BID, UUIDUtils.randomUUID().toString());

        final HttpRequest request = HttpRequestFactory.getMethodRequest(requestContext);
        if (request == null) {
            requestContext.abortWith(ResponseFactory.error(Response.Status.METHOD_NOT_ALLOWED));
            return;
        }
        //请求时间，monitor用
        requestContext.setProperty(Constants.REQUEST_TIMESTAMP, System.currentTimeMillis());
        // 获取请求Chain
        final String URI = requestContext.getUriInfo().getPath();
        FilterChain chain = ChainSingleton.getInstance().findChain(URI);
        if (chain != null)
            chain.execute(request);
    }
}
