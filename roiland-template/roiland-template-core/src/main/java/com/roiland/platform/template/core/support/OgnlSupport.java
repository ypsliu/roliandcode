package com.roiland.platform.template.core.support;

import com.roiland.platform.lang.*;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/6
 */
public class OgnlSupport {

    private final OgnlContext context = new OgnlContext();

    public OgnlSupport(Map<String,Object> context){
        //buildIn
        this.context.put("string", new StringUtils());
        this.context.put("random", new RandomUtils());
        this.context.put("ip", new IPUtils());
        this.context.put("num", new NumUtils());
        this.context.put("calendar", new CalendarUtils());
        this.context.put("byte", new ByteUtils());
        this.context.put("char", new CharUtils());
        this.context.putAll(context);
    }

    private Map<String, Object> bind(Map<String, Object> map, Map<String, Object> param) throws OgnlException {
        Map<String, Object> newMap = new LinkedHashMap<>(map.size(),1);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            newMap.put(entry.getKey(), bind(entry.getValue(), param));
        }
        return newMap;
    }

    private List<Object> bind(List<Object> list, Map<String, Object> param) throws OgnlException {
        List<Object> newList = new ArrayList<>(list.size());
        for (Object obj : list) {
            newList.add(bind(obj, param));
        }
        return newList;
    }

    @SuppressWarnings("unchecked")
    public Object bind(Object value, Map<String, Object> param) throws OgnlException {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            String str = ((String) value).trim();
            if (str.startsWith("${") && str.endsWith("}")) {
                String expr = str.substring(2, str.length() - 1);
                Object parseTree = Ognl.parseExpression(expr);
                return Ognl.getValue(parseTree, context, param);
            } else {
                return value;
            }
        } else if (value instanceof List) {
            return bind((List<Object>) value, param);
        } else if (value instanceof Map) {
            return bind((Map<String,Object>) value, param);
        } else if (value instanceof Node){
            //对编译好的表达式求值
            return Ognl.getValue(value,context,param);
        } else{
            return value;
        }
    }

    private Map<String, Object> compile(Map<String, Object> map) throws Exception {
        Map<String, Object> newMap = new LinkedHashMap<>(map.size(),1);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            newMap.put(entry.getKey(), compile(entry.getValue()));
        }
        return newMap;
    }

    private List<Object> compile(List<Object> list) throws Exception {
        List<Object> newList = new ArrayList<>(list.size());
        for (Object obj : list) {
            newList.add(compile(obj));
        }
        return newList;
    }

    @SuppressWarnings("unchecked")
    public Object compile(Object value) throws Exception {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            String str = ((String) value).trim();
            if (str.startsWith("${") && str.endsWith("}")) {
                String expr = str.substring(2, str.length() - 1);
                return Ognl.compileExpression(context,null,expr);
            } else {
                return value;
            }
        } else if (value instanceof List) {
            return compile((List<Object>) value);
        } else if (value instanceof Map) {
            return compile((Map<String, Object>) value);
        } else {
            return value;
        }
    }
}
