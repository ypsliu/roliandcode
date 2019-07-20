package com.roiland.platform.rest.util;

import com.roiland.platform.annotation.impl.beanutils.BeanAnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 2015/12/10.
 */
public class BeanUtils {

    private static final Map<Class<?>, Map<String, String>> CACHE = new ConcurrentHashMap<>();

    public static void populate(Class<?> clazz, Object object, Map<?, ?> properties) throws InvocationTargetException, IllegalAccessException {
        if (object == null || properties == null || properties.size() == 0) {
            return;
        }

        Map<String, String> propertyMapper = CACHE.get(clazz);
        if (propertyMapper == null) {
            propertyMapper = BeanAnnotationUtils.getAnnotationPropertyMap(clazz);
            CACHE.put(clazz, propertyMapper);
        }
        for (Map.Entry<?, ?> entry : properties.entrySet()) {
            String name = (String) entry.getKey();
            if (name == null) {
                continue;
            }
            String mappedName = propertyMapper.get(name);
            org.apache.commons.beanutils.BeanUtils.setProperty(object, mappedName == null ? name : mappedName, entry.getValue());
        }
    }

}
