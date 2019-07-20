package com.roiland.platform.cache.commands.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public abstract class RedisTemplate<T> {

	private static final Log LOGGER = LogFactory.getLog(RedisTemplate.class);
	
	public T using(JedisPool pool) {
		T result = null;
		Jedis jedis = pool.getResource();
		try {
			result = this.command(jedis);
		} catch (Exception e) {
			jedis.close();
			LOGGER.error(e.getMessage());
		} finally {
			pool.returnResourceObject(jedis);
		}
		return result;
	}
	
	protected abstract T command(Jedis jedis);
}
