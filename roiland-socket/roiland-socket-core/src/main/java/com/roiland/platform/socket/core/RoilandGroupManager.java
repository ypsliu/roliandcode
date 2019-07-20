package com.roiland.platform.socket.core;

import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.exception.GroupNotFoundException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>通道分组管理</p>
 * <code>
 * RoilandGroupManager manager = RoilandGroupManager.getInstance();
 * </code>
 *
 * @author 杨昆
 * @version 3.0.1
 * @sine 2016/08/23
 */
public final class RoilandGroupManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandGroupManager.class);

    private static RoilandGroupManager ourInstance = new RoilandGroupManager();

    public static RoilandGroupManager getInstance() {
        return ourInstance;
    }

    /**
     * 通道、分组关系
     */
    private final Map<Channel, String> channels;

    /**
     * 分组、通道关系
     */
    private final Map<String, ChannelGroup> groups;

    private RoilandGroupManager() {
        /**
         * 增加线程安全支持 @杨昆 2016/08/23
         */
        this.channels = Collections.synchronizedMap(new HashMap<Channel, String>());
        this.groups = Collections.synchronizedMap(new HashMap<String, ChannelGroup>());
    }

    /**
     * <p>添加分组与通道之间关系</p>
     *
     * @param group   分组
     * @param channel 通道
     */
    public void put(String group, Channel channel) {
        if (this.channels.containsKey(channel)) {
            // 通道重复注册
            LOGGER.warn("{}[{}] channel multiple registration", channel.toString(), group);
            return;
        }

        if (StringUtils.isEmpty(group)) {
            LOGGER.info("{} group undefined, using empty.", channel.toString());
            group = "";
        }

        synchronized (this) {
            if (!this.groups.containsKey(group)) {
                // 分组初始化
                this.groups.put(group, new ChannelGroup(group));
            }

            this.groups.get(group).add(channel);
            this.channels.put(channel, group);
        }
        LOGGER.info("{}[{}] channel registered", channel.toString(), group);
    }

    /**
     * <p>移除通道关系</p>
     *
     * @param channel 通道
     */
    public void remove(Channel channel) {
        synchronized (this) {
            // TODO: 待优化
            if (this.channels.containsKey(channel)) {
                String group = this.channels.remove(channel);
                if (this.groups.containsKey(group)) {
                    ChannelGroup tmpChannels = groups.get(group);
                    tmpChannels.remove(channel);

                    // 如果Hash一致算法节点长度为0时,移除当前分组
                    if (tmpChannels.size() == 0) this.groups.remove(group);
                }
                LOGGER.info("{}[{}] channel unregistered", channel.toString(), group);
            }
        }
    }

    /**
     * 包含当前分组
     *
     * @param group 组名
     * @return true: 包含，false: 不包含
     */
    public Boolean contains(String group) {
        return this.groups.containsKey(group);
    }

    /**
     * <p>根据当前分组及目标端获取通道</p>
     *
     * @param group  分组
     * @param target 目标
     * @return 通道
     * @throws GroupNotFoundException 分组未找到
     */
    public Channel get(String group, String target) throws GroupNotFoundException {
        if (this.channels.containsValue(group)) {
            return this.groups.get(group).get(target);
        } else {
            throw new GroupNotFoundException("[" + group + "] not found");
        }
    }

    /**
     * <p>向默认通道分组下发指令</p>
     *
     * @param stream 下发指令
     * @return 指令下发结果回调
     * @throws GroupNotFoundException 分组未找到
     */
    public static ChannelFuture writeAndFlush(DownStream stream) throws GroupNotFoundException {
        return writeAndFlush("", stream);
    }

    /**
     * <p>根据通道分组下发指令</p>
     *
     * @param group  分组
     * @param stream 下发指令
     * @return 指令下发结果回调
     * @throws GroupNotFoundException 分组未找到
     */
    public static ChannelFuture writeAndFlush(String group, DownStream stream) throws GroupNotFoundException {
        Channel channel = getInstance().get(group, stream.target());
        return new RoilandChannel(channel).writeAndFlush(stream);
    }

    /**
     * 通道分组
     */
    private class ChannelGroup {

        private final String name;
        /**
         * 通道数
         */
        private AtomicInteger size;

        /**
         * 通道列表
         */
        private List<Channel> channels;

        public ChannelGroup(String name) {
            this.name = name;
            this.size = new AtomicInteger(0);
            this.channels = Collections.synchronizedList(new ArrayList<Channel>());
        }

        /**
         * 添加通道
         *
         * @param channel 通道
         */
        public void add(Channel channel) {
            this.channels.add(channel);
            this.size.incrementAndGet();
            LOGGER.debug("{}[{}] added. channel size: {}", channel.toString(), this.name, this.size.get());
        }

        /**
         * 移除通道
         *
         * @param channel 通道
         */
        public void remove(Channel channel) {
            this.channels.remove(channel);
            this.size.decrementAndGet();
            LOGGER.debug("{}[{}] removed. channel size: {}", channel.toString(), this.name, this.size.get());
        }

        /**
         * 获取通道数
         *
         * @return 通道数
         */
        public int size() {
            return this.size.get();
        }

        /**
         * 根据目标端ID，计算下发通道
         *
         * @param target 目标端
         * @return 通道
         */
        public Channel get(String target) {
            return this.channels.get(Math.abs(target.hashCode() % this.size.get()));
        }
    }
}
