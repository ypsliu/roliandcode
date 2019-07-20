package com.roiland.platform.zookeeper.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigBean {

	private static Log LOGGER = LogFactory.getLog(ConfigBean.class);

	private String group;

	public String getGroup() {
		LOGGER.info(group);
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	
}
