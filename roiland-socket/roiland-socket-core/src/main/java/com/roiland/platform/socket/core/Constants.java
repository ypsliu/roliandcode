package com.roiland.platform.socket.core;

/**
 * Created by jeffy on 16/7/26.
 */
public final class Constants {

    /**
     * 日志监控配置(socket.monitor)主键
     */
    public static final String CONFIG_MONITOR = "socket.monitor";

    public static final String CONFIG_RESPONSE = "socket.response";

    public static final String CONFIG_PROJECT_NAME = "project.name";

    public static final String CONFIG_LOCAL_ADDRESS = "local.address";

    public static final String CONFIG_DEPLOY_LOCATION = "deploy.location";

    public static final String CONFIG_SOCKET_THREADS = "socket.multi_threads";

    public static final String CONFIG_SOCKET_RCVBUF = "socket.rcvbuf";

    public static final String CONFIG_SOCKET_SNDBUF = "socket.sndbuf";

    public enum METHODS {INPUT, OUTPUT, BUSINESS}
}
