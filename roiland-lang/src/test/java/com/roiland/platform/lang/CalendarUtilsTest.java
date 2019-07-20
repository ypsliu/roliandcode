package com.roiland.platform.lang;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * <p>类名：CalendarUtilsTest
 * <p>描述：
 * <p>版本：0.0.1
 * <p>创建者：杨昆
 * <p>历史
 * <table>
 * <tr><th>版本</th><th>时间</th><th>更新者</th></tr>
 * <tr><td>0.0.1</td><td>2015/8/30</td><td>杨昆</td></tr>
 * </table>
 */
public class CalendarUtilsTest {

    @Test
    public void testCalendarToString() {
        Calendar calendar = Calendar.getInstance();
        String strCalendar = CalendarUtils.calendarToString(calendar);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Assert.assertEquals( format.format(calendar.getTime()), strCalendar);
    }

    @Test
    public void testCalendarToShortString() {
        Calendar calendar = Calendar.getInstance();
        String strCalendar = CalendarUtils.calendarToShortString(calendar);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Assert.assertEquals( format.format(calendar.getTime()), strCalendar);
    }
}
