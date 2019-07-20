package com.roiland.platform.socket.server.exception;

/**
 * <p>服务器端认证失败异常</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/7/28
 */
public class AuthNotAccessException extends Throwable {

    /**
     * <p>构造器</p>
     *
     * @param message 异常信息
     */
    public AuthNotAccessException(String message) {
        super(message);
    }

}
