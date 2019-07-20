/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roiland.platform.socket.io.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 创建TCP服务器端，监听服务器端口(Port)。
 * 
 * @author 杨昆
 * @since 2015-05-02
 * @version 1.0.0
 */
public abstract class BaseServerBoot extends BaseSocketBoot{

    private static final Log LOGGER = LogFactory.getLog(BaseServerBoot.class);

    protected ServerBootstrap bootstrap = null;
    protected EventLoopGroup bossGroup = null;

    public BaseServerBoot() {
        this.bossGroup = new NioEventLoopGroup(2);
        // 创建Netty Bootstrap
        this.bootstrap = new ServerBootstrap();
        this.bootstrap.group(bossGroup, eventLoopGroup).channel(NioServerSocketChannel.class);
        this.bootstrap.option(ChannelOption.TCP_NODELAY, boolTcpNoDelay);
        this.bootstrap.option(ChannelOption.SO_KEEPALIVE, boolKeepAlive);
        this.bootstrap.option(ChannelOption.SO_REUSEADDR, boolReuseAddress);
        this.bootstrap.option(ChannelOption.SO_RCVBUF, iSocketRcvBuf);
        this.bootstrap.option(ChannelOption.SO_SNDBUF, iSocketSndBuf);
        this.bootstrap.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
    }
    
    public void listen(int port) {
        try {
            this.bootstrap.bind(port).syncUninterruptibly();
            LOGGER.info("监听服务器端口成功，端口：" + port);
        } catch (Exception e) {
            LOGGER.error("监听服务器端口失败，端口：" + port + "。异常信息：" + e.getMessage());
            throw new IllegalArgumentException("监听服务器端口失败，端口：" + port + "。异常信息：" + e.getMessage());
        }
    }

}
