package com.roiland.platform.cache.commands.redis;

import com.roiland.platform.cache.commands.IBasicCommands;
import com.roiland.platform.cache.commands.ICommands;

import redis.clients.jedis.Jedis;

public class Redis extends Jedis implements IBasicCommands, ICommands {
	
	public Redis(String host, int port) {
		super(host, port);
	}
}
