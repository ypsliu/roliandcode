package com.roiland.platform.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>类名：NumUtilsTest
 * <p>描述：
 * <p>版本：0.0.1
 * <p>创建者：杨昆
 * <p>历史
 * <table>
 * <tr><th>版本</th><th>时间</th><th>更新者</th></tr>
 * <tr><td>0.0.1</td><td>2015/8/31</td><td>杨昆</td></tr>
 * </table>
 */
public class NumUtilsTest {

    @Test
    public void testIntToByte() {
        byte out = NumUtils.intToByte(100);
        Assert.assertEquals(0x64, out);
    }

    @Test
    public void testShortToBytes() {
        short s = 512;
        byte[] bytes = NumUtils.shortToBytes(s);
        Assert.assertEquals(0x02, bytes[0]);
        Assert.assertEquals(0x00, bytes[1]);
    }

    @Test
    public void testIntTo2Bytes() {
        byte[] bytes = NumUtils.intTo2Bytes(512);
        Assert.assertEquals(0x02, bytes[0]);
        Assert.assertEquals(0x00, bytes[1]);
    }

    @Test
    public void testIntToBytes() {
        byte[] bytes = NumUtils.intToBytes(1024);
        Assert.assertEquals(0x00, bytes[0]);
        Assert.assertEquals(0x00, bytes[1]);
        Assert.assertEquals(0x04, bytes[2]);
        Assert.assertEquals(0x00, bytes[3]);
    }
}
