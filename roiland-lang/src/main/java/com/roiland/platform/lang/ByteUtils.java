package com.roiland.platform.lang;

import java.util.Calendar;
import java.util.Locale;

/**
 * 字节处理
 *
 * @author 杨昆
 */
public final class ByteUtils {

    public static final short[] CRC_KEYS = new short[256];
    public static final int G_PLOY = 0x8005;

    static {
        for (int value = 0, i = 0, j = 0; i < 256; i++, j = 0, value = i << 8) {
            while (j++ < CommConsts.EIGHT) {
                value = (value & 0x8000) != 0 ? (value << 1) ^ G_PLOY : value << 1;
            }
            CRC_KEYS[i] = (short) (value & 0xFFFF);
        }
    }

    /**
     * 字节转换为Hex字符串
     * <p/>
     * <ul>
     * <li>0x0A => "0A"</li>
     * <li>0x7F => "7F"</li>
     * <li>0x8A => "8A"</li>
     * </ul>
     *
     * @param b 字节
     * @return Hex字符串
     */
    public static String byteToHex(final byte b) {
        return "" + CommConsts.INT_TO_1_HEX[0x0F & b >>> CommConsts.BIT_LEN_1_BYTE] + CommConsts.INT_TO_1_HEX[0x0F & b];
    }

    /**
     * 字节数组转换为Hex字符串
     * <p/>
     * <ul>
     * <li>0x8A0B => "8A0B"</li>
     * </ul>
     *
     * @param bytes 字节数组
     * @return Hex字符串
     */
    public static String bytesToHex(final byte... bytes) {
        int length = bytes.length;
        char[] out = new char[length << CommConsts.ONE];

        int i = 0, j = 0;
        while (i < length) {
            out[j++] = CommConsts.INT_TO_1_HEX[0xF & bytes[i] >>> CommConsts.BIT_LEN_1_BYTE];
            out[j++] = CommConsts.INT_TO_1_HEX[0xF & bytes[i++]];
        }
        return String.valueOf(out);
    }

    /**
     * 字节转带符号int型
     * <p/>
     * <ul>
     * <li>0x0A => 10</li>
     * <li>0x8A => -118</li>
     * </ul>
     *
     * @param b 字节
     * @return 有符号int型
     */
    public static int byteToInt(final byte b) {
        return b;
    }

    /**
     * 字节转换为无符号int型
     * <p/>
     * <ul>
     * <li>0x0A => 10</li>
     * <li>0x8A => 138</li>
     * </ul>
     *
     * @param b 字节
     * @return 无符号int型
     */
    public static int byteToUInt(final byte b) {
        return 0xFF & b;
    }

    /**
     * 字节数据转为有符号int型
     * <p/>
     * <ul>
     * <li>数组长度不得超过4个字节</li>
     * <li>0x01 => 1</li>
     * <li>0x8A => -118</li>
     * <li>0x8A01 => -30207</li>
     * <li>0x8A81 => -30079</li>
     * </ul>
     *
     * @param bytes 字节数组
     * @return 有符号int型
     */

    public static int bytesToInt(final byte... bytes) {
        final int length = bytes.length;
        if (length > CommConsts.MAX_INT) {
            throw new IllegalArgumentException("无法转换，数组长度超出4字节");
        }
        int pos = 0, out = bytes[0] << (length - ++pos << CommConsts.THREE);
        while (pos < length) {
            out |= (bytes[pos] & 0xFF) << (length - ++pos << CommConsts.THREE);
        }
        return out;
    }

    /**
     * 字节数组转为无符号int型
     * <p>异常情况如下：
     * <ul>
     * <li>数组长度不得超过4个字节</li>
     * <li>当数组为4个字节且最高位为1时，无法将数组转换为无符号int型，建议使用{@code ByteUtils.bytesToLong(final byte...bytes)}</li>
     * </ul>
     *
     * @param bytes 字节数组
     * @return 无符号int型
     */
    public static int bytesToUInt(final byte... bytes) {
        final int length = bytes.length;
        if (length == CommConsts.MAX_INT && bytes[0] < CommConsts.ZERO) {
            throw new IllegalArgumentException("无法将4字节且高位为1的数组转换为无符号类型");
        } else if (length > CommConsts.MAX_INT) {
            throw new IllegalArgumentException("无法转换，数组长度超出4字节");
        }
        int out = 0, pos = 0;
        while (pos < length) {
            out |= (0xFF & bytes[pos]) << (length - ++pos << CommConsts.THREE);
        }
        return out;
    }

    /**
     * 字节数组转化为long型
     * <p/>
     * <ul>
     * <li>数组长度不得超过8个字节</li>
     * </ul>
     *
     * @param bytes 字节数组
     * @return 有符号long型
     */
    public static long bytesToLong(final byte... bytes) {
        final int length = bytes.length;
        if (length > CommConsts.MAX_LONG) {
            throw new IllegalArgumentException("无法转换，数组长度超出8字节");
        }
        int pos = 0;
        long out = bytes[0] << (length - ++pos << CommConsts.THREE);
        while (pos < length) {
            out |= (bytes[pos] & 0xFF) << (length - ++pos << CommConsts.THREE);
        }
        return out;
    }

