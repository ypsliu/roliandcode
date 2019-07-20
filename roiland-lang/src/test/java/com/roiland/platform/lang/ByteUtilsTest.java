package com.roiland.platform.lang;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * <p>类名：ByteUtilsTest
 * <p>描述：
 * <p>版本：0.0.1
 * <p>创建者：杨昆
 * <p>历史
 * <table>
 * <tr><th>版本</th><th>时间</th><th>更新者</th></tr>
 * <tr><td>0.0.1</td><td>2015/8/30</td><td>杨昆</td></tr>
 * </table>
 */
public class ByteUtilsTest {

    @Test
    public void testByteToHex() {
        String out = ByteUtils.byteToHex((byte)0x01);
        Assert.assertEquals("01", out);

        out = ByteUtils.byteToHex((byte)0x81);
        Assert.assertEquals("81", out);
    }

    @Test
    public void testBytesToHex() {
        String out = ByteUtils.bytesToHex(new byte[]{0x00, 0x01, 0x02, (byte) 0x81, (byte) 0x82});
        Assert.assertEquals("0001028182", out);
    }

    @Test
    public void testByteToInt() {
        int out = ByteUtils.byteToInt((byte) 0x81);
        Assert.assertEquals(-127, out);
    }

    @Test
    public void testByteToUInt() {
        int out = ByteUtils.byteToUInt((byte) 0x81);
        Assert.assertEquals(129, out);
    }

    @Test
    public void testBytesToInt() {
        int out = ByteUtils.bytesToInt(new byte[]{(byte) 0x81});
        Assert.assertEquals(-127, out);
    }

    @Test
    public void testBytesToUInt() {
        int out = ByteUtils.bytesToUInt(new byte[]{0x01, (byte) 0x81});
        Assert.assertEquals(385, out);
    }

    @Test
    public void testBytesToLong() {
        long out = ByteUtils.bytesToLong(new byte[]{0x01, (byte) 0x81});
        Assert.assertEquals(385, out);

        out = ByteUtils.bytesToLong(new byte[]{0x01, 0x01, (byte) 0x81});
        Assert.assertEquals(65921, out);
    }

    @Test
    public void testBytesToCalendar() {
        Calendar calendar = ByteUtils.bytesToCalendar(new byte[]{0x00, 0x0F, 0x08, 0x1D});
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Assert.assertEquals("20150829", format.format(calendar.getTime()));

        calendar = ByteUtils.bytesToCalendar(new byte[]{0x0F, 0x08, 0x1D, 0x0A, 0x01, 0x02});
        format = new SimpleDateFormat("yyyyMMddHHmmss");
        Assert.assertEquals("20150829100102", format.format(calendar.getTime()));

        calendar = ByteUtils.bytesToCalendar(new byte[]{0x0F, 0x08, 0x1D, 0x0A, 0x01, 0x02, 0x00, 0x64});
        format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Assert.assertEquals("20150829100102100", format.format(calendar.getTime()));
    }

    @Test
    public void testBytesToStrDate() {
        String calendar = ByteUtils.bytesToStrDate(new byte[]{0x00, 0x0F, 0x08, 0x1D});
        Assert.assertEquals("2015-08-29", calendar);

        calendar = ByteUtils.bytesToStrDate(new byte[]{0x0F, 0x08, 0x1D, 0x0A, 0x01, 0x02});
        Assert.assertEquals("2015-08-29 10:01:02", calendar);

        calendar = ByteUtils.bytesToStrDate(new byte[]{0x0F, 0x08, 0x1D, 0x0A, 0x01, 0x02, 0x00, 0x64});
        Assert.assertEquals("2015-08-29 10:01:02 100", calendar);
    }

    @Test
    public void testByteToBoolBin() {
        boolean[] bools = ByteUtils.byteToBoolBin((byte)0x05);
        Assert.assertEquals(false, bools[0]);
        Assert.assertEquals(false, bools[1]);
        Assert.assertEquals(false, bools[2]);
        Assert.assertEquals(false, bools[3]);
        Assert.assertEquals(false, bools[4]);
        Assert.assertEquals(true, bools[5]);
        Assert.assertEquals(false, bools[6]);
        Assert.assertEquals(true, bools[7]);
    }
}
