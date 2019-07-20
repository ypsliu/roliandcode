package com.roiland.platform.template.socket;

public class CRC16Util {

    public static short[] crcTable = new short[256];
    // 生成多项式
    public static int gPloy = 0x8005;
    public static boolean initFlag = false;

    static {
        for (int i = 0; i < 256; i++) {
            crcTable[i] = getCRCOfByte(i);
        }
    }

    // 1-255 int 转 crc 位移后的short 值
    public static short getCRCOfByte(int aByte) {
        int value = aByte << 8;
        for (int count = 7; count >= 0; count--) {
            // 高第16位为1，可以按位异或
            if ((value & 0x8000) != 0) {
                value = (value << 1) ^ gPloy;
            } else {
                // 首位为0，左移
                value = value << 1;
            }
        }
        // 取低16位的值
        value = value & 0xFFFF;
        return (short) value;
    }

    // 得到crc的short 值
    public static int crc(byte[] data) {
        int crc = 0;
        int length = data.length;
        for (int i = 0; i < length; i++) {
            crc = ((crc & 0xFF) << 8) ^ crcTable[(((crc & 0xFF00) >> 8) ^ data[i]) & 0xFF];
        }
        crc = crc & 0xFFFF;
        return crc;
    }

}