package com.roiland.platform.rest.resteasy;

import com.roiland.platform.rest.util.MessageEnum;
import com.roiland.platform.rest.util.ResponseFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2015/11/24.
 */
public class RestEasyExceptionFilter implements ExceptionMapper<Throwable> {

    private static final Log LOGGER = LogFactory.getLog(RestEasyExceptionFilter.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.error("未预料的异常", exception);
        if (exception instanceof WebApplicationException) {
            WebApplicationException webApplicationException = (WebApplicationException) exception;
            Response.StatusType statusType = webApplicationException.getResponse().getStatusInfo();
            return getResponse((WebApplicationException) exception, statusType);
        }
        return ResponseFactory.error(MessageEnum.UNEXPECTED);
    }

    private Response getResponse(WebApplicationException exception, Response.StatusType statusType) {
        Response response = exception.getResponse();
        if (response.getEntity() instanceof com.roiland.platform.rest.Response) {
            return response;
        } else {
            return ResponseFactory.error(statusType.getStatusCode(), statusType.getReasonPhrase());
        }
    }
}
