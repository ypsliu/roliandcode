package com.roiland.platform.rest.resteasy;

import com.roiland.platform.rest.filter.AbstractFilterChain;
import com.roiland.platform.rest.filter.FilterBean;
import com.roiland.platform.rest.filter.IFilterChain;
import com.roiland.platform.rest.filter.auth.BlackListAuthorizationFilter;
import com.roiland.platform.rest.filter.auth.StatisticsAuthorizationFilter;
import com.roiland.platform.rest.filter.auth.ValidationAuthorizationFilter;
import com.roiland.platform.rest.filter.auth.WhiteListAuthorizationFilter;
import com.roiland.platform.rest.util.BeanUtils;
import com.roiland.platform.rest.util.MessageEnum;
import com.roiland.platform.rest.util.ResponseFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;
import org.jboss.resteasy.plugins.server.netty.NettyHttpRequest;
import org.jboss.resteasy.util.HttpHeaderNames;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.roiland.platform.rest.util.Constants.AUTH_PARAMS;
import static com.roiland.platform.rest.util.Constants.AUTH_PARAM_FIELDS;

/**
 * Created by user on 2015/11/18.
 */
public class RestEasyRequestFilter implements ContainerRequestFilter, ReaderInterceptor {

    private static final Log LOGGER = LogFactory.getLog(RestEasyRequestFilter.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final IFilterChain chain;

    public RestEasyRequestFilter() {
        //方便扩展SPI
        List<AbstractFilterChain> list = new ArrayList<>();
//        list.add(new ValidationAuthorizationFilter());
//        list.add(new BlackListAuthorizationFilter());
//        list.add(new WhiteListAuthorizationFilter());
//        list.add(new StatisticsAuthorizationFilter());
        chain = constructChain(list);
    }

    private IFilterChain constructChain(List<AbstractFilterChain> list) {
        IFilterChain last = new IFilterChain() {
            @Override
            public void doFilter(FilterBean bean) throws WebApplicationException {

            }
        };
        for (int i = list.size() - 1; i >= 0; i--) {
            AbstractFilterChain filter = list.get(i);
            filter.setChain(last);
            last = filter;
        }
        return last;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        PostMatchContainerRequestContext ctx = (PostMatchContainerRequestContext) requestContext;
        NettyHttpRequest request = (NettyHttpRequest) ctx.getHttpRequest();
        String path = request.getUri().getMatchingPath();

        final Map<String, Object> map;
        final Map<String, String> extension = new HashMap<>();
        switch (requestContext.getMethod()) {
            case HttpMethod.GET:
                map = new HashMap<>(AUTH_PARAMS.length);
                final MultivaluedMap<String, String> parameters = request.getUri().getQueryParameters();
                for (String param : AUTH_PARAMS) {
                    if (param.equals(AUTH_PARAM_FIELDS)) {
                        //fields是数组类型，所以可能取得多条内容
                        map.put(param, parameters.get(param));
                    } else {
                        map.put(param, parameters.getFirst(param));
                    }
                }
                chain.doFilter(new FilterBean(path, map, extension));
                //回写filter扩展的数据
                for (Map.Entry<String, String> entry : extension.entrySet()) {
                    request.getUri().getQueryParameters().putSingle(entry.getKey(), entry.getValue());
                }
                break;
            case HttpMethod.PUT:
            case HttpMethod.POST:
                if (MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8").equals(requestContext.getMediaType().withCharset("UTF-8"))) {
                    //JSON type改请求头成 application/x-www-form-urlencoded 应对之后的参数绑定
                    requestContext.getHeaders().addFirst(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
                }
                if (MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset("UTF-8").equals(requestContext.getMediaType().withCharset("UTF-8"))) {
                    if (requestContext.getEntityStream() != null) {
                        map = MAPPER.readValue(requestContext.getEntityStream(), Map.class);
                        chain.doFilter(new FilterBean(path, map, extension));
                        //回写filter扩展的数据
                        map.putAll(extension);
                        bindFormParameters(request, map);
                        requestContext.setProperty("request_body", map);
                    } else {
                        requestContext.abortWith(ResponseFactory.error(MessageEnum.UN_SUPPORTED_REQUEST));
                    }
                } else {
                    requestContext.abortWith(ResponseFactory.error(Response.Status.UNSUPPORTED_MEDIA_TYPE));
                }
                break;
            default:
                requestContext.abortWith(ResponseFactory.error(Response.Status.METHOD_NOT_ALLOWED));
        }
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        LOGGER.warn("序列化2次");
        Map<String, Object> map = (Map) context.getProperty("request_body");
        try {
            Class<?> clazz = context.getType();
            Object object = clazz.newInstance();
            BeanUtils.populate(clazz, object, map);
            return object;
        } catch (Throwable e) {
            LOGGER.error("Map to Bean convert Exception", e);
            throw new WebApplicationException(ResponseFactory.error(Response.Status.INTERNAL_SERVER_ERROR));
        }
    }

    private void bindFormParameters(NettyHttpRequest request, Map<String, Object> map) {
        MultivaluedMap<String, String> formParameters = request.getDecodedFormParameters();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                formParameters.putSingle(entry.getKey(), (String) value);
            } else if (value instanceof List) {
                formParameters.put(entry.getKey(), (List<String>) value);
            } else {
                LOGGER.warn("Unsupported Request Parameter " + entry.getKey());
            }
        }
    }
}
