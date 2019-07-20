package com.roiland.platform.restful.response.filter;

import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.utils.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.jboss.resteasy.core.interception.ResponseContainerRequestContext;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/11/18
 */
@Provider
public class RestEasyLogFilter implements ContainerResponseFilter {

    private static final Log LOGGER = LogFactory.getLog(RestEasyLogFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final Map<String, Object> params = (Map<String, Object>) responseContext.getEntity();
        final String BID = params.containsKey(Constants.BID)? (String)params.get(Constants.BID): "";

        // 移除请求头
        this.deleteHeaders(requestContext);

        // 请求头信息
        final Map tmpRequest;

        // 请求参数信息
        if ("GET".equals(requestContext.getMethod())) {
            tmpRequest = requestContext.getUriInfo().getQueryParameters();
        } else {
            final ResponseContainerRequestContext context = (ResponseContainerRequestContext) requestContext;
            final InputStream inputStream = context.getHttpRequest().getInputStream();
            inputStream.reset();
            tmpRequest = MapUtils.toMap(inputStream);
        }
        final Map tmpHeader = requestContext.getHeaders();
        LOGGER.info("[BID:" + BID + "] " + new ResponseLogBean(tmpHeader, tmpRequest, params));
    }

    private static final String[] HEADERS = {
            HttpHeaders.ACCEPT,
            HttpHeaders.ACCEPT_CHARSET,
            HttpHeaders.ACCEPT_ENCODING,
            HttpHeaders.ACCEPT_LANGUAGE,
            HttpHeaders.ALLOW,
//            HttpHeaders.AUTHORIZATION,
            HttpHeaders.CACHE_CONTROL,
            HttpHeaders.CONNECTION,
            HttpHeaders.CONTENT_LOCATION,
            HttpHeaders.CONTENT_ENCODING,
//            HttpHeaders.CONTENT_MD5,
            HttpHeaders.CONTENT_LANGUAGE,
//            HttpHeaders.CONTENT_LENGTH,
            HttpHeaders.CONTENT_LOCATION,
            HttpHeaders.CONTENT_RANGE,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.DATE,
            HttpHeaders.ETAG,
            HttpHeaders.EXPIRES,
//            HttpHeaders.HOST,
            HttpHeaders.IF_MATCH,
            HttpHeaders.IF_MODIFIED_SINCE,
            HttpHeaders.IF_NONE_MATCH,
            HttpHeaders.IF_UNMODIFIED_SINCE,
            HttpHeaders.LAST_MODIFIED,
            HttpHeaders.LOCATION,
            HttpHeaders.RETRY_AFTER,
            HttpHeaders.USER_AGENT,
            HttpHeaders.VARY,
            HttpHeaders.WWW_AUTHENTICATE
    };

    private void deleteHeaders(ContainerRequestContext requestContext) {
        for(String header : HEADERS) {
            requestContext.getHeaders().remove(header);
        }
    }

    private class ResponseLogBean {

        public Map<String, Object> result;

        public ResponseLogBean(Map<String, Object> header, Map<String, Object> request, Map<String, Object> response) {
            final Map<String, Object> tmp = new HashMap<>(response);
            if (tmp.containsKey("data") && tmp.get("data") instanceof List) {
                tmp.remove("data");
            }

            result = new HashMap<>(3);
            result.put("Header", header);
            result.put("Request", request);
            result.put("Response", tmp);
        }

        public String toString() {
            return MapUtils.toString(result);
        }
    }
}
