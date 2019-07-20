package com.roiland.platform.dbutils.helper;

import com.roiland.platform.dbutils.DBConn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.skife.jdbi.v2.Handle;

/**
 * @author leon.chen
 * @since 2016/1/12
 */
public abstract class DBTemplate<T> {

    private static final Log LOGGER = LogFactory.getLog(DBTemplate.class);

    public T using(String schema) {
        return using(schema, false);
    }

    public T using(String schema, boolean isReadOnly) {
        Handle handle = DBConn.handle(schema, isReadOnly);
        T result = null;
        if (handle != null) {
            try {
                result = execute(handle);
            } catch (Throwable e) {
                LOGGER.error("database error:", e);
                throw new RuntimeException(e);
            } finally {
                handle.close();
            }
        }
        return result;
    }

    public abstract T execute(Handle handle);
}
