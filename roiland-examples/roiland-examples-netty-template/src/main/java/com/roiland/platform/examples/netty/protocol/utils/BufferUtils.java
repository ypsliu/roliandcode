package com.roiland.platform.examples.netty.protocol.utils;

import com.roiland.platform.lang.ByteUtils;

import java.nio.ByteBuffer;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/9
 */
public class BufferUtils {

    public static String readBufferAsHex(ByteBuffer buffer, Integer length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return ByteUtils.bytesToHex(bytes);
    }
}
