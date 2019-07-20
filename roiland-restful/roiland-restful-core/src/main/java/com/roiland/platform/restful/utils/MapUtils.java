package com.roiland.platform.restful.utils;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
public final  class MapUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setDeserializationConfig(MAPPER.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
    }

    public static Map<String, Object> toMap(InputStream inputStream) throws IOException {
        if (inputStream.available() == 0) {
            return Collections.EMPTY_MAP;
        }

        final byte[] bytes = new byte[inputStream.available()];
        try {
            inputStream.read(bytes);
        } finally {
            inputStream.close();
        }
        return MAPPER.readValue(bytes, Map.class);
    }

    public static String toString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {

        }
        return "";
    }

    public static Object fromStream(Map<String, Object> map, Class<?> clazz) {
        return MAPPER.convertValue(map, clazz);
    }

    /**
     * 兼容jquery get请求，在发送post请求时与jquery的get请求取得参数保持一致
     *
     * @param params Post请求参数
     */
    public static void flatten(MultivaluedMap<String, String> params, Map<String, Object> original) {
        flatten(false, params, StringUtils.EMPTY, original);
    }

    /**
     *  对Map对象进行扁平化处理
     * @param surround
     * @param key
     * @param target
     * @param original
     */
    private static void flatten(boolean surround, MultivaluedMap<String, String> target, String key, Map<String, Object> original) {
        for (Map.Entry<String, Object> entry : original.entrySet()) {
            String tmpKey = entry.getKey();
            Object tmpValue = entry.getValue();
            if (tmpValue == null) {
                target.add(key + surround(surround, tmpKey), null);
            } else if (tmpValue instanceof List) {
                flatten(key + surround(surround, tmpKey), target, (List) tmpValue);
            } else if (tmpValue instanceof Map) {
                flatten(true, target, key + surround(surround, tmpKey), (Map) tmpValue);
            } else {
                target.add(key + surround(surround, tmpKey), String.valueOf(tmpValue));
            }
        }
    }

    /**
     * 对列表进行扁平化处理
     * @param key
     * @param target
     * @param original
     */
    private static void flatten(String key, MultivaluedMap<String, String> target, List<Object> original) {
        for (int i = 0; i < original.size(); i++) {
            Object obj = original.get(i);
            if (obj == null) {
                target.add(key + "[" + i + "]", null);
            } else if (obj instanceof List) {
                flatten(key + "[" + i + "]", target, (List) obj);
            } else if (obj instanceof Map) {
                flatten(true, target, key + "[" + i + "]", (Map) obj);
            } else {
                if (key.charAt(key.length() - 1) != ']') {
                    target.add( key, String.valueOf(obj));
                }
                target.add(key + "[]", String.valueOf(obj));
            }
        }
    }

    private static String surround(boolean surround, String key) {
        return surround ? "[" + key + "]" : key;
    }
}
