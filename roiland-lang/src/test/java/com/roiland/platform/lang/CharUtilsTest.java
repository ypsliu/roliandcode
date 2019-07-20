package com.roiland.platform.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>类名：CharUtilsTest
 * <p>描述：
 * <p>版本：0.0.1
 * <p>创建者：杨昆
 * <p>历史
 * <table>
 * <tr><th>版本</th><th>时间</th><th>更新者</th></tr>
 * <tr><td>0.0.1</td><td>2015/8/30</td><td>杨昆</td></tr>
 * </table>
 */
public class CharUtilsTest {

    @Test
    public void testCharToByte() {
        byte b = CharUtils.charToByte('0');
        Assert.assertEquals(0x00, b);
    }
}
