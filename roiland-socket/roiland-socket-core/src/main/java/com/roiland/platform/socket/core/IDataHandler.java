package com.roiland.platform.socket.core;

/**
 * <p>业务处理接口</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/08/23
 */
public interface IDataHandler {

    /**
     * 业务处理方法
     *
     * @param channel      通道
     * @param protocolBean 协议
     */
    void handle(RoilandChannel channel, ProtocolBean protocolBean);
}
