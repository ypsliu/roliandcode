package com.roiland.platform.rest;

import com.roiland.platform.dbutils.DBConn;
import com.roiland.platform.dbutils.bean.DBConnBean;
import com.roiland.platform.rest.resteasy.RestEasyExceptionFilter;
import com.roiland.platform.rest.resteasy.RestEasyLoggerFilter;
import com.roiland.platform.rest.resteasy.RestEasyRequestFilter;
import com.roiland.platform.rest.util.Constants;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/11/17.
 */
public class RoilandRestBoot {

    private NettyJaxrsServer netty;

    private final List<Class> services = new ArrayList<>();

    private final List<Class> providers = new ArrayList<>();

    private final List<DBConnBean> dbConnBeans = new ArrayList<>();

    public RoilandRestBoot() {
        //初始化build-in服务,有顺序要求，所以在构造函数中初始化
        addDBConnBean(new DBConnBean(Constants.RESTFUL_DB_SCHEMA, Constants.RESTFUL_JDBC_URL, Constants.RESTFUL_JDBC_USERNAME, Constants.RESTFUL_JDBC_PASSWORD));

        addProviders(RestEasyLoggerFilter.class);
        addProviders(RestEasyRequestFilter.class);
        addProviders(RestEasyExceptionFilter.class);
    }

    public void addService(Class clazz) {
        services.add(clazz);
    }

    public void addProviders(Class clazz) {
        providers.add(clazz);
    }

    public void addDBConnBean(DBConnBean dbConnBean) {
        dbConnBeans.add(dbConnBean);
    }

    public void startup() {

        DBConn.initialize(dbConnBeans);

        netty = new NettyJaxrsServer();
        netty.setPort(Constants.RESTFUL_PORT);
        netty.setRootResourcePath(Constants.RESTFUL_CONTEXT_PATH);
        netty.setSecurityDomain(null);
        netty.setExecutorThreadCount(Constants.RESTFUL_EXECUTOR_COUNT);
        netty.setIoWorkerCount(Constants.RESTFUL_WORK_COUNT);
        if (services != null && services.size() > 0) {
            netty.getDeployment().setActualResourceClasses(services);
        }

        netty.getDeployment().setActualProviderClasses(providers);
        netty.start();
    }

    public void stop() {
        if (netty != null) {
            netty.stop();
        }
    }
}
