package com.roiland.platform.restful.request.method.impl;

import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.factory.ResponseFactory;
import com.roiland.platform.restful.request.method.HttpRequest;
import com.roiland.platform.restful.utils.MapUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.util.HttpHeaderNames;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
public class PostHttpRequest extends HttpRequest {

    private static final Log LOGGER = LogFactory.getLog(PostHttpRequest.class);

    public PostHttpRequest(ContainerRequestContext context) {
        super(context);
        if (MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8").equals(context.getMediaType().withCharset("UTF-8"))) {
            context.getHeaders().putSingle(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset("UTF-8").toString());
        } else {
            context.abortWith(new ResponseFactory().error(Response.Status.UNSUPPORTED_MEDIA_TYPE));
            return;
        }

        try {
            super.params.putAll(MapUtils.toMap(nettyHttpRequest.getInputStream()));
            this.context.setProperty(Constants.PARAMS, super.params);
        } catch (IOException e) {
            context.abortWith(new ResponseFactory().error(Response.Status.BAD_REQUEST));
            return;
        }

        // JQuery请求扁平化处理
        MapUtils.flatten(super.nettyHttpRequest.getDecodedFormParameters(), super.params);
    }

    @Override
    public void setParameter(String key, String value) {
        super.params.put(key, value);
        super.nettyHttpRequest.getDecodedFormParameters().add(key, value);
    }

    @Override
    public String getFirstParameter(String key) {
        return super.nettyHttpRequest.getDecodedFormParameters().getFirst(key);
    }

    @Override
    public List<String> getParameter(String key) {
        return super.nettyHttpRequest.getDecodedFormParameters().get(key);
    }
}
