package com.roiland.platform.restful.request;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.List;

/**
 * Created by jeffy.yang on 16-3-28.
 */
public interface IHttpRequest extends ContainerRequestContext {

    void setParameter(String key, String value);

    String getFirstParameter(String key);

    List<String> getParameter(String key);
}
