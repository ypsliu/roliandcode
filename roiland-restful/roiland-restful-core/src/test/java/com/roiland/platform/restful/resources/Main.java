package com.roiland.platform.restful.resources;

import com.roiland.platform.restful.RoilandRestBoot;
import com.roiland.platform.restful.chain.config.FilterConfig;
import com.roiland.platform.restful.chain.config.PathFilterConfig;

import java.io.IOException;

/**
 * Created by user on 2015/12/10.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FilterConfig filterConfig = new FilterConfig();
        filterConfig.add(LogFilter.class);

        PathFilterConfig defaultConfig = new PathFilterConfig();
        defaultConfig.put("DEFAULT", "logger,LogFilter");

        PathFilterConfig pathConfig = new PathFilterConfig();
        pathConfig.put("/test/get", "DEFAULT, logger,LogFilter");

        RoilandRestBoot boot = new RoilandRestBoot();
        boot.setPathFilter(filterConfig, defaultConfig, pathConfig);
        boot.setResource(TestServiceImpl.class);
        boot.startup();
    }
}
