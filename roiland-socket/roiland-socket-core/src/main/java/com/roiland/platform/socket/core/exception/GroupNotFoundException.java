package com.roiland.platform.socket.core.exception;

/**
 * <p>无法查找到分组资源</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/7/28
 */
public class GroupNotFoundException extends Throwable {

    public GroupNotFoundException(String message) {
        super(message);
    }
}
