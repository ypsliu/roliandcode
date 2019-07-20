package com.roiland.platform.restful.request.interceptor;

import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.utils.MapUtils;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.util.Map;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/12/11
 */
@Provider
public class RestEasyRequestInterceptor implements ReaderInterceptor {

    @Override
    public Object aroundReadFrom(final ReaderInterceptorContext context) {
        Map<String, Object> map = (Map) context.getProperty(Constants.PARAMS);
        return MapUtils.fromStream(map, context.getType());
    }
}
