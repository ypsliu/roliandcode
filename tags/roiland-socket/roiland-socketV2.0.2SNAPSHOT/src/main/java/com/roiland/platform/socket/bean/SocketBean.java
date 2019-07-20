package com.roiland.platform.socket.bean;

/**
 * 类名：SocketBean<br/>
 * 作用：Socket基本属性Bean。包含通道当前注册ID（UUID），分组，主机地址，端口等基础信息。<br/>
 * 
 * @author jeffy
 * @since 2015/5/13
 */
public abstract class SocketBean {

	/** 通道标识 */
    private String uuid;
	/** 目标端分组 */
    private String group;
	/** 主机地址 */
    private String host;
	/** 主机端口 */
    private int port;

    /**
     *
     * @param uuid 通道标识
     * @param group 目标端分组
     * @param host 主机地址
     * @param port 主机端口
     */
    public SocketBean(String uuid, String group, String host, int port) {
        this.uuid = uuid;
        this.group = group;
        this.host = host;
        this.port = port;
    }

    /**
     * 获取通道标识
     * @return 通道标识
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置通道标识
     * @param uuid 通道标识
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 获取目标端分组
     * @return 目标端分组
     */
    public String getGroup() {
        return group;
    }

    /**
     * 设置目标端分组
     * @param group 目标端分组
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * 获取主机地址
     * @return 主机地址
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置主机地址
     * @param host 主机地址
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取主机端口
     * @return 主机端口
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置主机端口
     * @param port 主机端口
     */
    public void setPort(int port) {
        this.port = port;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		SocketBean other = (SocketBean) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

}
