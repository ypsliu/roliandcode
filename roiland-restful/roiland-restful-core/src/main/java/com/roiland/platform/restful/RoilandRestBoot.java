package com.roiland.platform.restful;

import com.roiland.platform.restful.chain.ChainSingleton;
import com.roiland.platform.restful.chain.config.FilterConfig;
import com.roiland.platform.restful.chain.config.PathFilterConfig;
import com.roiland.platform.restful.request.filter.AllowCorsFilter;
import com.roiland.platform.restful.request.filter.RestEasyRequestFilter;
import com.roiland.platform.restful.request.interceptor.RestEasyRequestInterceptor;
import com.roiland.platform.restful.response.exception.RestEasyExceptionFilter;
import com.roiland.platform.restful.response.filter.RestEasyLogFilter;
import com.roiland.platform.restful.response.filter.RestEasyResponseFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/11/17
 */
public class RoilandRestBoot {

    private static final Log LOGGER = LogFactory.getLog(RoilandRestBoot.class);

    private NettyJaxrsServer netty;

    public RoilandRestBoot() {
        final String RESTFUL_CONTEXT_PATH = System.getProperty(Constants.RESTFUL_CONTEXT_PATH_KEY, "/");
        final int RESTFUL_PORT = Integer.parseInt(System.getProperty(Constants.RESTFUL_PORT_KEY, "8080"));
        final int RESTFUL_EXECUTOR_COUNT = Integer.parseInt(System.getProperty(Constants.RESTFUL_EXECUTOR_COUNT_KEY, "200"));
        final int RESTFUL_WORK_COUNT = Integer.parseInt(System.getProperty(Constants.RESTFUL_WORK_COUNT_KEY, "200"));
        LOGGER.info(Constants.RESTFUL_CONTEXT_PATH_KEY + ":" + RESTFUL_CONTEXT_PATH);
        LOGGER.info(Constants.RESTFUL_PORT_KEY + ":" + RESTFUL_PORT);
        LOGGER.info(Constants.RESTFUL_EXECUTOR_COUNT_KEY + ":" + RESTFUL_EXECUTOR_COUNT);
        LOGGER.info(Constants.RESTFUL_WORK_COUNT_KEY + ":" + RESTFUL_WORK_COUNT);

        this.netty = new NettyJaxrsServer();
        this.netty.setPort(RESTFUL_PORT);
        this.netty.setRootResourcePath(RESTFUL_CONTEXT_PATH);
        this.netty.setSecurityDomain(null);
        this.netty.setExecutorThreadCount(RESTFUL_EXECUTOR_COUNT);
        this.netty.setIoWorkerCount(RESTFUL_WORK_COUNT);

        //初始化build-in服务
        setProvider(AllowCorsFilter.class);
        setProvider(RestEasyRequestFilter.class);
        setProvider(RestEasyRequestInterceptor.class);
        setProvider(RestEasyExceptionFilter.class);
        setProvider(RestEasyResponseFilter.class);
        setProvider(RestEasyLogFilter.class);
    }

    public void setPathFilter(FilterConfig filterConfig, PathFilterConfig defaultConfig, PathFilterConfig pathConfig){
        ChainSingleton.getInstance().setFilterConfig(filterConfig);
        ChainSingleton.getInstance().setDefaultConfig(defaultConfig);
        ChainSingleton.getInstance().setPathConfig(pathConfig);
    }

    public void setResource(Class clazz) {
        netty.getDeployment().getActualResourceClasses().add(clazz);
    }

    public void setResourceInstance(Object instance){
        netty.getDeployment().getResources().add(instance);
    }

    public void setProvider(Class clazz) {
        netty.getDeployment().getActualProviderClasses().add(clazz);
    }

    public void setProviderInstance(Object instance){
        netty.getDeployment().getProviders().add(instance);
    }

    public void startup() {
        netty.start();
    }

    public void stop() {
        netty.stop();
    }
}
