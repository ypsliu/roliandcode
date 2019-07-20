package com.roiland.platform.lang;

import java.util.Calendar;

/**
 * 日期相关处理类
 */
public final class CalendarUtils {

    /**
     * 时间转换时间字符串 格式：yyyy-MM-dd HH:mm:ss
     *
     * @param calendar 日期
     * @return yyyy-MM-dd HH:mm:ss格式日期字符串
     */
    public static String calendarToString(final Calendar calendar) {
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int date = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int second = calendar.get(Calendar.SECOND);
        return calendar.get(Calendar.YEAR)
                + "-" + (month < 10 ? "0" + month : month)
                + "-" + (date < 10 ? "0" + date : date)
                + " " + (hour < 10 ? "0" + hour : hour)
                + ":" + (minute < 10 ? "0" + minute : minute)
                + ":" + (second < 10 ? "0" + second : second);
    }

    /**
     * 时间转换时间字符串 格式：yyyy-MM-dd
     *
     * @param calendar 日期
     * @return yyyy-MM-dd格式字符串
     */
    public static String calendarToShortString(final Calendar calendar) {
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int date = calendar.get(Calendar.DATE);
        return calendar.get(Calendar.YEAR)
                + "-" + (month < 10 ? "0" + month : month)
                + "-" + (date < 10 ? "0" + date : date);
    }

    /**
     * 将日历 转换为字节数组 指定长度len如果是6,则不带毫秒;如果为8,则带毫秒
     *
     * @param cal 要转换的字节数组
     * @return 字节数组
     */
    public static byte[] calendarTo4Bytes(final Calendar cal) {
        return new byte[]{
                0x00
                , CommConsts.INT_TO_BYTE[cal.get(Calendar.YEAR) - 2000]
                , CommConsts.INT_TO_BYTE[cal.get(Calendar.MONTH) + 1]
                , CommConsts.INT_TO_BYTE[cal.get(Calendar.DATE)]
        };
    }

    /**
     * 将日历 转换为字节数组 指定长度len如果是6,则不带毫秒;如果为8,则带毫秒
     *
     * @param cal 要转换的字节数组
     * @return 字节数组
     */
    public static byte[] calendarTo6Bytes(final Calendar cal) {
        return new byte[]{
                CommConsts.INT_TO_BYTE[cal.get(Calendar.YEAR) - 2000],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.MONTH) + 1],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.DAY_OF_MONTH)],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.HOUR_OF_DAY)],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.MINUTE)],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.SECOND)]
        };
    }

    /**
     * 将日历 转换为字节数组 指定长度len如果是6,则不带毫秒;如果为8,则带毫秒
     *
     * @param cal 要转换的字节数组
     * @return 字节数组
     */
    public static byte[] calendarTo8Bytes(final Calendar cal) {
        int millisecond = cal.get(Calendar.MILLISECOND);
        return new byte[]{
                CommConsts.INT_TO_BYTE[cal.get(Calendar.YEAR) - 2000],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.MONTH) + 1],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.DAY_OF_MONTH)],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.HOUR_OF_DAY)],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.MINUTE)],
                CommConsts.INT_TO_BYTE[cal.get(Calendar.SECOND)],
                CommConsts.INT_TO_BYTE[0xFF & millisecond >>> 8],
                CommConsts.INT_TO_BYTE[0xFF & millisecond]
        };
    }
}
