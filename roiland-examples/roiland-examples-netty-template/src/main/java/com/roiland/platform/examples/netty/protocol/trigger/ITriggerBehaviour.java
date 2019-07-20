package com.roiland.platform.examples.netty.protocol.trigger;

import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * <p>触发器处理共通接口</p>
 *
 * @author 杨昆
 * @since 16/8/6
 */
public interface ITriggerBehaviour<T extends Object> {

    /**
     * 触发器处理
     *
     * @param uuid 向下传递的业务主键
     * @param params 已包含参数列表
     * @return 处理后数据
     */
    T done(ProtocolBean protocol, String uuid, Map<String, Object> params);
}
