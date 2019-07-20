package com.roiland.platform.rest;

import com.roiland.platform.rest.util.MessageEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2015/11/23.
 */
public final class Response {

    public static final Map<String, Object> RESPONSE_0 = code(MessageEnum.SUCCESS.getCode());

    public static <T extends Object> Map<String, Object> ok(T data) {
        if (data == null) {
            return RESPONSE_0;
        } else {
            final Map<String, Object> params = new HashMap<>(3);
            params.putAll(RESPONSE_0);
            params.put("data", data);
            return params;
        }
    }

    public static Map<String, Object> code(int code) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("code", code);
        return params;
    }

    public static Map<String, Object> code(int code, String message) {
        final Map<String, Object> params = code(code);
        params.put("message", message);
        return params;
    }
}
