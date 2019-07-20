package com.roiland.platform.monitor.http;

import com.roiland.platform.monitor.basic.extract.Marker;
import com.roiland.platform.monitor.basic.transform.DataBean;
import com.roiland.platform.monitor.basic.transform.InfoBean;
import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.request.method.HttpRequest;

import javax.annotation.Priority;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Provider
@Priority(Priorities.USER + 5000)
public class PostHttpMonitorFilter implements ContainerRequestFilter, ContainerResponseFilter, WriterInterceptor {

    private static final String APPLICATION = System.getProperty("project.name", "");
    private static final String MONITOR_TYPE = "http";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //bug fix，浏览器cors请求的时候要过滤掉OPTIONS
        if (containerRequestContext.getMethod().equalsIgnoreCase("OPTIONS") || containerRequestContext.getProperty("cors.failure") != null) {
            //when OPTION request. do nothing.
            return;
        }
        containerRequestContext.setProperty("payload", containerRequestContext.getEntityStream().available());
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        final HttpRequest request = getMethodRequest(containerRequestContext);
        final String PATH = request.getUriInfo().getPath();
        final String METHOD = request.getMethod();
        final String DEALER = request.getFirstParameter("dea_id");
        final String VERSION = request.getHeaderString("version");
        final String APP = request.getFirstParameter("app_id");
        final String SERVER = request.getUriInfo().getRequestUri().getHost() + ":" + request.getUriInfo().getRequestUri().getPort();
        final Integer INPUT = request.getProperty("payload") == null ? 0 : Integer.valueOf(request.getProperty("payload").toString());

        final Map<String, Object> entity = (Map<String, Object>) containerResponseContext.getEntity();
        final String BID = String.valueOf(entity.get(Constants.BID));
        final Integer CODE = Integer.valueOf(entity.get("code").toString());

        Integer success = 0;
        Integer failure = 0;
        if (CODE == 200) {
            success = 1;
        } else {
            failure = 1;
        }

        InfoBean infoBean = new InfoBean(InfoBean.TYPE.HTTP, APPLICATION, CODE == 404 ? "" : PATH, METHOD, DEALER, VERSION, APP, SERVER);
        DataBean dataBean = new DataBean(success, failure, INPUT, 0, 0, 0);
        Marker.getInstance().start(BID, infoBean, dataBean);
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        OutputStream out = context.getOutputStream();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        //设置OutputStream为ByteArrayOutputStream，方法调用之后会填充ByteArrayOutputStream，方便计算output payload
        context.setOutputStream(byteOut);
        final Integer output;
        try {
            //方法调用
            context.proceed();
        } finally {
            output = byteOut.size();
            byteOut.writeTo(out);
            byteOut.close();
            context.setOutputStream(out);
        }

        final Long TIMESTAMP = context.getProperty(Constants.REQUEST_TIMESTAMP) != null ? (Long) context.getProperty(Constants.REQUEST_TIMESTAMP) : 0;
        final Map<String, Object> entity = (Map<String, Object>) context.getEntity();
        final String BID = String.valueOf(entity.get(Constants.BID));

        DataBean dataBean = Marker.getInstance().getDataBean(BID);
        dataBean.setOutput(output);
        dataBean.setElapsed(TIMESTAMP == 0 ? 0 : Long.valueOf(System.currentTimeMillis() - TIMESTAMP).intValue());
        Marker.getInstance().stop(BID, dataBean);
    }

    /* bug fix ,在发送Post请求的时候，post请求会将application/json转化成application/x-www-form-urlencoded;所以如果使用 */
    /* HttpRequestFactory.getMethodRequest 的时候，在return  new PostHttpRequest(context);会返回错误的消息 */
    private HttpRequest getMethodRequest(final ContainerRequestContext requestContext) {
        if (requestContext.getMethod().equals(HttpMethod.DELETE)) {
            return null;
        }
        return new HttpRequest(requestContext) {
            @Override
            public void setParameter(String key, String value) {
                throw new UnsupportedOperationException("un-supported");
            }

            @Override
            public String getFirstParameter(String key) {
                switch (requestContext.getMethod()) {
                    case HttpMethod.GET:
                        return super.nettyHttpRequest.getUri().getQueryParameters().getFirst(key);
                    case HttpMethod.POST:
                    case HttpMethod.PUT:
                        return super.nettyHttpRequest.getDecodedFormParameters().getFirst(key);
                    default:
                        return null;
                }
            }

            @Override
            public List<String> getParameter(String key) {
                switch (requestContext.getMethod()) {
                    case HttpMethod.GET:
                        return super.nettyHttpRequest.getUri().getQueryParameters().get(key);
                    case HttpMethod.POST:
                    case HttpMethod.PUT:
                        return super.nettyHttpRequest.getDecodedFormParameters().get(key);
                    default:
                        return null;
                }
            }
        };
    }

}
