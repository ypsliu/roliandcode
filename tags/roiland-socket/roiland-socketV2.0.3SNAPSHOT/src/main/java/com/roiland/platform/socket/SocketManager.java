package com.roiland.platform.socket;

import com.roiland.platform.socket.bean.ChannelBean;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 类名：SocketManager<br/>
 * 作用：通道管理类<br/>
 * 描述：对通道进行绑定、解绑、查找、是否存在操作<br/>
 *
 * @author 杨昆
 * @since 2015/5/13
 */
public class SocketManager {

    private static final Log LOGGER = LogFactory.getLog(SocketManager.class);

    private static SocketManager ourInstance = new SocketManager();

    public static SocketManager getInstance() {
        return ourInstance;
    }

    private Set<ChannelBean> channels = Collections.synchronizedSet(new HashSet<ChannelBean>());

    private SocketManager() {}

    public synchronized boolean add(String uuid, String group, String host, Integer port, Channel channel) {
        ChannelBean socketBean = new ChannelBean(uuid, group, host, port);
        if (channels.contains(socketBean)) {
            LOGGER.info("Socket已被注册，UUID：" + uuid + "，GROUP：" + group + "，HOST：" + host + "，PORT：" + port);
            return false;
        } else {
            DispatcherManager.getInstance().add(group, channel);
            socketBean.setChannel(channel);
            return channels.add(socketBean);
        }
    }

    /**
     * 通道解绑
     * @param channel 通道
     */
    public synchronized boolean remove(Channel channel) {
        LOGGER.info("尝试从Socket管理中心移除通道，Remote：" + channel.remoteAddress() + "，Local：" + channel.localAddress());
        Iterator<ChannelBean> iterator = channels.iterator();
        ChannelBean channelBean = null;
        Channel tmpChannel = null;
        while(iterator.hasNext()) {
            channelBean = iterator.next();
            tmpChannel = channelBean.getChannel();
            if (channel.equals(tmpChannel)) {
                iterator.remove();
                LOGGER.info("从Socket管理中心移除通道成功，Remote：" + channel.remoteAddress() + "，Local：" + channel.localAddress());
                return DispatcherManager.getInstance().remove(channel);
            }
        }
        return false;
    }

    /**
     * 查找当前通道列表
     * @return 通道列表
     */
    public Set<ChannelBean> findAll() {
        return channels;
    }

    /**
     * 通道列表中是否包含该通道
     * @param uuid 客户端标识
     * @param host 主机
     * @param port 端口
     * @return true：包含；false：不包含
     */
    public boolean contains(String uuid, String group, String host, int port) {
        return channels.contains(new ChannelBean(uuid, group, host, port));
    }

    /**
     *  当前通道是否存在
     * @param channel 通道
     * @return true：存在；false：不存在
     */
    public boolean exists(Channel channel) {
        Iterator<ChannelBean> iterator = channels.iterator();
        ChannelBean channelBean = null;
        while(iterator.hasNext()) {
            channelBean = iterator.next();
            if (channel.equals(channelBean.getChannel())) {
                return true;
            }
        }
        return false;
    }
}
