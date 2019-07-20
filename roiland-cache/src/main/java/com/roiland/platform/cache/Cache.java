package com.roiland.platform.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.roiland.platform.cache.commands.CommandBean;
import com.roiland.platform.cache.commands.ICommands;
import com.roiland.platform.cache.commands.redis.RedisCommandBean;

/**
 * Cache管理类，缓存数据库相关调用入口类。</br>
 * <code>
 *		List<CacheBean> caches = new ArrayList<CacheBean>(1024); <br>
 *		caches.add(new CacheBean(host, port, index));<br>
 *		Cache.getInstance().register(caches);<br>
 *		ICommand command = Cache.getInstance().getCmd(equipment);<br>
 *		...
 * </code>
 * </br>
 * getCmd(String key)：根据key值获取缓存数据库连接池，如果key = Null或调用getCmd()方法，默认调用index为0的连接。
 *
 * @author 杨昆
 *
 */
public class Cache {

	private static Cache cache = new Cache();
	
	private final String DEFAULT = "default";
	
	public enum TYPE { REDIS}

	private Map<String, CommandBean> commands = new HashMap<String, CommandBean>();
	
	public static Cache getInstance() {
		return cache;
	}
	
	public void register(String host, Integer port) {
		this.register(TYPE.REDIS, DEFAULT, host, port);
	}

	public void register(List<CacheBean> beans) {
		this.register(TYPE.REDIS, DEFAULT, beans);
	}

	public void register(TYPE type, String schema, String host, Integer port) {
		switch (type) {
		case REDIS:
			commands.put(schema, new RedisCommandBean(new CacheBean(host, port)));
			break;
		}
	}

	public void register(TYPE type, String schema, List<CacheBean> beans) {
		switch (type) {
		case REDIS:
			commands.put(schema, new RedisCommandBean(beans));
			break;
		}
	}

	public Boolean contains(String schema) {
		return commands.containsKey(schema);
	}

	public ICommands getCmd(String key) {
		return commands.get(DEFAULT).get(key);
	}

	public ICommands getCmd() {
		return commands.get(DEFAULT).get();
	}

	public ICommands getCmdBySchema(String schema) {
		return commands.get(schema).get();
	}

	public ICommands getCmdBySchema(String schema, String key) {
		return commands.get(schema).get(key);
	}
}