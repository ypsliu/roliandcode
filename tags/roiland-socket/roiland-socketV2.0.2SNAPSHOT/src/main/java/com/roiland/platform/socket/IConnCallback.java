package com.roiland.platform.socket;

import io.netty.channel.Channel;

/**
 * 类名：IConnCallback<br/>
 * 作用：建立连接后，回调接口<br/>
 *
 * @author 杨昆
 * @since 2015/5/13
 */
public interface IConnCallback {

    /**
     * 建立连接后，回调方法
     * @param group 当前分组
     * @param uuid 当前客户端唯一标识
     * @param channel 当前通道
     */
    void call(String group, String uuid, Channel channel);
}
