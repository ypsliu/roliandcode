package com.roiland.platform.socket.io.bootstrap;

import com.roiland.platform.socket.IConnCallback;
import com.roiland.platform.socket.SocketManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 创建TCP客户端线程。通过主机地址(Host)及端口(Port)连接到服务器端。
 * 
 * @author yangkun
 * @since 2015-05-02
 * @version 1.0.0
 */
public abstract class BaseClientBoot extends BaseSocketBoot {

    private static final Log LOGGER = LogFactory.getLog(BaseClientBoot.class);

    protected Bootstrap bootstrap = null;

    public BaseClientBoot() {
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(eventLoopGroup);
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.option(ChannelOption.TCP_NODELAY, boolTcpNoDelay);
        this.bootstrap.option(ChannelOption.SO_KEEPALIVE, boolKeepAlive);
        this.bootstrap.option(ChannelOption.SO_REUSEADDR, boolReuseAddress);
        this.bootstrap.option(ChannelOption.SO_RCVBUF, iSocketRcvBuf);
        this.bootstrap.option(ChannelOption.SO_SNDBUF, iSocketSndBuf);
        this.bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }
    
    public Channel startup(String uuid, String group, String remoteHost, Integer remotePort, IConnCallback callback) {
    	Channel channel = null;
        try {
            ChannelFuture future = bootstrap.connect(remoteHost, remotePort).syncUninterruptibly();
            channel = future.channel();
            if (callback != null && channel.isActive()) {
                LOGGER.info("服务【" + uuid + "】已连接服务器端，地址：" + remoteHost + ":" + remotePort);
                callback.call(group, uuid, channel);
            }
            SocketManager.getInstance().add(uuid, group, remoteHost, remotePort, channel);
        } catch (Exception e) {
            LOGGER.error("服务【" + uuid + "】连接服务器失败，异常信息：" + e.getMessage());
        }
        return channel;
    }
}
