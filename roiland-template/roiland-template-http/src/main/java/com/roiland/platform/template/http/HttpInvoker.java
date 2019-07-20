package com.roiland.platform.template.http;

import com.roiland.platform.signature.RoilandSignautre;
import com.roiland.platform.template.core.Invoker;
import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.support.TemplateSupport;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import static com.roiland.platform.template.core.support.MapperSupport.*;

/**
 * @author leon.chen
 * @since 2016/4/11
 */
@SuppressWarnings("ALL")
public class HttpInvoker extends AbstractHttpInvoker {

    private CloseableHttpClient client;

    private static final RoilandSignautre signature = new RoilandSignautre();

    private HttpInvoker(Map<String, Object> globalParam, ResourceBean resourceBean) {
        super(globalParam, resourceBean);
    }

    public static HttpInvokerBuilder create(String template) throws IOException {
        return new HttpInvokerBuilder().template(template);
    }

    public static HttpInvokerBuilder create(byte[] template) throws IOException {
        return new HttpInvokerBuilder().template(template);
    }

    public static HttpInvokerBuilder create(InputStream template) throws IOException {
        return new HttpInvokerBuilder().template(template);
    }

    public static HttpInvokerBuilder create(Reader template) throws IOException {
        return new HttpInvokerBuilder().template(template);
    }

    public static HttpInvokerBuilder create(File template) throws IOException {
        return new HttpInvokerBuilder().template(template);
    }

