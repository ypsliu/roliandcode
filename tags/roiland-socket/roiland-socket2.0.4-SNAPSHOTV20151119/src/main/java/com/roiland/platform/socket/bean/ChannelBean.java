package com.roiland.platform.socket.bean;

import io.netty.channel.Channel;

/**
 * 类名：ChannelBean<br/>
 * 作用：SocketBean扩展Bean。增加通道属性Channel<br/>
 * 
 * @author 杨昆
 * @since 2015/6/26
 */
public class ChannelBean extends SocketBean {

	/** 通道 */
    private Channel channel;

    /**
     * @param uuid  客户端ID
     * @param group 分组
     * @param host  主机
     * @param port  端口
     */
    public ChannelBean(String uuid, String group, String host, int port) {
        super(uuid, group, host, port);
    }

    /**
     * 获取通道
     * @return 通道
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * 设置通道
     * @param channel 通道
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
