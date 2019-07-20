package com.roiland.platform.lang;

import java.lang.annotation.Annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2015/12/10.
 */
public class AnnotationUtils {

    /**
     * 如果解析类与注解类型一致，返回<code>true</code>，否则返回<code>false</code>
     *
     * @param <T> Annotation泛型
     * @param annotation 注解
     * @param source 解析类
     * @return true：一致，false：不一致
     */
    public static <T extends Annotation> boolean isTargetAnnotation(Class<T> annotation, Class<?> source) {
        return source.isAnnotationPresent(annotation);
    }

    /**
     * 返回解析类的注解对象，如果解析类型不一致，返回null
     *
     * @param <T> Annotation泛型
     * @param annotation 注解
     * @param source 解析类
     * @return 注解对象
     */
    public static <T extends Annotation> T explain(Class<T> annotation, Class<?> source) {
        if (isTargetAnnotation(annotation, source)) {
            return source.getAnnotation(annotation);
        }
        return null;
    }

    /**
     * 返回解析类字段的注解对象，如果解析类型不一致，返回null
     *
     * @param <T> Annotation泛型
     * @param annotation 注解
     * @param source 解析类
     * @param fieldName 待处理字段名称
     * @return 目标泛型对象
     */
    public static <T extends Annotation> T explain(Class<T> annotation, Class<?> source, String fieldName) {
        try {
            final Field field = source.getDeclaredField(fieldName);
            if (field != null) {
                if (field.isAnnotationPresent(annotation)) {
                    return field.getAnnotation(annotation);
                }
            }
        } catch (NoSuchFieldException | SecurityException ex) {

        }
        return null;
    }

    /**
     * 返回解析类的注解属性值，如果注解不存在，则返回Null
     *
     * @param <T> Annotation泛型
     * @param <U> Object 泛型
     * @param annotation 注解
     * @param source 解析类
     * @param property 属性名称
     * @param returnType 返回类型
     * @return 注解属性值
     */
    public static <T extends Annotation, U extends Object> U findByAnnotationProperty(Class<T> annotation, Class<?> source, String property, Class<U> returnType) {
        T object = explain(annotation, source);
        if (object != null) {
            try {
                Method method = object.getClass().getMethod(property);
                return (U) method.invoke(object);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {

            }
        }
        return null;
    }

    /**
     * 比对注解属性值与期望值是否一致，如果一致，返回true，否则false
     *
     * @param <T> Annotation泛型
     * @param <U> Object 泛型
     * @param annotation 注解
     * @param source 解析类
     * @param property 属性名称
     * @param expect 期望值
     * @return true：一致，false：不一致
     */
    public static <T extends Annotation, U extends Object> boolean check(Class<T> annotation, Class<?> source, String property, U expect) {
        if (expect == null) {
            return false;
        }

        U result = (U) findByAnnotationProperty(annotation, source, property, expect.getClass());
        return expect.equals(result);
    }

    /**
     * 返回解析类中第一个包含属性名称及属性值的字段
     *
     * @param <T> Annotation泛型
     * @param <U> Object 泛型
     * @param annotation 注解
     * @param source 解析类
     * @param property 属性名称
     * @param value 属性值
     * @return 字段
     */
    public static <T extends Annotation, U extends Object> Field findFieldByAnnotationProperty(Class<T> annotation, Class<?> source, String property, U value) {
        if (value == null) {
            return null;
        }
        
        final Field[] fields = source.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return null;
        }

        for (Field field : fields) {
            Object object = findByField(field, annotation, property, value.getClass());
            if (value.equals(object)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 回解析类中第一个包含属性名称及属性值的字段名称
     *
     * @param <T> Annotation泛型
     * @param <U> Object 泛型
     * @param annotation 注解
     * @param source 解析类
     * @param property 属性名称
     * @param value 属性值
     * @return 字段名称
     */
    public static <T extends Annotation, U extends Object> String findFieldNameByAnnotationProperty(Class<T> annotation, Class<?> source, String property, U value) {
        Field field = findFieldByAnnotationProperty(annotation, source, property, value);
        if (field == null) {
            return null;
        }
        return field.getName();
    }

    /**
     * 获取当前待处理类中，包含目标泛型类字段
     *
     * @param <T> Annotation泛型
     * @param annotation 注解
     * @param source 解析类
     * @return 目标泛型类
     */
    public static <T extends Annotation> Set<Field> findFields(Class<T> annotation, Class<?> source) {
        final Field[] fields = source.getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return new HashSet<>();
        }

        final Set<Field> result = new HashSet<>(fields.length);
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                result.add(field);
            }
        }
        return result;
    }

    /**
     * 返回字段名称与注解属性值Map
     *
     * @param <T> Annotation泛型
     * @param <U> Object 泛型
     * @param annotation 注解
     * @param source 解析类
     * @param property 属性名称
     * @param returnType 返回值类型
     * @return 字段名称与返回值Map
     */
    public static <T extends Annotation, U extends Object> Map<String, U> findAsMap(Class<T> annotation, Class<?> source, String property, Class<U> returnType) {
        Set<Field> fields = findFields(annotation, source);

        final Map<String, U> result = new HashMap<>(fields.size());
        for (Field field : fields) {
            U temp = findByField(field, annotation, property, returnType);
            result.put(field.getName(), temp);
        }
        return result;
    }

    /**
     * 返回注解属性值与字段名称Map
     *
     * @param <T> Annotation泛型
     * @param <U> Object 泛型
     * @param annotation 注解
     * @param source 解析类
     * @param property 属性名称
     * @param returnType 返回值类型
     * @return 返回值与字段名称Map
     */
    public static <T extends Annotation, U extends Object> Map<U, String> findAsReverseMap(Class<T> annotation, Class<?> source, String property, Class<U> returnType) {
        Set<Field> fields = findFields(annotation, source);

        final Map<U, String> result = new HashMap<>(fields.size());

        for (Field field : fields) {
            U value = findByField(field, annotation, property, returnType);
            result.put(value, field.getName());
        }
        return result;
    }

    private static <T extends Annotation, U extends Object> U findByField(Field field, Class<T> annotation, String property, Class<U> returnType) {
        if (field.isAnnotationPresent(annotation)) {
            try {
                T object = field.getAnnotation(annotation);
                Method method = object.getClass().getMethod(property);
                return (U) method.invoke(object);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                
            }
        }
        return null;
    }
}
