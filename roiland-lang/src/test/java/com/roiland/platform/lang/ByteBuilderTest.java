package com.roiland.platform.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>类名：ByteBuilderTest
 * <p>描述：
 * <p>版本：0.0.1
 * <p>创建者：杨昆
 * <p>历史
 * <table>
 * <tr><th>版本</th><th>时间</th><th>更新者</th></tr>
 * <tr><td>0.0.1</td><td>2015/8/30</td><td>杨昆</td></tr>
 * </table>
 */
public class ByteBuilderTest {

    @Test
    public void testSkip() {
        ByteBuilder builder = ByteBuilder.wrap(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        builder.skip(5);
        Assert.assertEquals(0x05, builder.pick());
    }

    @Test
    public void testGetAsInteger() {
        ByteBuilder builder = ByteBuilder.wrap(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        int out = builder.getAsInteger();
        Assert.assertEquals(66051, out);
    }

    @Test
    public void testGetAsAscII() {
        ByteBuilder builder = ByteBuilder.wrap(new byte[] {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x00, 0x37, 0x38, 0x39});
        String out = builder.getAsAscII(9);
        Assert.assertEquals("012345", out);
        Assert.assertEquals(9, builder.pos());
    }
}
