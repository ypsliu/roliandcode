package com.roiland.platform.examples.netty.protocol.behaviour.obd.attribute;

import com.roiland.platform.lang.ByteUtils;

import java.nio.ByteBuffer;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/11
 */
public class Length {
    private Boolean isFix = true;
    private Integer len;

    public Length(String length) {
        if (length == null) {
            this.len = 0;
        } else if (length.charAt(0) == '#') {
            this.isFix = false;
            int split1 = length.indexOf('{');
            int split2 = length.indexOf('}');
            this.len = Integer.valueOf(length.substring(split1 + 1, split2));
        } else {
            this.len = Integer.valueOf(length);
        }
    }

    public Integer decode(ByteBuffer buffer) {
        if (isFix) {
            return len;
        } else {
            byte[] bytes = new byte[len];
            buffer.get(bytes);
            return ByteUtils.bytesToUInt(bytes);
        }
    }

    public String toString() {
        return isFix ? String.valueOf(this.len) : "#{" + this.len + "}";
    }
}
