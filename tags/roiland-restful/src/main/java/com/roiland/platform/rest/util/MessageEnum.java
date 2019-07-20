package com.roiland.platform.rest.util;

/**
 * Created by user on 2015/11/24.
 */
public enum MessageEnum {

    SUCCESS(200, "OK"),

    UNEXPECTED(500, "Internal Server Error"),

    MAX_ALLOWED_REQUEST(420, "Max Allowed Request Count"),

    UNAUTHORIZED(421, "Unauthorized"),

    IN_BLACK_LIST(422, "In Request Black List"),

    NOT_IN_WHITE_LIST(423, "Not In Request White List"),

    BAD_REQUEST(424, "Bad Request Parameter"),

    UN_SUPPORTED_REQUEST(425,"Unsupported Request Format");

    private final int code;
    private final String message;

    MessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
