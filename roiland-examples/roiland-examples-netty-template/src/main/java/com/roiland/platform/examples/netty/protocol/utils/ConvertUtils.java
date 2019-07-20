package com.roiland.platform.examples.netty.protocol.utils;

import com.roiland.platform.lang.ByteUtils;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/10
 */
public class ConvertUtils {

    private enum TYPE {_int, _uint, _long, _float, _double, _char, _time, _hex}

    public static TYPE findType(String type) {
        try {
            return TYPE.valueOf("_" + type.toLowerCase());
        } catch (Exception e) {
            return null;
        }
    }

    public static Object toObject(byte[] bytes, String type, Integer rate) {
        TYPE _type = TYPE.valueOf("_" + type.toLowerCase());
        Object result = null;
        switch (_type) {
            case _int:
                result = ByteUtils.bytesToInt(bytes);
                break;
            case _uint:
                result = ByteUtils.bytesToUInt(bytes);
                break;
            case _long:
                result = ByteUtils.bytesToLong(bytes);
                break;
            case _float:
                if (rate == null || rate == 0) {
                    result = Integer.valueOf(ByteUtils.bytesToInt(bytes)).floatValue();
                } else {
                    result = Integer.valueOf(ByteUtils.bytesToInt(bytes)).floatValue() / rate;
                }
                break;
            case _double:
                if (rate == null || rate == 0) {
                    result = Integer.valueOf(ByteUtils.bytesToInt(bytes)).doubleValue();
                } else {
                    result = Integer.valueOf(ByteUtils.bytesToInt(bytes)).doubleValue() / rate;
                }
                break;
            case _char:
                result = new String(bytes);
                break;
            case _time:
                result = ByteUtils.bytesToStrDate(bytes);
                break;
            case _hex:
                result = ByteUtils.bytesToHex(bytes);
        }
        return result;
    }

    public static Object toObject(String value, String type, Integer rate) {
        TYPE _type = TYPE.valueOf("_" + type.toLowerCase());
        Object result = null;
        switch (_type) {
            case _int:
                result = Integer.valueOf(value);
                break;
            case _uint:
                result = Integer.valueOf(value);
                break;
            case _long:
                result = Long.valueOf(value);
                break;
            case _float:
                result = Float.valueOf(value) / rate;
                break;
            case _double:
                result = Double.valueOf(value) / rate;
                break;
            case _char:
            case _time:
                result = value;
                break;
        }
        return result;
    }
}
