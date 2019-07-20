package com.roiland.platform.socket;

import com.roiland.platform.socket.bean.DownStreamBean;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 类名：RoilandCallBack<br/>
 * 作用：楼兰协议建立连接后回调函数<br/>
 *
 * @author jeffy
 * @since 2015/5/21
 */
public class RoilandCallBack implements IConnCallback {

    private static final Log LOGGER = LogFactory.getLog(RoilandCallBack.class);

    /**
     * 建立连接后向服务器端发送鉴权指令
     * @param group 当前分组
     * @param uuid 当前客户端唯一标识
     * @param channel 当前通道
     */
    @Override
    public void call(final String group, final String uuid, Channel channel) {
        if (uuid.length() == 32) {
            channel.writeAndFlush(new DownStreamBean("RG,40," + uuid + ",0,0,0,0"));
        } else {
            LOGGER.warn("注册组件ID位数非法，UUID：" + uuid);
            channel.close();
        }
    }
}
