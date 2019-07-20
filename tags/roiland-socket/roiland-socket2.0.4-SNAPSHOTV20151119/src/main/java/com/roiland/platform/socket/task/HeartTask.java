package com.roiland.platform.socket.task;

import com.roiland.platform.socket.SocketManager;
import com.roiland.platform.socket.bean.ChannelBean;
import com.roiland.platform.socket.bean.DownStreamBean;
import com.roiland.platform.socket.property.SocketProperties;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * 心跳定时类</br>
 * 定时向通道发送心跳包，维护通道连接，默认时长1分钟。
 * @author jeffy
 * @since 2015/5/13
 */
public class HeartTask implements Runnable {

    private static final Log LOGGER = LogFactory.getLog(ClientConnTask.class);

    private int iHeartTime = Integer.valueOf(System.getProperty(SocketProperties.SOCKET_CLIENT_HEART_TIME, "60"));

    @Override
    public void run() {
        LOGGER.info("启动定时心跳任务，每隔" + iHeartTime + "秒检查一次");

        try {
            while (true) {
                Set<ChannelBean> channels = SocketManager.getInstance().findAll();
                for (ChannelBean channelBean : channels) {
                    Channel channel = channelBean.getChannel();
                    if ( channel != null && channel.isActive()) {
                        channel.writeAndFlush(new DownStreamBean("RG,9,0,0,0,0,0"));
                    }
                }
                Thread.sleep(iHeartTime * 1000);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
