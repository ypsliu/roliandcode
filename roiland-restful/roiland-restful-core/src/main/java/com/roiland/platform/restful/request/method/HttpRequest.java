package com.roiland.platform.restful.request.method;

import com.roiland.platform.restful.request.IHttpRequest;
import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;
import org.jboss.resteasy.core.interception.PreMatchContainerRequestContext;
import org.jboss.resteasy.plugins.server.netty.NettyHttpRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

/**
 * Created by jeffy.yang on 16-3-25.
 */
public abstract class HttpRequest implements IHttpRequest {

    protected Map<String, Object> params = new HashMap<>();

    protected PreMatchContainerRequestContext context = null;
    protected NettyHttpRequest nettyHttpRequest = null;

    public HttpRequest(ContainerRequestContext context) {
        this.context = (PreMatchContainerRequestContext) context;
        this.nettyHttpRequest = (NettyHttpRequest) this.context.getHttpRequest();
    }

    public NettyHttpRequest getHttpRequest() {
        return nettyHttpRequest;
    }

    public abstract void setParameter(String key, String value);

    public abstract String getFirstParameter(String key);

    public abstract List<String> getParameter(String key);

    @Override
    public Object getProperty(String name) {
        return context.getProperty(name);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return context.getPropertyNames();
    }

    @Override
    public void setProperty(String name, Object object) {
        context.setProperty(name, object);
    }

    @Override
    public void removeProperty(String name) {
        context.removeProperty(name);
    }

    @Override
    public UriInfo getUriInfo() {
        return context.getUriInfo();
    }

    @Override
    public void setRequestUri(URI requestUri) {
        context.setRequestUri(requestUri);
    }

    @Override
    public void setRequestUri(URI baseUri, URI requestUri) {
        context.setRequestUri(baseUri, requestUri);
    }

    @Override
    public Request getRequest() {
        return context.getRequest();
    }

    @Override
    public String getMethod() {
        return context.getMethod();
    }

    @Override
    public void setMethod(String method) {
        context.setMethod(method);
    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        return context.getHeaders();
    }

    @Override
    public String getHeaderString(String name) {
        return context.getHeaderString(name);
    }

    @Override
    public Date getDate() {
        return context.getDate();
    }

    @Override
    public Locale getLanguage() {
        return context.getLanguage();
    }

    @Override
    public int getLength() {
        return context.getLength();
    }

    @Override
    public MediaType getMediaType() {
        return context.getMediaType();
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        return context.getAcceptableMediaTypes();
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        return context.getAcceptableLanguages();
    }

    @Override
    public Map<String, Cookie> getCookies() {
        return context.getCookies();
    }

    @Override
    public boolean hasEntity() {
        return context.hasEntity();
    }

    @Override
    public InputStream getEntityStream() {
        return context.getEntityStream();
    }

    @Override
    public void setEntityStream(InputStream input) {
        context.setEntityStream(input);
    }

    @Override
    public SecurityContext getSecurityContext() {
        return context.getSecurityContext();
    }

    @Override
    public void setSecurityContext(SecurityContext context) {

    }

    @Override
    public void abortWith(Response response) {
        context.abortWith(response);
    }
}
