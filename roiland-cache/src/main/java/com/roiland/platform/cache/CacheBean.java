package com.roiland.platform.cache;

/**
 * 缓存数据库配置信息
 * 
 * @author 杨昆
 *
 */
public final class CacheBean {
	
	private String host;						// 主机地址
	private Integer port;						// 主机端口
	private Integer index = 0;				// 分片索引

	/**
	 * 构造器
	 * 
	 * @param host 主机地址
	 * @param port 主机端口
	 */
	public CacheBean(String host, Integer port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * 构造器
	 * @param host 主机地址
	 * @param port 主机端口
	 * @param index 分片索引
	 */
	public CacheBean(String host, Integer port, Integer index) {
		this(host, port);
		this.index = index;
	}

	/**
	 * 获取主机地址
	 * @return 地址信息
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 获取主机端口
	 * @return 端口号
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * 获取分片索引
	 * @return 索引号
	 */
	public Integer getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return "Host:" + host + ", Post:" + port + ", Index:" + index;
	}
}
