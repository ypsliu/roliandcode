package com.roiland.platform.template.core;

import com.roiland.platform.lang.ByteUtils;
import com.roiland.platform.lang.StringUtils;

import javax.xml.bind.DatatypeConverter;

/**
 * @author leon.chen
 * @since 2016/6/15
 */
public class ByteHelper {
    public static void main(String[] args) {
        String str = "CQABIAUAPAEFDwYZDic1DwYZDicnAQcBDP7+/v4BAQMDAwMBAv////8BAwAAAP4BBAAAAP4BCwAAAAEBEAAAAP9hug==";
        byte[] ary = DatatypeConverter.parseBase64Binary(str);
        String hex = ByteUtils.bytesToHex(ary);
        System.out.println(base64(hex));
    }

    public static String base64(String str){
        byte[] b = StringUtils.hexToBytes(str);
        return DatatypeConverter.printBase64Binary(b);
    }
}
