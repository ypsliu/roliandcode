package com.roiland.platform.cache.commands;

import com.roiland.platform.cache.commands.ICommands;

public abstract class CommandBean {

	public abstract ICommands get();
	
	public abstract ICommands get(String key);
}
