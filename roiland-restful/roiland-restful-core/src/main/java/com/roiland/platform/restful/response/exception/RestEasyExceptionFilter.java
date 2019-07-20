package com.roiland.platform.restful.response.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/11/24
 */
@Provider
public class RestEasyExceptionFilter implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException webApplicationException = (WebApplicationException) exception;
            Response response = webApplicationException.getResponse();
            if (response.getEntity() instanceof Map) {
                return response;
            }
        }
        return Response.ok(exception).type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8")).build();
    }
}
