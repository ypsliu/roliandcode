package com.roiland.platform.restful.factory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/11/24
 */
public final class ResponseFactory {

    public static Response error(Response.Status status) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("code", status.getStatusCode());
        params.put("message", status.getReasonPhrase());
        return Response.ok(params).type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8")).build();
    }

    public static Response error(int code, String message) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("code", code);
        params.put("message", message);
        return Response.ok(params).type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8")).build();
    }

    public static Response error(int code) {
        return error(Response.Status.fromStatusCode(code));
    }
}
