package com.roiland.platform.socket.core;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 通道管理
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/08/23
 */
public final class RoilandChannelManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandChannelManager.class);

    private static RoilandChannelManager ourInstance = new RoilandChannelManager();

    public static RoilandChannelManager getInstance() {
        return ourInstance;
    }

    private Map<String, Channel> channels = null;
    private Map<String, String> groups = null;

    private RoilandChannelManager() {
        this.channels = Collections.synchronizedMap(new HashMap<String, Channel>());
        this.groups = Collections.synchronizedMap(new HashMap<String, String>());
    }

    /**
     * 添加分组与地址之间关系
     *
     * @param group   分组
     * @param address 地址
     */
    public void add(String group, String address) {
        if (this.channels.containsKey(address)) {
            LOGGER.debug("[{}] address exists, Address => {}", group, address);
        } else {
            // 地址不存在，建立地址与通道、地址与分组之间关键
            this.channels.put(address, null);
            this.groups.put(address, group == null ? "" : group);
        }
    }

    /**
     * 建立通道连接，如果
     *
     * @param channel
     * @return 通道分组
     */
    public String connect(Channel channel) {
        String address = channel.remoteAddress().toString().substring(1);
        Channel tmp = this.channels.put(address, channel);
        LOGGER.info("{} channel registered", channel.toString());
        if (tmp != null) {
            tmp.close();
            LOGGER.info("{} kick out.", channel.toString());
        }
        return groups.get(address);
    }

    public String disconnect(Channel channel) {
        String address = channel.remoteAddress().toString().substring(1);
        if (this.channels.containsKey(address)) {
            this.channels.put(address, null);
            LOGGER.info("{} channel unregistered", channel.toString());
        }
        return groups.get(address);
    }

    public boolean contains(String address) {
        return this.channels.containsKey(address);
    }

    public boolean contains(Channel channel) {
        return this.channels.containsValue(channel);
    }

    public Map<String, Channel> findAll() {
        return this.channels;
    }

    public String findGroup(String address) {
        return this.groups.get(address);
    }
}
