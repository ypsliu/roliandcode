package com.roiland.platform.examples.netty.protocol.utils;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class ObjectUtils {

    public static <T extends Object> T toObject(Object object, Class<T> type) {
        if (object == null) {
            return null;
        } else {
            return (T)object;
        }
    }
}
