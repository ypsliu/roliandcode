package com.roiland.platform.cache.commands.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.JedisPoolConfig;

import com.roiland.platform.cache.CacheBean;
import com.roiland.platform.cache.commands.CacheProperties;
import com.roiland.platform.cache.commands.CommandBean;
import com.roiland.platform.cache.commands.ICommands;

public class RedisCommandBean extends CommandBean {

	private static final Log LOGGER = LogFactory.getLog(RedisCommandBean.class);

	private static JedisPoolConfig config = null;
	
	private Map<String, ICommands> paths = new HashMap<String, ICommands>();
	private Map<Integer, ICommands> commands =new HashMap<Integer, ICommands>();
	
	static {
		config = new JedisPoolConfig();
		config.setLifo(Boolean.valueOf(System.getProperty(CacheProperties.CACHE_POOL_LIFO, "true")));
		config.setMaxTotal(Integer.valueOf(System.getProperty(CacheProperties.CACHE_POOL_MAX_TOTAL, "4")));
		config.setMinIdle(Integer.valueOf(System.getProperty(CacheProperties.CACHE_POOL_MIN_IDLE, "1")));
		config.setMaxIdle(Integer.valueOf(System.getProperty(CacheProperties.CACHE_POOL_MAX_IDLE, "2")));
		config.setMaxWaitMillis(Integer.valueOf(System.getProperty(CacheProperties.CACHE_POOL_MAX_WAIT_MILLIS, "5000")));
		config.setMinEvictableIdleTimeMillis(Integer.valueOf(System.getProperty(CacheProperties.CACHE_POOL_MIN_EVICTABLE_IDLE_MILLIS, "10000")));
		config.setNumTestsPerEvictionRun(Integer.valueOf(System.getProperty(CacheProperties.CACHE_POOL_NUM_TEST_PER_EVICTION_RUN, "100")));
		config.setSoftMinEvictableIdleTimeMillis(Integer.valueOf(System.getProperty(CacheProperties.CACHE_POOL_SOFT_MIN_EVICTABLE_IDLE_MILLIS, "10000")));
		config.setTestOnBorrow(Boolean.valueOf(System.getProperty(CacheProperties.CACHE_POOL_TEST_ON_BORROW, "false")));
		config.setTestOnReturn(Boolean.valueOf(System.getProperty(CacheProperties.CACHE_POOL_TEST_ON_RETURN, "false")));
		config.setTestWhileIdle(Boolean.valueOf(System.getProperty(CacheProperties.CACHE_POOL_TEST_WHILE_IDLE, "false")));
		config.setTimeBetweenEvictionRunsMillis(Integer.valueOf(System.getProperty(CacheProperties.CACHE_POOL_TIME_BETWEEN_EVICTION_RUNS_MILLIS, "5000")));
	
	}
	
	public RedisCommandBean(CacheBean bean) {
		this.initialize(config, bean);
	}
	
	public RedisCommandBean(List<CacheBean> beans) {
		for (CacheBean bean : beans) {
			this.initialize(config, bean);
		}
	}
	
	private void initialize(JedisPoolConfig config, CacheBean bean) {
		String host = bean.getHost();
		Integer port = bean.getPort();
		String path = host + ":" + port;
		Integer index = bean.getIndex();

		ICommands command = null;
		if (paths.containsKey(path)) {
			command = paths.get(path);
		} else {
			command = new RedisAdapter(config, host, port);
			paths.put(path, command);
		}
		commands.put(index, command);
		LOGGER.debug("redis index:" + index + " address:" + path);
	}
	
	public ICommands get() {
		return commands.get(0);
	}
	
	public ICommands get(String key) {
		final int size = commands.size();
		if (size > 0) {
			final Integer index = key == null? 0: Math.abs(key.hashCode() % size);
			if(commands.containsKey(index)) {
				return commands.get(index);
			} else {
				LOGGER.error("Can't find index,  key:" + key + "  index:" + index);
				return commands.get(0);
			}
		} else {
			LOGGER.error("config no register");
			return null;
		}
	}
}
