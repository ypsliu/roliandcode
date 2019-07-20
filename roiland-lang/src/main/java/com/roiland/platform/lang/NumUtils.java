package com.roiland.platform.lang;

/**
 * 数字相关操作类
 */
public final class NumUtils {

    public static byte intToByte(final int i) {
        if (i > 255 || i < -128) {                  // 判断当前取值范围  -128~127或0~255
            throw new IllegalArgumentException("输入参数范围不合法");
        } else if (i >= 0) {
            return CommConsts.INT_TO_BYTE[i];
        } else {
            return CommConsts.INT_TO_BYTE[256 + i];
        }
    }

    /**
     * Short型转2字节数组
     *
     * @param i Short型
     * @return 数组
     */
    public static byte[] shortToBytes(final short i) {
        return new byte[]{
                CommConsts.INT_TO_BYTE[0xFF & i >>> 8]
                , CommConsts.INT_TO_BYTE[0xFF & i]
        };
    }

    /**
     * int型转4字节数组
     *
     * @param i int数字
     * @return 4字节数组
     */
    public static byte[] intToBytes(final int i) {
        return new byte[] {
                CommConsts.INT_TO_BYTE[0xFF & i >>> 24]
                , CommConsts.INT_TO_BYTE[0xFF & i >>> 16]
                , CommConsts.INT_TO_BYTE[0xFF & i >>> 8]
                , CommConsts.INT_TO_BYTE[0xFF & i]
        };
    }

    /**
     * int型转2字节数组
     *
     * @param i int数字
     * @return 2字节数组
     */
    public static byte[] intTo2Bytes(final int i) {
        return new byte[] {
                CommConsts.INT_TO_BYTE[0xFF & i >>> 8],
                CommConsts.INT_TO_BYTE[0xFF & i]
        };
    }

    /**
     * long型转不定长度字节数组
     *
     * @param i long型数字
     * @param size 返回数组大小
     * @return 数组
     */
    public static byte[] longToBytes(final long i, final int size) {
        byte[] out = new byte[size];
        int pos = 0;
        while (pos < size - 1) {
            out[pos++] = CommConsts.INT_TO_BYTE[(int) (0xFF & i >>> (size - pos - 1 << 3))];
        }
        out[size - 1] = CommConsts.INT_TO_BYTE[(int) (0xFF & i)];
        return out;
    }
}