    /**
     * 将字节数组转换为日历 (奥迪故障码解析专用)，字节数组要保证4位
     *
     * @param bytes 字节数组
     * @return 日历
     */
    public static Calendar bytesToAudiCalendar(final byte... bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("非法字节数组长度");
        }
        final Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.set(Calendar.YEAR, 2000 + (bytes[0] & 0xFC >> 2));
        final int month = (bytes[0] & 0x03 << 2) | (bytes[1] & 0xC0 >> 6);
        cal.set(Calendar.MONTH, month > 0 ? month - 1 : month);
        cal.set(Calendar.DATE, (bytes[1] & 0x3E) >> 1);
        cal.set(Calendar.HOUR_OF_DAY, ((bytes[1] & 0x01) << 4) | ((bytes[2] & 0xF0) >> 4));
        cal.set(Calendar.MINUTE, ((bytes[2] & 0x0F) << 2) | ((bytes[3] & 0xC0) >> 6));
        cal.set(Calendar.SECOND, bytes[3] & 0x3F);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    /**
     * 字节数组转换为日历
     * <ul>
     * <li>长度为4位时，分别代表年（2位）、月（3位）、日（4位），0位无效</li>
     * <li>长度为6位时，分别代表年（1位）、月（2位）、日（3位）、时（4位）、分（5位）、秒（6位）</li>
     * <li>长度为8位时，分别代表年（1位）、月（2位）、日（3位）、时（4位）、分（5位）、秒（6位）、毫秒（7、8位）</li>
     * </ul>
     *
     * @param bytes 要转换的字节数组
     * @return 日历
     */
    public static Calendar bytesToCalendar(final byte... bytes) {
        final int length = bytes.length;
        final Calendar calendar = Calendar.getInstance();
        if (length == 4) {
            calendar.set(bytes[1] + 2000, bytes[2] - 1, bytes[3]);
        } else if (length == 6) {
            calendar.set(bytes[0] + 2000, bytes[1] - 1, bytes[2], bytes[3], bytes[4], bytes[5]);
            calendar.set(Calendar.MILLISECOND, 0);
        } else if (length == 8){
            calendar.set(bytes[0] + 2000, bytes[1] - 1, bytes[2], bytes[3], bytes[4], bytes[5]);
            calendar.set(Calendar.MILLISECOND, ByteUtils.bytesToInt(bytes[6], bytes[7]));
        } else {
            throw new IllegalArgumentException("数组长度不合法");
        }
        return calendar;
    }

    /**
     * 字节数组转换为日期字符串
     * <ul>
     * <li>长度为4位时，分别代表年（2位）、月（3位）、日（4位），0位无效，格式：yyyy-MM-dd</li>
     * <li>长度为6位时，分别代表年（1位）、月（2位）、日（3位）、时（4位）、分（5位）、秒（6位），格式：yyyy-MM-dd HH:mm:ss</li>
     * <li>长度为8位时，分别代表年（1位）、月（2位）、日（3位）、时（4位）、分（5位）、秒（6位）、毫秒（7、8位），格式：yyyy-MM-dd HH:mm:ss SSS</li>
     * </ul>
     */
    public static String bytesToStrDate(final byte... bytes) {
        int length = bytes.length;
        if (length == 4) {
            return (bytes[1] + 2000) + "-" + byteToStrInt(bytes[2]) + "-" + byteToStrInt(bytes[3]);
        } else if (length == 6) {
            return (bytes[0] + 2000) + "-" + byteToStrInt(bytes[1]) + "-" + byteToStrInt(bytes[2]) + " " + byteToStrInt(bytes[3]) + ":" + byteToStrInt(bytes[4]) + ":" + byteToStrInt(bytes[5]);
        } else if (length == 8) {
            final int iMillisecond = ByteUtils.bytesToInt(bytes[6], bytes[7]);
            String millisecond = iMillisecond < 10? "00" + iMillisecond: (iMillisecond < 100? "0" + iMillisecond: "" + iMillisecond);
            return (bytes[0] + 2000) + "-" + byteToStrInt(bytes[1]) + "-" + byteToStrInt(bytes[2]) + " " + byteToStrInt(bytes[3]) + ":" + byteToStrInt(bytes[4]) + ":" + byteToStrInt(bytes[5]) + " " + millisecond;
        } else {
            throw new IllegalArgumentException("数组长度不合法");
        }
    }

    /**
     * 字节转boolean数组，数组0~7位分别代表字节8~1位
     *
     * @param b 字节
     * @return boolean数组
     */
    public static boolean[] byteToBoolBin(final byte b) {
        boolean[] out = new boolean[8];
        out[7] = (0x01 & b) == 1;
        out[6] = (0x01 & b >>> 1) == 1;
        out[5] = (0x01 & b >>> 2) == 1;
        out[4] = (0x01 & b >>> 3) == 1;
        out[3] = (0x01 & b >>> 4) == 1;
        out[2] = (0x01 & b >>> 5) == 1;
        out[1] = (0x01 & b >>> 6) == 1;
        out[0] = (0x01 & b >>> 7) == 1;
        return out;
    }

    public static byte[] copy(final byte[] src, final int pos, final int length) {
        final byte[] out = new byte[length];
        System.arraycopy(src, pos, out, 0, length);
        return out;
    }

    public static byte[] crc(final byte[] bytes) {
        int out = 0, i = 0, length = bytes.length;
        while (i < length) {
            out = ((0xFF & out) << 8) ^ CRC_KEYS[((0xFF & out >> 8) ^ bytes[i++]) & 0xFF];
        }
        return NumUtils.intTo2Bytes(out);
    }

    /**
     * 不足两位左补0，0x09 => 09
     * @param b 字节
     * @return
     */
    public static String byteToStrInt(final byte b) {
        return b < 10? "0" + b: "" + b;
    }
}
