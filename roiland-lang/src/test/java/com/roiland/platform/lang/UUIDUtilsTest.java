package com.roiland.platform.lang;

import junit.framework.TestCase;

public class UUIDUtilsTest extends TestCase {

    public void testVersionUUID() throws Exception {
        int size = 1000;
        for (int i = 0; i < size; i++) {
            assertEquals("6", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0x60)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("7", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0x70)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("8", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0x80)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("9", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0x90)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("a", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0xa0)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("b", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0xb0)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("c", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0xc0)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("d", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0xd0)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("e", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0xe0)).substring(12, 13));
        }
        for (int i = 0; i < size; i++) {
            assertEquals("f", UUIDUtils.toString(UUIDUtils.randomUUID((byte) 0xf0)).substring(12, 13));
        }
    }

    public void testRandomUUID() throws Exception {
        int size = 1000;
        for (int i = 0; i < size; i++) {
            assertEquals("4", UUIDUtils.toString(UUIDUtils.randomUUID()).substring(12, 13));
        }
    }

    public void testNameUUIDFromBytes() throws Exception {
        assertEquals("3", UUIDUtils.toString(UUIDUtils.nameUUIDFromBytes("abc".getBytes())).substring(12, 13));
    }

    public void testToString() throws Exception {
        int size = 1000;
        for (int i = 0; i < size; i++) {
            String str = UUIDUtils.toString(UUIDUtils.randomUUID());
            int index = str.indexOf('-');
            assertEquals(-1, index);
        }
    }
}