    public static HttpInvokerBuilder create(ResourceBean template) throws IOException {
        return new HttpInvokerBuilder().template(template);
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    public static class HttpInvokerBuilder {

        private HttpInvokerBuilder() {

        }

        private RequestConfig.Builder configBuilder = RequestConfig.custom().setConnectTimeout(10000);

        private HttpClientBuilder clientBuilder = HttpClients.custom().setMaxConnTotal(1000).setMaxConnPerRoute(500);

        private Map<String, Object> globalParam;

        private ResourceBean resourceBean;

        private TemplateSupport templateSupport = new TemplateSupport();

        public HttpInvokerBuilder context(Map<String, Object> context) {
            this.templateSupport.putContext(context);
            return this;
        }

        public HttpInvokerBuilder context(TemplateSupport templateSupport) {
            this.templateSupport = templateSupport;
            return this;
        }

        public HttpInvokerBuilder context(String key, Object value) {
            this.templateSupport.putContext(key, value);
            return this;
        }

        public HttpInvokerBuilder dateFormat(String pattern) {
            this.templateSupport.setDateFormat(pattern);
            return this;
        }

        public HttpInvokerBuilder connectTimeout(int timeMillis) {
            configBuilder.setConnectTimeout(timeMillis);
            return this;
        }

        public HttpInvokerBuilder maxConnTotal(int conn) {
            clientBuilder.setMaxConnTotal(conn);
            return this;
        }

        public HttpInvokerBuilder maxConnPerRoute(int conn) {
            clientBuilder.setMaxConnPerRoute(conn);
            return this;
        }

        private HttpInvokerBuilder template(String template) throws IOException {
            this.resourceBean = templateSupport.parseTemplate(template);
            return this;
        }

        private HttpInvokerBuilder template(byte[] template) throws IOException {
            this.resourceBean = templateSupport.parseTemplate(template);
            return this;
        }

        private HttpInvokerBuilder template(InputStream template) throws IOException {
            this.resourceBean = templateSupport.parseTemplate(template);
            return this;
        }

        private HttpInvokerBuilder template(Reader template) throws IOException {
            this.resourceBean = templateSupport.parseTemplate(template);
            return this;
        }

        private HttpInvokerBuilder template(ResourceBean template) throws IOException {
            this.resourceBean = template;
            return this;
        }

        private HttpInvokerBuilder template(File template) throws IOException {
            return template(new BufferedInputStream(new FileInputStream(template)));
        }

        public HttpInvokerBuilder globalParam(String param) throws IOException {
            this.globalParam = readValue(param, Map.class);
            return this;
        }

        public HttpInvokerBuilder globalParam(byte[] param) throws IOException {
            this.globalParam = readValue(param, Map.class);
            return this;
        }

        public HttpInvokerBuilder globalParam(InputStream param) throws IOException {
            this.globalParam = readValue(param, Map.class);
            return this;
        }

        public HttpInvokerBuilder globalParam(Reader param) throws IOException {
            this.globalParam = readValue(param, Map.class);
            return this;
        }

        public HttpInvokerBuilder globalParam(File param) throws IOException {
            return globalParam(new BufferedInputStream(new FileInputStream(param)));
        }

        public HttpInvokerBuilder globalParam(Map<String, Object> param) throws IOException {
            this.globalParam = param;
            return this;
        }

        public Invoker build(boolean preCompile) {
            if (globalParam == null || resourceBean == null) {
                throw new IllegalArgumentException("globalParam is null or resourceBean is null");
            }
            //预编译模板
            if (preCompile) templateSupport.preCompile(resourceBean);
            HttpInvoker invoker = new HttpInvoker(globalParam, resourceBean) {
                protected TemplateSupport getTemplateSupport() {
                    return templateSupport;
                }
            };
            invoker.client = clientBuilder.setDefaultRequestConfig(configBuilder.build()).build();
            return invoker;
        }
    }

    @Override
    public Object handle(Map<String, Object> object, ResourceBean bean,InvokerCallback callback) throws IOException {
        String method = getMethod(bean);
        Object obj;
        if (method.equalsIgnoreCase("GET")) {
            obj = handleGet(object, bean);
        } else if (method.equalsIgnoreCase("POST")) {
            obj = handlePost(object, bean);
        } else if (method.equalsIgnoreCase("PUT")) {
            obj = handlePut(object, bean);
        } else if (method.equalsIgnoreCase("DELETE")) {
            obj = handleDelete(object, bean);
        } else {
            throw new UnsupportedOperationException("Unsupported method: " + method);
        }
        if(callback!=null){
            callback.callback(obj);
        }
        return obj;
    }

    private Object handlePost(Map<String, Object> object, ResourceBean bean) throws IOException {
        String url = getHost(object) + getResource(bean);
        Map<String, Object> data = getParam(bean);
        Map<String, String> headers = getHeader(bean);
        String secretKey = getSecretKey(bean);
        String contentType = getContentType(object);
        return post(url, data, headers, contentType, secretKey);
    }


    private Object handlePut(Map<String, Object> object, ResourceBean bean) throws IOException {
        String url = getHost(object) + getResource(bean);
        Map<String, Object> data = getParam(bean);
        Map<String, String> headers = getHeader(bean);
        String secretKey = getSecretKey(bean);
        String contentType = getContentType(object);
        return put(url, data, headers, contentType, secretKey);
    }

    private Object handleGet(Map<String, Object> object, ResourceBean bean) throws IOException {
        String url = getHost(object) + getResource(bean);
        Map<String, Object> data = getParam(bean);
        Map<String, Object> convertedData = convertValue(data, Map.class);
        Map<String, String> headers = getHeader(bean);
        String secretKey = getSecretKey(bean);
        return get(url, convertedData, headers, secretKey);
    }

    private Object handleDelete(Map<String, Object> object, ResourceBean bean) throws IOException {
        String url = getHost(object) + getResource(bean);
        Map<String, Object> data = getParam(bean);
        Map<String, Object> convertedData = convertValue(data, Map.class);
        Map<String, String> headers = getHeader(bean);
        String secretKey = getSecretKey(bean);
        return delete(url, convertedData, headers, secretKey);
    }

    private String post(String url, Map<String, Object> data, Map<String, String> headers, String contentType, String secretKey) throws IOException {
        HttpPost post = new HttpPost(url);
        String body = writeValueAsString(data);
        post.setEntity(new StringEntity(body, ContentType.create(contentType)));
        if (secretKey != null) {
            headers = signature.signPUT(url, body, headers, secretKey);
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try (CloseableHttpResponse response = client.execute(post)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new UnsupportedOperationException("Request Failed.Status:" + response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            return result;
        }
    }

    private String get(String url, Map<String, Object> data, Map<String, String> headers, String secretKey) throws IOException {
        String newUrl = getUrl(url, data == null ? Collections.EMPTY_MAP : data);
        HttpGet get = new HttpGet(newUrl);
        if (secretKey != null) {
            headers = signature.signGET(newUrl, headers, secretKey);
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                get.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try (CloseableHttpResponse response = client.execute(get)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new UnsupportedOperationException("Request Failed.Status:" + response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            return result;
        }
    }

    private String delete(String url, Map<String, Object> data, Map<String, String> headers, String secretKey) throws IOException {
        String newUrl = getUrl(url, data == null ? new HashMap<String, Object>() : data);
        HttpDelete get = new HttpDelete(newUrl);
        if (secretKey != null) {
            headers = signature.signDELETE(newUrl, headers, secretKey);
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                get.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try (CloseableHttpResponse response = client.execute(get)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new UnsupportedOperationException("Request Failed.Status:" + response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            return result;
        }
    }

    private String put(String url, Map<String, Object> data, Map<String, String> headers, String contentType, String secretKey) throws IOException {
        HttpPut put = new HttpPut(url);
        String body = writeValueAsString(data);
        put.setEntity(new StringEntity(body, ContentType.create(contentType)));
        if (secretKey != null) {
            headers = signature.signPUT(url, body, headers, secretKey);
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                put.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try (CloseableHttpResponse response = client.execute(put)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new UnsupportedOperationException("Request Failed.Status:" + response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            return result;
        }
    }

    private static String getUrl(String url, Map<String, Object> data) {
        try {
            List<KeyValue> list = new ArrayList<>();
            flatten(list, data);
            StringBuilder builder = new StringBuilder();
            if (list != null && list.size() > 0) {
                builder.append("?");
                Iterator<KeyValue> it = list.iterator();
                while (it.hasNext()) {
                    KeyValue entry = it.next();
                    builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    builder.append("=");
                    if (entry.getValue() != null) {
                        builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    }
                    if (it.hasNext()) {
                        builder.append("&");
                    }
                }
                return url + builder.toString();
            } else {
                return url;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static void flatten(List<KeyValue> target, Map<String, Object> original) {
        mapFlatten(false, "", target, original);
    }

    private static void mapFlatten(boolean surround, String str, List<KeyValue> target, Map<String, Object> original) {
        for (Map.Entry<String, Object> entry : original.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                target.add(new KeyValue(str + surround(surround, key), null));
            } else if (value instanceof Collection) {
                listFlatten(str + surround(surround, key), target, (Collection) value);
            } else if (value instanceof Map) {
                mapFlatten(true, str + surround(surround, key), target, (Map) value);
            } else {
                target.add(new KeyValue(str + surround(surround, key), String.valueOf(value)));
            }
        }
    }

    private static void listFlatten(String key, List<KeyValue> target, Collection<Object> original) {
        int i = 0;
        for (Object obj : original) {
            if (obj == null) {
                target.add(new KeyValue(key + "[" + i + "]", null));
            } else if (obj instanceof Collection) {
                listFlatten(key + "[" + i + "]", target, (Collection) obj);
            } else if (obj instanceof Map) {
                mapFlatten(true, key + "[" + i + "]", target, (Map) obj);
            } else {
                target.add(new KeyValue(key + "[]", String.valueOf(obj)));
            }
            i++;
        }
    }

    private static String surround(boolean surround, String key) {
        return surround ? "[" + key + "]" : key;
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    private static class KeyValue {
        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
