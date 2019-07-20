package com.roiland.platform.dbutils;

import com.alibaba.druid.pool.DruidDataSource;
import com.roiland.platform.dbutils.bean.DBConnBean;
import com.roiland.platform.dbutils.bean.DataSourceBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.sql.DataSource;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库配置连接管理类
 * 
 * @author jeffy.yang
 *
 */
public class DBConn {

	private static Log LOGGER = LogFactory.getLog(DBConn.class);

	/**
	 * 数据库与连接对应关系
	 */
	private static final Map<String, DataSourceBean> schemas = Collections.synchronizedMap(new HashMap<String, DataSourceBean>());
	private static final Map<String, DruidDataSource> sources = Collections.synchronizedMap(new HashMap<String, DruidDataSource>());

	/**
	 * @param conns 数据库多组连接
	 */
	public static void initialize(List<DBConnBean> conns) {
		for (DBConnBean conn : conns) {
			initialize(conn);
		}
	}

	/**
	 *
	 * @param conn 数据库连接
	 */
	public static void initialize(DBConnBean conn) {
		DruidDataSource druidDataSource;
		if (sources.containsKey(conn.getUrl())) {
			druidDataSource = sources.get(conn.getUrl());
		} else {
			final String username =conn.getUsername();
			final String password = conn.getPassword();
			final String driverClassName = System.getProperty("druid.driver_class_name", "com.mysql.jdbc.Driver");
			final int maxActive = Integer.valueOf(System.getProperty("druid.max_active", "4"));
			final int initialSize = Integer.valueOf(System.getProperty("druid.initial_size", "1"));
			final int maxWait = Integer.valueOf(System.getProperty("druid.max_wait", "10000"));
			final int minIdle = Integer.valueOf(System.getProperty("druid.min_idle", "2"));
			final int timeBetweenEvictionRunsMillis = Integer.valueOf(System.getProperty("druid.time_between_eviction_runs_millis", "60000"));
			final int minEvictableIdleTimeMillis = Integer.valueOf(System.getProperty("druid.min_evictableIdle_time_millis", "300000"));
			final String validationQuery = System.getProperty("druid.validation_query", "SELECT '1';");
			final boolean testWhileIdle = Boolean.valueOf(System.getProperty("druid.test_while_idle", "true"));
			final boolean testOnBorrow = Boolean.valueOf(System.getProperty("druid.test_on_borrow", "false"));
			final boolean testOnReturn = Boolean.valueOf(System.getProperty("druid.test_on_return", "false"));
			final int maxOpenPreparedStatements = Integer.valueOf(System.getProperty("druid.max_open_prepared_statements", "20"));
			final boolean removeAbandoned = Boolean.valueOf(System.getProperty("druid.remove_abandoned", "true"));
			final int removeAbandonedTimeout = Integer.valueOf(System.getProperty("druid.remove_abandoned_timeout", "1800"));
			final boolean logAbandoned = Boolean.valueOf(System.getProperty("druid.log_abandoned", "true"));

			druidDataSource = new DruidDataSource();
			druidDataSource.setUrl(conn.getUrl());
			druidDataSource.setUsername(username);
			druidDataSource.setPassword(password);
			druidDataSource.setDriverClassName(driverClassName);
			druidDataSource.setMaxActive(maxActive);
			druidDataSource.setInitialSize(initialSize);
			druidDataSource.setMaxWait(maxWait);
			druidDataSource.setMinIdle(minIdle);
			druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
			druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
			druidDataSource.setValidationQuery(validationQuery);
			druidDataSource.setTestWhileIdle(testWhileIdle);
			druidDataSource.setTestOnBorrow(testOnBorrow);
			druidDataSource.setTestOnReturn(testOnReturn);
			druidDataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
			druidDataSource.setRemoveAbandoned(removeAbandoned);
			druidDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
			druidDataSource.setLogAbandoned(logAbandoned);
			sources.put(conn.getUrl(), druidDataSource);
		}
		schemas.put(conn.getName(), new DataSourceBean(conn.getName(), conn.getSchema(), druidDataSource));
		LOGGER.info("创建数据库连接，" + conn.toString());
		test(conn.getName());
	}

	/**
	 * 测试数据库是否连接
	 * @param schema 待测试数据库
	 */
	public static void test(String schema) {
		Handle handle = null;
		try {
			handle = handle(schema);
		} finally {
			if (handle != null) handle.close();
		}
	}

	public static Handle handle(String schema) {
        return handle(schema, false);
	}

	public static Handle handle(String schema, boolean isReadOnly) {
		if (schemas.containsKey(schema)) {
			final DataSourceBean dataSourceBean = schemas.get(schema);
			final DataSource dataSource = dataSourceBean.getDataSource();
			try {
				final Handle handle = new DBI(dataSource).open();
				handle.getConnection().setCatalog(dataSourceBean.getSchema());
				handle.getConnection().setReadOnly(isReadOnly);
				return handle;
			} catch (Exception e) {
				LOGGER.error("创建数据库连接失败，数据库 - " + schema, e);
			}
			return null;
		} else {
			LOGGER.error("数据库未被初始化，数据库:" + schema);
			return null;
		}
	}
}
