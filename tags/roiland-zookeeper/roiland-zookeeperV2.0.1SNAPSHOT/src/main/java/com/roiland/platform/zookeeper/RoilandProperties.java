package com.roiland.platform.zookeeper;

import java.util.Properties;
import java.util.ResourceBundle;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;
import com.roiland.platform.lang.IPUtils;
import com.roiland.platform.lang.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.zookeeper.data.Stat;

/**
 * 单例方法，初始化配置信息。
 * <p>使用：
 * <p>① 初始化配置信息：{@code RoilandPropertyLoader.getInstance().initialize(project, keys)}
 * <p>② 配置信息调用：{@code RoilandPropertyLoader.getInstance().getProperties()}、 {@code RoilandPropertyLoader.getInstance().getValue(key)}
 * 
 * 
 * @author 杨昆
 *
 */
public class RoilandProperties extends Properties {

	private static final long serialVersionUID = -6501904752843141961L;

	public static final char PATH_SEPARATOR = '/';										// zookeeper目录分隔符
	public static final char NAME_SEPARATOR = '.';										// zookeeper名字分隔符
	
	public static final String ZOOKEEPER_URLS = "zookeeper.urls"; 				// zookeeper配置中心的url（必须项，配置在config.properties文件中）
	public static final String DEPLOY_LOCATION = "deploy.location"; 			// 项目部署位置（必须项，配置在config.properties文件中）
	
	public static final String PLATFORM = "platform"; 										// zookeeper根目录
	public static final String PLATFORM_ENVIRONMENT = "environment";		// 环境共通配置
	public static final String PLATFORM_COMMON = "common";						// 全局共通配置
	
	private static final String PROJECT_NAME = "project.name";
	private static final String LOG_PATH = "log.path";
	private static final String LOG_LEVEL = "log.level";

	private static final Log LOGGER = LogFactory.getLog(RoilandProperties.class);
	
	private static final String[] DEFAULT_KEYS = { LOG_PATH, LOG_LEVEL};

	private final static String ip = IPUtils.ip();

	private static CuratorFramework client = null;

	// zookeeper连接
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		for (String key : bundle.keySet()) {
			System.setProperty(key, bundle.getString(key));
		}
		final String zkAddress = System.getProperty(ZOOKEEPER_URLS);

		if (!StringUtils.isEmpty(zkAddress)) {
			client = CuratorFrameworkFactory.builder()
					.connectString(zkAddress)
					.namespace(PLATFORM)
					.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
					.connectionTimeoutMs(60000).build();
			client.start();
		}
	}

	/**
	 * 初始化配置信息
	 * 
	 * @param project 项目名称，如GB01
	 * @param keys 项目定义配置信息Key
	 * @return RoilandPropertyLoader 实例
	 */
	public RoilandProperties(final String project) {
		this.setProperty(PROJECT_NAME, project);
		this.loadProperties(DEFAULT_KEYS);

		// 日志配置重启
		final LoggerContext ctx = (LoggerContext)LogManager.getContext(false);
		ctx.stop();
		ctx.start();
	}
	
	/**
	 * 初始化配置信息
	 * 
	 * @param project 项目名称，如GB01
	 * @param keys 项目定义配置信息Key
	 * @return RoilandPropertyLoader 实例
	 */
	public RoilandProperties(final String project, final String[] keys) {
		this(project);
		this.loadProperties(keys);
	}

	public String setProperty(String key, String value) {
		super.setProperty(key, value);
		return System.setProperty(key, value);
	}

	public String getProperty(String key) {
		return System.getProperty(key);
	}

	/**
	 * 加载属性
	 * @param project 项目名称
	 * @param keys 属性数组
	 */
	private void loadProperties(String[] keys) {
		for(String key : keys) {
			if(System.getProperty(key) == null) {
				final String value = this.getZKProperty(key);
				if(StringUtils.isEmpty(value)) {
					LOGGER.error("获取变量[" + key + "]失败，请检查相关配置");
				} else {
					this.setProperty(key, value);
				}
				LOGGER.info("配置信息加载，" + key + ":" + value);
			}
		}
	}

	/**
	 * 获取Zookeeper属性值，如果zookeeper无值，则返回默认值
	 * @param project 项目名称
	 * @param key 主键
	 * @param defaultValue 默认值
	 * @return
	 */
	private String getZKProperty(String key) {
		final String domain = System.getProperty(DEPLOY_LOCATION);
		final String project = System.getProperty(PROJECT_NAME);
		
		String result = System.getProperty(key);
		if (StringUtils.isEmpty(result)) {                          // 获取当前项目IP下配置信息
			key = key.replace(NAME_SEPARATOR, PATH_SEPARATOR);
			result = getZKValue(PATH_SEPARATOR + project + PATH_SEPARATOR + ip + PATH_SEPARATOR + key);
			LOGGER.debug("尝试从ZK中获取[" + PATH_SEPARATOR + project + PATH_SEPARATOR + ip + PATH_SEPARATOR + key + "=" + result + "]");
		}

		if (StringUtils.isEmpty(result)) {                          // 当前项目下配置信息
			result = getZKValue(PATH_SEPARATOR + project + PATH_SEPARATOR + key);
			LOGGER.debug("尝试从ZK中获取[" + PATH_SEPARATOR + project + PATH_SEPARATOR + key + "=" + result + "]");
		}

		if (StringUtils.isEmpty(result)) {                          // 当前项目下配置信息
			result = getZKValue(PATH_SEPARATOR + PLATFORM_ENVIRONMENT + PATH_SEPARATOR + domain + PATH_SEPARATOR + key);
			LOGGER.debug("尝试从ZK中获取[" + PATH_SEPARATOR + PLATFORM_ENVIRONMENT + PATH_SEPARATOR + domain + PATH_SEPARATOR + key + "=" + result + "]");
		}

		if (StringUtils.isEmpty(result)) {                          // 当前项目下配置信息
			result = getZKValue(PATH_SEPARATOR + PLATFORM_COMMON + PATH_SEPARATOR + key);
			LOGGER.debug("尝试从ZK中获取[" + PATH_SEPARATOR + PLATFORM_COMMON + PATH_SEPARATOR + key + "=" + result + "]");
		}
		return result;
	}

	/**
	 * 根据路径获取zookeeper属性
	 * @param path 路径
	 * @return 值
	 */
	private String getZKValue(String path) {
		try {
			Stat stat = client.checkExists().forPath(path);
			return stat == null? null: new String(client.getData().forPath(path));
		} catch (Exception e) {

		}
		return null;
	}
}
