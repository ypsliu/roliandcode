package com.roiland.platform.socket.core;

import io.netty.channel.ChannelFuture;

/**
 * <p>客户端连接建立处理方法</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/08/23
 */
public interface ISocketConnection {

    /**
     * 客户端连接处理
     *
     * @param group
     * @param host
     * @param port
     * @return
     */
    ChannelFuture connect(String group, String host, Integer port);
}
