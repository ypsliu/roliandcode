package com.roiland.platform.socket.property;

/**
 * Created by yangkun on 15/5/10.
 */
public final class SocketProperties {

    /**
     *
     */
    public static final String SOCKET_TCP_NODELAY = "socket.tcp_nodelay";

    /**
     * 心跳包的机制
     */
    public static final String SOCKET_SO_KEEPALIVE = "socket.so_keepalive";

    /**
     * 重用处于TIME_WAIT但是未完全关闭的socket地址
     */
    public static final String SOCKET_SO_REUSEADDR = "socket.so_reuseaddr";

    /**
     * Netty接收缓冲区
     */
    public static final String SOCKET_RCV_BUF = "socket.rcv_buf";

    /**
     * Netty发送缓冲区
     */
    public static final String SOCKET_SND_BUF = "socket.snd_buf";

    /**
     * 输出日志
     */
    public static final String SOCKET_LOGGER_OUTPUT = "socket.logger_output";

    /**
     * 输入日志
     */
    public static final String SOCKET_LOGGER_INPUT = "socket.logger_input";

    /**
     *  心跳时间
     */
    public static final String SOCKET_CLIENT_HEART_TIME = "socket.heart_time";

    /**
     * 重连时间
     */
    public static final String SOCKET_CLIENT_RECONN_TIME = "socket.reconn_time";

    /**
     * 重连时间
     */
    public static final String SOCKET_CORE_SIZE = "socket.core_size";

    /**
     * 后端是否开启多线程支持
     */
    public static final String SOCKET_MULTI_THREADS = "socket.multi_threads";
    
    /**
     * 是否打开测量工具，true：打开；false：关闭
     */
    public static final String SOCKET_METRIC = "socket.metric";
}
