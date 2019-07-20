package com.roiland.platform.monitor.basic.load;

import com.roiland.platform.monitor.basic.transform.InfoBean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class LoadBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -657648728907201527L;
	
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String type;
	private String date;
	private String application;
    private String service;
    private String method;
    private String group;
    private String version;
    private String client;
    private String server;

	private Long success;
	private Long failure;
	private Long input;
	private Long output;
	private Long elapsed;
	private Long concurrent;
	private Long maxInput;
	private Long maxOutput;
	private Long maxElapsed;
	private Long maxConcurrent;
	
	public LoadBean(InfoBean infoBean, Long[] data) {
		this.type = infoBean.getType();
		this.date = format.format(new Date());
		this.application = infoBean.getApplication();
		this.service = infoBean.getService();
		this.method = infoBean.getMethod();
		this.group = infoBean.getGroup();
		this.version = infoBean.getVersion();
		this.client = infoBean.getClient();
		this.server = infoBean.getServer();
		this.success = data[0];
		this.failure = data[1];
		this.input = data[2];
		this.output = data[3];
		this.elapsed = data[4];
		this.concurrent = data[5];
		this.maxInput = data[6];
		this.maxOutput = data[7];
		this.maxElapsed = data[8];
		this.maxConcurrent = data[9];
	}

	public String getType() {
		return type;
	}

	public String getDate() {
		return date;
	}

	public String getApplication() {
		return application;
	}

	public String getService() {
		return service;
	}

	public String getMethod() {
		return method;
	}

	public String getGroup() {
		return group;
	}

	public String getVersion() {
		return version;
	}

	public String getClient() {
		return client;
	}

	public String getServer() {
		return server;
	}

	public Long getSuccess() {
		return success;
	}

	public Long getFailure() {
		return failure;
	}

	public Long getInput() {
		return input;
	}

	public Long getOutput() {
		return output;
	}

	public Long getElapsed() {
		return elapsed;
	}

	public Long getConcurrent() {
		return concurrent;
	}

	public Long getMaxInput() {
		return maxInput;
	}

	public Long getMaxOutput() {
		return maxOutput;
	}

	public Long getMaxElapsed() {
		return maxElapsed;
	}

	public Long getMaxConcurrent() {
		return maxConcurrent;
	}

	@Override
	public String toString() {
		return "LoadBean{" +
				"date='" + date + '\'' +
				", application='" + application + '\'' +
				", service='" + service + '\'' +
				", method='" + method + '\'' +
				", group='" + group + '\'' +
				", version='" + version + '\'' +
				", client='" + client + '\'' +
				", server='" + server + '\'' +
				", success=" + success +
				", failure=" + failure +
				", input=" + input +
				", output=" + output +
				", elapsed=" + elapsed +
				", concurrent=" + concurrent +
				", maxInput=" + maxInput +
				", maxOutput=" + maxOutput +
				", maxElapsed=" + maxElapsed +
				", maxConcurrent=" + maxConcurrent +
				'}';
	}
}
