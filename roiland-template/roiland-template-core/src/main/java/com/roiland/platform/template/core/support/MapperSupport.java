package com.roiland.platform.template.core.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author leon.chen
 * @since 2016/4/13
 */
public class MapperSupport {

    private static final ObjectMapper mapper = getMapper();

    public static ObjectMapper getMapper() {
        return new ObjectMapper();
    }

    public static <T> T readValue(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public static <T> T readValue(byte[] json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public static <T> T readValue(InputStream json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public static <T> T readValue(Reader json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static byte[] writeValueAsBytes(Object object) throws JsonProcessingException {
        return mapper.writeValueAsBytes(object);
    }

    public static <T> T convertValue(Object object, Class<T> clazz) {
        return mapper.convertValue(object, clazz);
    }

    public static <T> T convertValue(Object object, JavaType javaType) {
        return mapper.convertValue(object, javaType);
    }

    public static <T> T convertValue(Object object, TypeReference<?> typeReference) {
        return mapper.convertValue(object, typeReference);
    }

    public static <T> T readValue(String json, JavaType javaType) throws IOException {
        return mapper.readValue(json, javaType);
    }

    public static <T> T readValue(byte[] json, JavaType javaType) throws IOException {
        return mapper.readValue(json, javaType);
    }

    public static <T> T readValue(InputStream json, JavaType javaType) throws IOException {
        return mapper.readValue(json, javaType);
    }

    public static <T> T readValue(Reader json, JavaType javaType) throws IOException {
        return mapper.readValue(json, javaType);
    }

    public static <T> T readValue(String json, TypeReference typeReference) throws IOException {
        return mapper.readValue(json, typeReference);
    }

    public static <T> T readValue(byte[] json, TypeReference typeReference) throws IOException {
        return mapper.readValue(json, typeReference);
    }

    public static <T> T readValue(InputStream json, TypeReference typeReference) throws IOException {
        return mapper.readValue(json, typeReference);
    }

    public static <T> T readValue(Reader json, TypeReference typeReference) throws IOException {
        return mapper.readValue(json, typeReference);
    }
}
