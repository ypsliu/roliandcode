package com.roiland.platform.lang;

/**
 * 字符串处理
 *
 * @author 杨昆
 */
public final class StringUtils extends org.apache.commons.lang.StringUtils {

    /**
     * 单个Hex转单字节，“0A” => 0x0A
     *
     * @param hex 单Hex字符串
     * @return 单字节
     */
    public static byte hexToByte(final String hex) {
        int length = hex.length();
        if (length > 2) {
            throw new IllegalArgumentException("非法字符串长度，长度不能超过2个字符");
        }
        return Integer.valueOf(hex, 16).byteValue();
    }

    /**
     * Hex字符串转字节数组
     *
     * @param hex hex字符串
     * @return 字节数组
     */
    public static byte[] hexToBytes(final String hex) {
        final int length = hex.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("非法字符串长度，长度非2的倍数");
        }

        final byte[] out = new byte[length >> 1];
        final char[] chars = hex.toCharArray();

        int pos = 0, index = 0;    // 当前数组位置
        while (pos < length) {
            out[index++] = CommConsts.INT_TO_BYTE[
                    (CharUtils.charToByte(chars[pos++]) << CommConsts.BIT_LEN_1_BYTE)
                            + CharUtils.charToByte(chars[pos++])
                    ];
        }
        return out;
    }

    /**
     * '
     * Hex字符串转int类型
     *
     * @param hex Hex字符串
     * @return int类型
     */
    public static int hexToInt(final String hex) {
        if (StringUtils.isEmpty(hex)) {
            throw new IllegalArgumentException("非法字符串长度");
        }

        final int length = hex.length();
        if (length % CommConsts.TWO != 0) {
            throw new IllegalArgumentException("非法字符串长度，长度非2的倍数");
        } else if (length > CommConsts.EIGHT) {
            throw new IllegalArgumentException("非法字符串长度，长度大于8个字符");
        }

        final char[] chars = hex.toCharArray();
        int out = CommConsts.HEX_TO_HIGH[CharUtils.charToByte(chars[0])] + CharUtils.charToByte(chars[1]) << (length -  2) * CommConsts.BIT_LEN_1_BYTE;
        int curr = 2;
        while (curr < length) {
            out += CharUtils.charToByte(chars[curr]) << (length - curr - 1) * CommConsts.BIT_LEN_1_BYTE;
            curr++;
        }
        return out;
    }

    /**
     * Hex转无符号Int型
     *
     * @param hex 16进制字符串
     * @return 无符号Int型
     */
    public static int hexToUInt(final String hex) {
        final int length = hex.length();
        if (length % CommConsts.TWO != 0) {
            throw new IllegalArgumentException("非法字符串长度，长度非2的倍数");
        } else if (length > CommConsts.EIGHT) {
            throw new IllegalArgumentException("非法字符串长度，长度大于8个字符");
        }

        final char[] chars = hex.toCharArray();
        if (length == CommConsts.EIGHT && chars[0] > '8') {
            throw new IllegalArgumentException("无法将8字符长度的16进制字符串转换为无符号类型");
        } else {
            int out = 0;
            int curr = 0;        // 当前字符位置
            while (curr < length) {
                out += CharUtils.charToByte(chars[curr]) << (length - curr - 1) * CommConsts.BIT_LEN_1_BYTE;
                curr++;
            }
            return out;
        }
    }

    /**
     * Hex转有符号float型
     *
     * @param hex Hex字符串
     * @return 有符号int型
     */
    public static float hexToFloat(final String hex) {
        return hexToInt(hex);
    }

    /**
     * Hex转无符号float型
     *
     * @param hex Hex字符串
     * @return 无符号float型
     */
    public static float hexToUFloat(final String hex) {
        return hexToUInt(hex);
    }

    /**
     * Hex转Long型
     *
     * @param hex Hex字符串
     * @return Long型
     */
    public static long hexToLong(final String hex) {
        final int length = hex.length();
        if (length % CommConsts.TWO != 0) {
            throw new IllegalArgumentException("非法字符串长度，长度非2的倍数");
        } else if (length > 16) {
            throw new IllegalArgumentException("非法字符串长度，长度大于16个字符");
        }

        final char[] chars = hex.toCharArray();
        long out = CommConsts.HEX_TO_HIGH[CharUtils.charToByte(chars[0])] + CharUtils.charToByte(chars[1]) << (length -  2) * CommConsts.BIT_LEN_1_BYTE;
        int curr = 2;
        while (curr < length) {
            out += CharUtils.charToByte(chars[curr]) << (length - curr - 1) * CommConsts.BIT_LEN_1_BYTE;
            curr++;
        }
        return out;
    }

    /**
     * Hex转无符号Long型
     *
     * @param hex Hex字符串
     * @return 无符号Long型
     */
    public static long hexToULong(final String hex) {
        final int length = hex.length();

        if (length % CommConsts.TWO != 0) {
            throw new IllegalArgumentException("非法字符串长度，长度非2的倍数");
        } else if (length > 16) {
            throw new IllegalArgumentException("非法字符串长度，长度大于8个字符");
        }

        final char[] chars = hex.toCharArray();
        if (length == 16 && chars[0] > '8') {
            throw new IllegalArgumentException("无法将16字符长度的16进制字符串转换为无符号类型");
        } else {
            long out = 0;
            int curr = 0;        // 当前字符位置
            while (curr < length) {
                out += CharUtils.charToByte(chars[curr]) << (length - curr - 1) * CommConsts.BIT_LEN_1_BYTE;
                curr++;
            }
            return out;
        }
    }

    /**
     * Hex转有符号Double型
     *
     * @param hex Hex字符串
     * @return 有符号double型
     */
    public static double hexToDouble(final String hex) {
        return hexToLong(hex);
    }

    /**
     * Hex字符串转无符号double型
     *
     * @param hex Hex字符串
     * @return 无符号double型
     */
    public static double hexToUDouble(final String hex) {
        return hexToULong(hex);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return True：字符串为空；false：字符串不为空
     */
    public static boolean isEmpty(final String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }
}
