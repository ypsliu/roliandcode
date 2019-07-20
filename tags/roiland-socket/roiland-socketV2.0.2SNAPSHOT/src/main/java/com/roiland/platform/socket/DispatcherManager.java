package com.roiland.platform.socket;

import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类名：DispatcherManager<br/>
 * 名称：数据分发管理器<br/>
 * 作用：轮询向分组中的通道下发数据<br/>
 * 
 * @author 杨昆
 * @since 2015/6/26
 */
public class DispatcherManager {

    private static final Log LOGGER = LogFactory.getLog(DispatcherManager.class);

    private static DispatcherManager ourInstance = new DispatcherManager();

    private Map<String, LoadBalance> groups = new LinkedHashMap<String, LoadBalance>();

    public static DispatcherManager getInstance() {
        return ourInstance;
    }

    private DispatcherManager() {}

    /**
     * 分组中是否包含该通道
     * @param group 分组
     * @param channel 通道
     * @return true：包含<br/>
     * 				false：不包含
     */
    public boolean contains(String group, Channel channel) {
        if (groups.containsKey(group)) {
            return groups.get(group).contains(channel);
        } else {
            return false;
        }
    }

    /**
     * 添加分组、通道对应关系
     * @param group 分组
     * @param channel 通道
     */
    public boolean add(String group, Channel channel) {
        boolean success = false;
        if (channel != null && channel.isActive()) {
            if ( !groups.containsKey(group) ) {
                groups.put(group, new LoadBalance(group));
            }
            groups.get(group).add(channel);
            success = true;
        } else {
            LOGGER.warn("通道非Active状态，添加失败。");
        }
        return success;
    }

    /**
     * 通道移除
     * @param channel 通道
     * @return true：通道移除成功<br/>
     * 				false：通道移除失败，不包含该通道
     */
    public boolean remove(Channel channel) {
        boolean success = false;
        Collection<LoadBalance> balances = groups.values();
        for (LoadBalance loadBalance : balances) {
            if (loadBalance.contains(channel)) {
//                LOGGER.info("分发中心移除通道，Remote：" + channel.remoteAddress() + "，Local：" + channel.localAddress());
                success = loadBalance.remove(channel);
                break;
            }
        }
        return success;
    }

    /**
     * 向分组通道中写入消息
     * @param group 分组
     * @param downStreamBean 消息
     */
    public void write(final String group, final IDownStream downStreamBean) {
        if (this.groups.containsKey(group)) {
            this.groups.get(group).write(downStreamBean);
        } else {
            LOGGER.error("未包含分组[" + group + "]，指令下发失败。 指令：" + downStreamBean.toString());
        }
    }

    /**
     * 负载均衡简单实现
     */
    private class LoadBalance {

    	/**
    	 * 通道列表
    	 */
        private List<Channel> channels = Collections.synchronizedList(new LinkedList<Channel>());
        /**
         * 通道计数器
         */
        private AtomicInteger counter = new AtomicInteger(0);

        /**
         * 分组名称
         */
        private String name;
        
        /**
         * 构造器
         * @param name 分组名称
         */
        public LoadBalance(String name) {
        	this.name = name;
        }
        
        /**
         * 添加通道
         * @param channel 通道
         * @return true：添加成功<br/> false：添加失败
         */
        public synchronized boolean add(Channel channel) {
            return channels.add(channel);
        }

        /**
         * 移除通道
         * @param channel 通道
         * @return true：移除成功<br/> false：移除失败
         */
        public synchronized boolean remove(Channel channel) {
            return channels.remove(channel);
        }

        /**
         * 是否包含通道
         * @param channel 通道
         * @return true：包含通道<br/> false：不包含该通道
         */
        public boolean contains(Channel channel) {
            return channels.contains(channel);
        }

        /**
         * 根据当前通道数量，轮询向通道中发送数据
         * @param message 消息
         */
        public void write(IDownStream message) {
            if (channels.size() > 0) {
                int curr = counter.getAndIncrement();
                Channel channel = channels.get(Math.abs(curr % channels.size()));
                if (channel.isActive()) {
                    channel.writeAndFlush(message);
                } else {
                    channels.remove(channel);
                    this.write(message);
                }
            } else {
                LOGGER.error("分组[" + name + "]中未包含任何通道，指令下发失败。 指令：" + message.toString());
            }
        }
    }
}
