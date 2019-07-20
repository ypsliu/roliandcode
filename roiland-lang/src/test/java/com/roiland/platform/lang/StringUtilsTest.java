package com.roiland.platform.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>类名：StringUtilsTest
 * <p>描述：
 * <p>版本：0.0.1
 * <p>创建者：杨昆
 * <p>历史
 * <table>
 * <tr><th>版本</th><th>时间</th><th>更新者</th></tr>
 * <tr><td>0.0.1</td><td>2015/8/31</td><td>杨昆</td></tr>
 * </table>
 */
public class StringUtilsTest {

    @Test
    public void testHexToByte() {
        byte out = StringUtils.hexToByte("0A");
        Assert.assertEquals(0x0A, out);
    }

    @Test
    public void testHexToBytes() {
        byte[] out = StringUtils.hexToBytes("0A10");
        Assert.assertEquals(0x0A, out[0]);
        Assert.assertEquals(0x10, out[1]);
    }

    @Test
    public void testHexToInt() {
        int out = StringUtils.hexToInt("0201");
        Assert.assertEquals(513, out);

        out = StringUtils.hexToInt("8101");
        Assert.assertEquals(-32511, out);
    }

    @Test
    public void testHexToUInt() {
        int out = StringUtils.hexToUInt("0101");
        Assert.assertEquals(257, out);

        out = StringUtils.hexToUInt("8101");
        Assert.assertEquals(33025, out);
    }

    @Test
    public void testHexToLong() {
        long out = StringUtils.hexToLong("020101");
        Assert.assertEquals(131329, out);

        out = StringUtils.hexToLong("810101");
        Assert.assertEquals(-8322815, out);
    }

    @Test
    public void testHexToULong() {
        long out = StringUtils.hexToULong("020101");
        Assert.assertEquals(131329, out);

        out = StringUtils.hexToULong("810101");
        Assert.assertEquals(8454401, out);
    }
}
