package com.roiland.platform.restful;

/**
 * Created by jeffy.yang on 16-3-28.
 */
public enum SystemErrorEnum {
    //4xx框架异常
    CODE_422(422, "In Request Black List"),
    CODE_423(423, "Not In Request White List"),
    CODE_424(424, "Bad Request Headers"),
    CODE_426(426, "Failed To Authorize Signature"),
    CODE_427(427, "Invalid Secret Key"),
    CODE_428(428, "Invalid Sign Algorithm Version"),
    CODE_429(429, "Failed To Get Secret Key"),
    CODE_430(430, "Failed To Get UUID"),
    CODE_431(431, "Failed To Get User Role"),
    CODE_432(432, "Failed To Get Expired Time"),
    CODE_433(433, "Access Token Expired"),
    CODE_434(434, "Unsupported Version"),
    CODE_435(435, "Failed To Get Access Token"),
    CODE_436(436, "Failed To Get Type"),
    CODE_500(500, "Internal Server Error");

    private final int code;
    private final String message;

    private SystemErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
