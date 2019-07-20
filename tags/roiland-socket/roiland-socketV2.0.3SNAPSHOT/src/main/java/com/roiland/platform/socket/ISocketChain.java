package com.roiland.platform.socket;

import com.roiland.platform.socket.bean.ProtocolBean;

import io.netty.channel.Channel;

/**
 * 类名：ISocketChain<br/>
 * 作用：楼兰协议处理链(chain)接口<br/>
 *
 * @author jeffy
 * @since 2015/5/21
 */
public interface ISocketChain {

    /**
     * 楼兰协议处理方法
     * @param channel
     * @param object
     */
    void doChain(Channel channel, ProtocolBean object);
}
