package com.roiland.platform.rest.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by user on 2015/11/24.
 */
public class ResponseFactory {

    public static Response error(Response.Status message) {
        return error(message.getStatusCode(), message.getReasonPhrase());
    }

    public static Response error(MessageEnum message) {
        return error(message.getCode(), message.getMessage());
    }

    public static Response error(Response.StatusType statusType) {
        return error(statusType.getStatusCode(), statusType.getReasonPhrase());
    }

    public static Response error(int code, String message) {
        return Response.ok(com.roiland.platform.rest.Response.code(code, message)).type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8")).build();
    }
}
