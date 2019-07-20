package com.roiland.platform.restful;

import javax.ws.rs.core.MediaType;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/12/8
 */
public final class Constants {

    //每个请求唯一的业务ID
    public static final String BID = "bid";

    public static final String REQUEST_TIMESTAMP = "request_timestamp";

    public static final String PARAMS = "request_body";

    public static final String DEFAULT = "default";

    public static final String FILTER = "filter";

    public static final String RESTFUL_PORT_KEY = "restful.port";

    public static final String RESTFUL_CONTEXT_PATH_KEY = "restful.context_path";

    public static final String RESTFUL_EXECUTOR_COUNT_KEY = "restful.executor_count";

    public static final String RESTFUL_WORK_COUNT_KEY = "restful.worker_count";

    public static final String APPLICATION_JSON_UTF_8 = MediaType.APPLICATION_JSON + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8";

}
