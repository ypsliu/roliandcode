package com.roiland.platform.examples.netty.protocol;

import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * <p>业务处理行为共通接口</p>
 *
 * @author 杨昆
 * @since 16/8/6
 */
public interface IBehaviour<T extends Object> {

    /**
     * 待处理必要参数验证
     *
     * @param buffer 待处理字节流
     * @param params 已包含参数列表
     */
//    void validate(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params);

    /**
     * 业务处理
     *
     * @param buffer 待处理字节流
     * @param params 已包含参数列表
     * @return 处理后数据
     */
    T done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params);
}
