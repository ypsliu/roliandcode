package com.roiland.platform.spi;

import com.roiland.platform.lang.AnnotationUtils;
import com.roiland.platform.spi.annotation.Extension;
import com.roiland.platform.spi.annotation.SPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;

/**
 * SPI loader
 * {@code
 * Map<String,? extends IFilter> providers = RoilandServiceLoader.getProviders("filter",IFilter.class,this.getClass().getClassLoader());
 * }
 *
 * @author leon.chen
 * @since 2016/3/15
 */
public class RoilandServiceLoader {

    private RoilandServiceLoader() {

    }

    private static final String PREFIX = "META-INF/roiland/";

    /**
     *
     * @param extensionName {@code @Extension("name")}
     * @param service SPI的接口Class
     * @param loader 类加载器
     * @param <T> 接口类型
     * @return Map<String,Class></> key代表short name. value代表对应的接口实现class
     */
    public static <T> Map<String, Class<? extends T>> getProviders(String extensionName, Class<T> service, ClassLoader loader) {
        if (!AnnotationUtils.isTargetAnnotation(SPI.class, service)) {
            throw new ServiceConfigurationError(service.getName() + " must have @SPI annotation. ");
        }

        final String fullName = PREFIX + service.getName();
        Enumeration<URL> configs;

        try {
            if (loader == null) {
                configs = ClassLoader.getSystemResources(fullName);
            } else {
                configs = loader.getResources(fullName);
            }
        } catch (IOException e) {
            throw new ServiceConfigurationError(service.getName() + ": fail to load " + fullName, e);
        }
        final Map<String, Class<? extends T>> map = new HashMap<>();
        while (configs.hasMoreElements()) {
            readProviders(service, configs.nextElement(), loader, extensionName, map);
        }
        return map;
    }

    private static <T> void readProviders(Class<T> service, URL url, ClassLoader loader, String extensionName, Map<String, Class<? extends T>> map) {
        InputStream in = null;
        BufferedReader r = null;
        try {
            in = url.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line;
            while ((line = r.readLine()) != null) {
                // 去除#之后的无用注释
                final int index = line.indexOf('#');
                if (index >= 0) {
                    line = line.substring(0, index);
                }
                if (line.trim().length() != 0) {
                    Object[] ary = parseLine(service, line, loader);
                    String key = (String) ary[0];
                    Class<? extends T> value = (Class<? extends T>) ary[1];
                    if (!AnnotationUtils.isTargetAnnotation(Extension.class, value)) {
                        throw new ServiceConfigurationError(value.getName() + " must have @Extension(\"" + extensionName + "\") annotation ");
                    }
                    if (!AnnotationUtils.check(Extension.class, value, "value", extensionName)) {
                        continue;
                    }
                    if (map.containsKey(key) || map.containsValue(value)) {
                        throw new ServiceConfigurationError(service.getName() + ": Duplicate prefix short name or suffix class name. Short name:" + key + ", Class name:" + value.getName());
                    }
                    map.put(key, value);
                }
            }
        } catch (IOException e) {
            throw new ServiceConfigurationError(service.getName() + ": Error reading configuration file", e);
        } finally {
            try {
                if (r != null) r.close();
                if (in != null) in.close();
            } catch (IOException e) {
                throw new ServiceConfigurationError(service.getName() + ": Error closing configuration file", e);
            }
        }
    }

    private static <T> Object[] parseLine(Class<T> service, String line, ClassLoader loader) {

        final String[] ary = line.split("=");
        if (ary == null || ary.length != 2 || ary[0] == null || ary[0].length() == 0 || ary[1] == null || ary[1].length() == 0) {
            throw new ServiceConfigurationError(service.getName() + ": Error reading " + line);
        }
        final String shortName = ary[0] == null ? null : ary[0].trim();
        final String strClass = ary[1] == null ? null : ary[1].trim();
        try {
            Class<?> provider = Class.forName(strClass, false, loader);
            if (!service.isAssignableFrom(provider)) {
                throw new ServiceConfigurationError(service.getName() + ": Provider " + provider + " not a subtype");
            }
            return new Object[]{shortName, (Class<? extends T>) provider};
        } catch (ClassNotFoundException e) {
            //bug fix
            if (loader.getParent() != null) {
                return parseLine(service, line, loader.getParent());
            } else {
                throw new ServiceConfigurationError(service.getName() + ": Provider " + strClass + " not found");
            }
        }
    }
}
