package com.roiland.platform.socket.core;

import io.netty.channel.Channel;

import java.util.Map;

/**
 * <p>数据回调接口</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/08/23
 */
public interface IDataCallback {

    /**
     * <p>数据回调方法</p>
     *
     * @param channel
     * @param params
     * @param throwable
     */
    void callback(Channel channel, Map<String, String> params, Throwable throwable);
}
