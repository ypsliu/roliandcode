package com.roiland.platform.template.core.support;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roiland.platform.template.core.ast.Resource;
import com.roiland.platform.template.core.bean.CodeGenResourceBean;
import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.tree.CodeGenVisitor;
import com.roiland.platform.template.core.tree.TemplateParserVisitor;
import ognl.OgnlException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/13
 */
@SuppressWarnings("ALL")
public class TemplateSupport {

    private final ObjectMapper mapper = MapperSupport.getMapper();

    private static final SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Map<String, Object> context = new HashMap<>();

    public TemplateSupport() {
        this.mapper.setDateFormat(defaultFormat);
    }

    public void putContext(Map<String, Object> context) {
        this.context.putAll(context);
    }

    public void putContext(String key, Object value) {
        this.context.put(key, value);
    }

    public void setDateFormat(String pattern) {
        this.mapper.setDateFormat(new SimpleDateFormat(pattern));
    }

    public ResourceBean bind(String strParam, ResourceBean resourceBean) throws IOException {
        Map<String, Object> param = mapper.readValue(strParam, Map.class);
        return bind(param, resourceBean);
    }

    public ResourceBean bind(byte[] byteParam, ResourceBean resourceBean) throws IOException {
        Map<String, Object> param = mapper.readValue(byteParam, Map.class);
        return bind(param, resourceBean);
    }

    public ResourceBean bind(InputStream streamParam, ResourceBean resourceBean) throws IOException {
        Map<String, Object> param = mapper.readValue(streamParam, Map.class);
        return bind(param, resourceBean);
    }

    public ResourceBean bind(File fileParam, ResourceBean resourceBean) throws IOException {
        return bind(new BufferedInputStream(new FileInputStream(fileParam)), resourceBean);
    }

    public ResourceBean bind(Reader readerParam, ResourceBean resourceBean) throws IOException {
        Map<String, Object> param = mapper.readValue(readerParam, Map.class);
        return bind(param, resourceBean);
    }

    public ResourceBean bind(Map<String, Object> param, ResourceBean resourceBean) {
        ResourceBean result = resourceBean.clone();
        List<Object> paramValues = result.getParamValue();
        List<JavaType> paramJavaTypes = result.getParamJavaType();
        List<Object> newParamValues = new ArrayList<>(paramValues.size());
        try {
            OgnlSupport ognl = new OgnlSupport(context);
            for (int i = 0; i < paramValues.size(); i++) {
                Object paramValue = paramValues.get(i);
                JavaType javaType = paramJavaTypes.get(i);
                Object obj = ognl.bind(paramValue, param);
                newParamValues.add(mapper.convertValue(obj, javaType));
            }
            result.setParamValue(newParamValues);
        } catch (OgnlException e) {
            throw new UnsupportedOperationException(e);
        }
        return result;
    }

    /**
     * 解析表达式预编译，提升执行速度
     *
     * @param resourceBean
     */
    public void preCompile(ResourceBean resourceBean) {
        try {
            List<Object> paramValues = resourceBean.getParamValue();
            List<Object> compiledParamValues = new ArrayList<>(paramValues.size());
            OgnlSupport ognl = new OgnlSupport(context);
            for (Object paramValue : paramValues) {
                compiledParamValues.add(ognl.compile(paramValue));
            }
            resourceBean.setParamValue(compiledParamValues);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Template Pre-Compile Exception", e);
        }
    }

    public ResourceBean parseTemplate(String strTemplate) throws IOException {
        Resource template = mapper.readValue(strTemplate, Resource.class);
        return parseTemplate(template);
    }

    public ResourceBean parseTemplate(byte[] byteTemplate) throws IOException {
        Resource template = mapper.readValue(byteTemplate, Resource.class);
        return parseTemplate(template);
    }

    public ResourceBean parseTemplate(InputStream streamTemplate) throws IOException {
        Resource template = mapper.readValue(streamTemplate, Resource.class);
        return parseTemplate(template);
    }

    public ResourceBean parseTemplate(Reader readerTemplate) throws IOException {
        Resource template = mapper.readValue(readerTemplate, Resource.class);
        return parseTemplate(template);
    }

    public ResourceBean parseTemplate(File template) throws IOException {
        return parseTemplate(new BufferedInputStream(new FileInputStream(template)));
    }

    public ResourceBean parseTemplate(Resource template) throws IOException {
        TemplateParserVisitor scanner = new TemplateParserVisitor();
        template.accept(scanner);
        ResourceBean resourceBean = scanner.getResource();
//        preCompile(resourceBean);
        return resourceBean;
    }

    public List<CodeGenResourceBean> codeGenResourceBean(String... strTemplates) throws IOException {
        Resource[] resource = null;
        if (strTemplates != null) {
            resource = new Resource[strTemplates.length];
            for (int i = 0; i < strTemplates.length; i++) {
                resource[i] = mapper.readValue(strTemplates[i], Resource.class);
            }
        }
        return codeGenResourceBean(resource);
    }

    public List<CodeGenResourceBean> codeGenResourceBean(byte[]... byteTemplates) throws IOException {
        Resource[] resource = null;
        if (byteTemplates != null) {
            resource = new Resource[byteTemplates.length];
            for (int i = 0; i < byteTemplates.length; i++) {
                resource[i] = mapper.readValue(byteTemplates[i], Resource.class);
            }
        }
        return codeGenResourceBean(resource);
    }

    public List<CodeGenResourceBean> codeGenResourceBean(InputStream... streamsTemplates) throws IOException {
        Resource[] resource = null;
        if (streamsTemplates != null) {
            resource = new Resource[streamsTemplates.length];
            for (int i = 0; i < streamsTemplates.length; i++) {
                resource[i] = mapper.readValue(streamsTemplates[i], Resource.class);
            }
        }
        return codeGenResourceBean(resource);
    }

    public List<CodeGenResourceBean> codeGenResourceBean(Reader... readerTemplates) throws IOException {
        Resource[] resource = null;
        if (readerTemplates != null) {
            resource = new Resource[readerTemplates.length];
            for (int i = 0; i < readerTemplates.length; i++) {
                resource[i] = mapper.readValue(readerTemplates[i], Resource.class);
            }
        }
        return codeGenResourceBean(resource);
    }

    public List<CodeGenResourceBean> codeGenResourceBean(File... fileTemplates) throws IOException {
        InputStream[] inputStreams = null;
        if (fileTemplates != null) {
            inputStreams = new InputStream[fileTemplates.length];
            for (int i = 0; i < fileTemplates.length; i++) {
                inputStreams[i] = new BufferedInputStream(new FileInputStream(fileTemplates[i]));
            }
        }
        return codeGenResourceBean(inputStreams);
    }

    public List<CodeGenResourceBean> codeGenResourceBean(Resource... templates) {
        List<CodeGenResourceBean> list = new ArrayList<>();
        if (templates != null) {
            for (Resource template : templates) {
                CodeGenVisitor scanner = new CodeGenVisitor();
                template.accept(scanner);
                list.add(scanner.getCodeGenResource());
            }
        }
        return list;
    }
}
