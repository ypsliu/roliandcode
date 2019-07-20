package com.roiland.platform.template.core;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/11
 */
public interface Invoker extends Closeable {

    Object invoke(byte[] param,InvokerCallback callback) throws Exception;

    Object invoke(InputStream param,InvokerCallback callback) throws Exception;

    Object invoke(Reader param,InvokerCallback callback) throws Exception;

    Object invoke(String param,InvokerCallback callback) throws Exception;

    Object invoke(File param,InvokerCallback callback) throws Exception;

    Object invoke(Map<String, Object> param,InvokerCallback callback) throws Exception;

    boolean isAsync();

    public static interface InvokerCallback{
        public void callback(Object obj);
    }
}
