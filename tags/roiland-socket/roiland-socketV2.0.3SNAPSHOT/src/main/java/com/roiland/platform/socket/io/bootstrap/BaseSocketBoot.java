package com.roiland.platform.socket.io.bootstrap;

import com.roiland.platform.socket.property.SocketProperties;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 类名：BaseSocketBoot<br/>
 * 作用：初始化Socket默认参数<br/>
 *
 * @author 杨昆
 * @since 2015/5/19
 */
public abstract class BaseSocketBoot {

    protected Integer iSocketRcvBuf = null;
    protected Integer iSocketSndBuf = null;
    protected Boolean boolTcpNoDelay = null;
    protected Boolean boolKeepAlive = null;
    protected Boolean boolReuseAddress = null;
    protected Boolean boolSocketLogOutput = null;
    protected Boolean boolSocketLogInput = null;
    protected EventLoopGroup eventLoopGroup = null;
    protected Integer iCoreSize = null;

    public BaseSocketBoot() {
    	this.iSocketRcvBuf = Integer.valueOf(System.getProperty(SocketProperties.SOCKET_RCV_BUF, "1048576"));
    	this.iSocketSndBuf = Integer.valueOf(System.getProperty(SocketProperties.SOCKET_SND_BUF, "1048576"));
    	this.boolKeepAlive = Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_SO_KEEPALIVE, "true"));
    	this.boolTcpNoDelay =  Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_TCP_NODELAY, "true"));
    	this.boolReuseAddress =  Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_SO_REUSEADDR, "true"));
    	this.boolSocketLogInput =  Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_LOGGER_INPUT, "true"));
    	this.boolSocketLogOutput =  Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_LOGGER_OUTPUT, "true"));
        this.iCoreSize =  Integer.valueOf(System.getProperty(SocketProperties.SOCKET_CORE_SIZE, "" + Runtime.getRuntime().availableProcessors() * 2));
        this.eventLoopGroup = new NioEventLoopGroup(iCoreSize);
    }
}
