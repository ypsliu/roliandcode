package com.roiland.platform.socket.server;

import com.roiland.platform.socket.server.exception.AuthNotAccessException;
import com.roiland.platform.socket.server.exception.ServiceSignException;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>服务器端鉴权认证管理。如果未设置服务标识及分组，允许任何通道接入。</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2017/07/30
 */
public class RoilandAccessManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandAccessManager.class);

    private static RoilandAccessManager ourInstance = new RoilandAccessManager();

    public static RoilandAccessManager getInstance() {
        return ourInstance;
    }

    private Map<String, Channel> sockets = null;
    private Map<String, String> groups = null;

    private RoilandAccessManager() {
        this.sockets = new HashMap<>();
        this.groups = new HashMap<>();
    }

    /**
     * 添加服务标识及其对应分组
     *
     * @param uuid  服务标识
     * @param group 分组
     */
    public synchronized void add(String uuid, String group) throws ServiceSignException {
        if (this.sockets.containsKey(uuid)) {
            throw new ServiceSignException("[" + uuid + "] has registered.");
        } else {
            group = group == null ? "" : group;
            this.sockets.put(uuid, null);
            this.groups.put(uuid, group);
            LOGGER.info("[{}][{}] registered", uuid, group);
        }
    }

    /**
     * 移除服务标识
     *
     * @param uuid 服务标识
     * @throws ServiceSignException 服务标识不存在抛出异常
     */
    public synchronized void remove(String uuid) throws ServiceSignException {
        if (this.sockets.containsKey(uuid)) {
            Channel _channel = this.sockets.remove(uuid);
            if (_channel != null) _channel.close();

            this.groups.remove(uuid);
        } else {
            throw new ServiceSignException("[" + uuid + "] not exist.");
        }
    }

    /**
     * 通道认证
     *
     * @param channel 通道
     * @return true: 认证通过，false: 认证失败
     */
    public boolean access(Channel channel) {
        return this.sockets.size() == 0 || this.sockets.containsValue(channel);
    }

    /**
     * 创建服务标识与通道之间的关系
     *
     * @param uuid    服务标识
     * @param channel 通道
     * @return 服务标识分组
     * @throws AuthNotAccessException 无权限通过
     */
    public synchronized String create(String uuid, Channel channel) throws AuthNotAccessException {
        if (channel == null) {
            throw new AuthNotAccessException("[" + uuid + "] channel empty.");
        }

        if (this.sockets.containsKey(uuid)) {
            /**
             * UUID被注册，
             */
            Channel _channel = this.sockets.put(uuid, channel);
            if (_channel != null) {
                LOGGER.warn("[" + uuid + "] kick out " + _channel.toString());
                _channel.close();
            }

        } else if (!this.sockets.isEmpty()) {
            throw new AuthNotAccessException("[" + uuid + "] not exist.");
        }
        return this.groups.isEmpty() ? "" : this.groups.get(uuid);
    }

    public synchronized void destroy(Channel channel) {
        if (channel == null) {
            return;
        }

        if (this.sockets.containsValue(channel)) {
            for (Map.Entry<String, Channel> _entry : this.sockets.entrySet()) {
                if (channel.equals(_entry.getValue())) {
                    _entry.setValue(null);
                    break;
                }
            }
        }
    }

    public boolean contains(String uuid) {
        return this.sockets.containsKey(uuid);
    }

    public boolean contains(Channel channel) {
        return this.sockets.containsValue(channel);
    }

    public boolean isEmpty() {
        return this.sockets.isEmpty();
    }

}
