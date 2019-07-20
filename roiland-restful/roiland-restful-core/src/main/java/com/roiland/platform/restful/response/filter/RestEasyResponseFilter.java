package com.roiland.platform.restful.response.filter;

import com.roiland.platform.lang.UUIDUtils;
import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.utils.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.jboss.resteasy.core.interception.ResponseContainerRequestContext;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
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
public class RestEasyResponseFilter implements ContainerResponseFilter {

    private static final Log LOGGER = LogFactory.getLog(RestEasyResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS") || requestContext.getProperty("cors.failure") != null) {
            //when OPTION request. do nothing.
            return;
        }

        /**
         *  从请求头中获取业务ID，如果请求头中不包含业务ID，则新生成
         */
        final String BID;
        if (requestContext.getHeaders().containsKey(Constants.BID)) {
            BID = requestContext.getHeaderString(Constants.BID);
        } else {
            BID = UUIDUtils.randomUUID().toString();
        }

        /**
         * 返回实体如果为Map类型，则请求被正确处理，否则视为异常
         */
        final Map<String, Object> entity;
        if (responseContext.getEntity() instanceof Map) {
            entity = (Map)responseContext.getEntity();
        } else {
            entity = new HashMap<>(3);

            final Integer code;
            final String phrase;
            /**
             * WebApplicationException异常即被框架捕获异常，根据框架定义Code返回对应状态。
             * 其他异常全部视为系统异常
             */
            if (responseContext.getEntity() instanceof WebApplicationException) {
                WebApplicationException webApplicationException = (WebApplicationException) responseContext.getEntity() ;
                Response.StatusType status = webApplicationException.getResponse().getStatusInfo();
                code = status.getStatusCode();
                phrase = status.getReasonPhrase();
            } else {
                Response.Status status = Response.Status.fromStatusCode(500);
                code = status.getStatusCode();
                phrase = status.getReasonPhrase();
            }
            entity.put("code", code);
            entity.put("message", phrase);

            if (responseContext.getEntity() instanceof Throwable) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[BID:" + BID + "]", (Throwable) responseContext.getEntity());
                } else {
                    LOGGER.info("[BID:" + BID + "]" + ((Throwable) responseContext.getEntity()).getMessage());
                }

            }
		}
        // 返回数据绑定业务ID
        entity.put(Constants.BID, BID);
        responseContext.setEntity(entity);
    }
}
