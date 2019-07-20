package com.roiland.platform.cache.commands.redis;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.roiland.platform.cache.commands.ICommands;

public class RedisAdapter implements ICommands {
	
	private JedisPool pool = null;

	public RedisAdapter(final GenericObjectPoolConfig config, String host, int port) {
		this.pool = new JedisPool(config, host, port);
	}

	@Override
	public String set(final String key, final String value) {
		 return new RedisTemplate<String>() {
			@Override
			protected String command(Jedis jedis) {
				return jedis.set(key, value);
			}
		}.using(pool);
	}

	@Override
	public String get(final String key) {
		 return new RedisTemplate<String>() {
			@Override
			protected String command(Jedis jedis) {
				return jedis.get(key);
			}
		}.using(pool);
	}

	@Override
	public Boolean exists(final String key) {
		 return new RedisTemplate<Boolean>() {
			@Override
			protected Boolean command(Jedis jedis) {
				return jedis.exists(key);
			}
		}.using(pool);
	}

	@Override
	public Long hset(final String key, final String field, final String value) {
		 return new RedisTemplate<Long>() {
			@Override
			protected Long command(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		}.using(pool);
	}

	@Override
	public String hget(final String key, final String field) {
		 return new RedisTemplate<String>() {
			@Override
			protected String command(Jedis jedis) {
				return jedis.hget(key, field);
			}
		}.using(pool);
	}

	@Override
	public String hmset(final String key, final Map<String, String> hash) {
		 return new RedisTemplate<String>() {
			@Override
			protected String command(Jedis jedis) {
				return jedis.hmset(key, hash);
			}
		}.using(pool);
	}

	@Override
	public List<String> hmget(final String key, final String... fields) {
		 return new RedisTemplate<List<String>>() {
			@Override
			protected List<String> command(Jedis jedis) {
				return jedis.hmget(key, fields);
			}
		}.using(pool);
	}

	@Override
	public Boolean hexists(final String key, final String field) {
		 return new RedisTemplate<Boolean>() {
			@Override
			protected Boolean command(Jedis jedis) {
				return jedis.hexists(key, field);
			}
		}.using(pool);
	}

	@Override
	public Long hdel(final String key, final String... field) {
		 return new RedisTemplate<Long>() {
			@Override
			protected Long command(Jedis jedis) {
				return jedis.hdel(key, field);
			}
		}.using(pool);
	}

	@Override
	public Set<String> hkeys(final String key) {
		 return new RedisTemplate<Set<String>>() {
			@Override
			protected Set<String> command(Jedis jedis) {
				return jedis.hkeys(key);
			}
		}.using(pool);
	}

	@Override
	public List<String> hvals(final String key) {
		 return new RedisTemplate<List<String>>() {
			@Override
			protected List<String> command(Jedis jedis) {
				return jedis.hvals(key);
			}
		}.using(pool);
	}

	@Override
	public Map<String, String> hgetAll(final String key) {
		 return new RedisTemplate<Map<String, String>>() {
			@Override
			protected Map<String, String> command(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		}.using(pool);
	}

	@Override
	public Long del(final String key) {
		 return new RedisTemplate<Long>() {
			@Override
			protected Long command(Jedis jedis) {
				return jedis.del(key);
			}
		}.using(pool);
	}

	@Override
	public Long expire(final String key, final int seconds) {
		 return new RedisTemplate<Long>() {
			@Override
			protected Long command(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		}.using(pool);
	}

	@Override
	public String rename(final String oldKey, final String newKey) {
		 return new RedisTemplate<String>() {
			@Override
			protected String command(Jedis jedis) {
				return jedis.rename(oldKey, newKey);
			}
		}.using(pool);
	}

	@Override
	public Long renamenx(final String oldKey, final String newKey) {
		 return new RedisTemplate<Long>() {
			@Override
			protected Long command(Jedis jedis) {
				return jedis.renamenx(oldKey, newKey);
			}
		}.using(pool);
	}

	@Override
	public Long incr(final String key) {
		return new RedisTemplate<Long>() {
			@Override
			protected Long command(Jedis jedis) {
				return jedis.incr(key);
			}
		}.using(pool);
	}

	@Override
	public Long decr(final String key) {
		return new RedisTemplate<Long>() {
			@Override
			protected Long command(Jedis jedis) {
				return jedis.decr(key);
			}
		}.using(pool);
	}

	@Override
	public void close() throws IOException {
		pool.close();
	}
}
