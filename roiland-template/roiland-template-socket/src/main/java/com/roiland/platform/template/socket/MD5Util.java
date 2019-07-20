package com.roiland.platform.template.socket;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static final String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5","6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    
    /**
     * 将字符串转换成MD5_32位字符串
     * @param origin 需要转换的字符串
     * @return  转换后的MD5字符串
     */
    public static byte[] MD5Encodebyte(byte[] origin) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = md.digest(origin);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] MD5Encodebyte16(byte[] origin) {
    	byte[] result = new byte[16];
        System.arraycopy(MD5Encodebyte(origin), 0, result, 0, 16);
    	return result;
    }
    public static byte[] MD5Encode(String origin) {
        return MD5Encodebyte(origin.getBytes());
    }
}