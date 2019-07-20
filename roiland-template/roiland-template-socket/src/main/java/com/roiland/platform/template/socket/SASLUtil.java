package com.roiland.platform.template.socket;

import com.roiland.platform.lang.ByteUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SASLUtil {

    /**
     * hashPwd: 对原始密码进行hash加密
     *
     * @param uid      用户的id
     * @param password 用户的原始密码
     * @return 加密后的密码
     */
    public static String hashPwd(String uid, String password) {
        return DatatypeConverter.printBase64Binary(hmacMD5(MD5Util.MD5Encode(uid), password.getBytes()));
    }

    /**
     * saltPwd: 对hash密码进行加盐
     *
     * @param uid     用户的id
     * @param hashPwd 用户的hashed密码
     * @return 加盐后的密码
     */
    public static String saltPwd(String uid, String hashPwd) {
        //TODO 时间戳:APP是毫秒 设备是秒
        long timestamp = System.currentTimeMillis();
        //随机数
        long nonce =Math.round(Math.random() * 10000000);
        return saltPwd(timestamp,nonce,uid,hashPwd);
    }

    private static String saltPwd(long timestamp,long nonce,String uid, String hashPwd) {
        //初始challenge
        String challenge = "<" + nonce + "." + timestamp + ">";
        //生成response
        byte[] tmpB = hmacMD5(hashPwd.getBytes(), challenge.getBytes());
        String tmp = ByteUtils.bytesToHex(tmpB).toLowerCase();
        String response = uid + " " + tmp + " " + challenge;
        String base64Sasl = DatatypeConverter.printBase64Binary(response.getBytes());
        return base64Sasl;
    }

    private static byte[] hmacMD5(byte[] key, byte[] data) {
        SecretKey sk = new SecretKeySpec(key, "HmacMD5");
        Mac mac = null;
        byte[] result = new byte[0];
        try {
            mac = Mac.getInstance("HmacMD5");
            mac.init(sk);
            result = mac.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return result;
    }
}