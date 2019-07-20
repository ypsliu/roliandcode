package com.roiland.platform.lang;


public final class CharUtils {

    public static byte charToByte(final char c) {
        return CommConsts.CHAR_TO_BYTE[c];
    }
}
