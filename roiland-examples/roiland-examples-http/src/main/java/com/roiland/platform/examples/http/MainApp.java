package com.roiland.platform.examples.http;

import com.roiland.platform.monitor.http.HttpMonitorFilter;
import com.roiland.platform.restful.core.RoilandRestBootstrap;
import com.roiland.platform.restful.plugins.filter.FilterChainFactory;
import com.roiland.platform.zookeeper.RoilandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MainApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        new RoilandProperties("roiland-examples-http");

        List<Object> providers = new ArrayList<>();
        providers.add(new HttpMonitorFilter());

        FilterChainFactory factory = new FilterChainFactory(null);
		RoilandRestBootstrap bootstrap = new RoilandRestBootstrap(factory);
		bootstrap.setProviders(providers);
		bootstrap.startup();
    }
}
