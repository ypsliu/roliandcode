package com.roiland.platform.rest.util;

import javax.ws.rs.core.MediaType;

/**
 * Created by jeffy.yang on 2015/12/8.
 */
public final class Constants {

    public static final int RESTFUL_PORT = Integer.parseInt(System.getProperty("restful.port", "8080"));

    public static final String RESTFUL_CONTEXT_PATH = System.getProperty("restful.context_path", "/");

    public static final int RESTFUL_EXECUTOR_COUNT = Integer.parseInt(System.getProperty("restful.executor_count", "200"));

    public static final int RESTFUL_WORK_COUNT = Integer.parseInt(System.getProperty("restful.worker_count", "200"));

    public static final String RESTFUL_JDBC_URL = System.getProperty("restful.jdbc.url", "jdbc:mysql://192.168.35.253:3306");

    public static final String RESTFUL_JDBC_USERNAME = System.getProperty("restful.jdbc.username", "roiland");

    public static final String RESTFUL_JDBC_PASSWORD = System.getProperty("restful.jdbc.password", "roiland123!@#");

    public static final String RESTFUL_DB_SCHEMA = System.getProperty("restful.db.schema", "web_rest");

    public static final String APPLICATION = System.getProperty("restful.application", "application1");

    public static final String DAILY_REQUEST_STATISTICS = "daily_request_statistics";

    public static final String DAILY_REQUEST_PREVENT = "daily_request_prevent";

    public static final String APPLICATION_JSON_UTF_8 = MediaType.APPLICATION_JSON + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8";

    public static final int BLACK_WHITE_LIST_SYNC_TASK_DELAY = 60000;

    public static final int BLACK_WHITE_LIST_SYNC_TASK_PERIOD = 60000;

    public static final int STATISTIC_SYNC_TASK_DELAY = 60000;

    public static final int STATISTIC_SYNC_TASK_PERIOD = 60000;

    public static final String AUTH_PARAM_APP_ID = "app_id";

    public static final String AUTH_PARAM_ACCESS_TOKEN = "access_token";

    public static final String AUTH_PARAM_VERSION = "version";

    public static final String AUTH_PARAM_FIELDS = "fields";

    public static final String[] AUTH_PARAMS = new String[]{AUTH_PARAM_APP_ID, AUTH_PARAM_ACCESS_TOKEN, AUTH_PARAM_VERSION, AUTH_PARAM_FIELDS};
}
