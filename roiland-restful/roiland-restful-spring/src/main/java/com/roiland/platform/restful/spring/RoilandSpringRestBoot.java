package com.roiland.platform.restful.spring;

import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.RoilandRestBoot;
import com.roiland.platform.restful.annotation.Resource;
import com.roiland.platform.restful.chain.FilterChain;
import com.roiland.platform.restful.chain.config.FilterConfig;
import com.roiland.platform.restful.chain.config.PathFilterConfig;
import com.roiland.platform.restful.spring.config.SpringPathFilterConfig;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.ext.Provider;
import java.util.Collection;
import java.util.Map;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/12/18
 */
public class RoilandSpringRestBoot {
    private RoilandRestBoot boot = new RoilandRestBoot();

    public RoilandSpringRestBoot(ApplicationContext context) {
        Collection<Object> services = context.getBeansWithAnnotation(Resource.class).values();
        Collection<Object> providers = context.getBeansWithAnnotation(Provider.class).values();
        SpringPathFilterConfig configuration = null;
        try {
            configuration = context.getBean(SpringPathFilterConfig.class);
        } catch (NoSuchBeanDefinitionException e) {
            // NOP
        }
        for (Object service : services) {
            boot.setResourceInstance(service);
        }
        for (Object provider : providers) {
            boot.setProviderInstance(provider);
        }
        if (configuration != null) {
            FilterConfig filterConfig = new FilterConfig();
            Map<String, Class<? extends FilterChain>> registerFilters = configuration.getRegisterFilters();
            if (registerFilters != null) {
                for (Map.Entry<String, Class<? extends FilterChain>> entry : registerFilters.entrySet()) {
                    filterConfig.put(entry.getKey(), entry.getValue());
                }
            }

            PathFilterConfig defaultConfig = new PathFilterConfig();
            String defaultFilters = configuration.getDefaultFilters();
            if (defaultFilters != null) {
                defaultConfig.put(Constants.DEFAULT, defaultFilters);
            }

            PathFilterConfig pathConfig = new PathFilterConfig();
            Map<String, String> properties = configuration.getPathFilters();
            if (properties != null) {
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    pathConfig.put(entry.getKey(), entry.getValue());
                }
            }
            boot.setPathFilter(filterConfig, defaultConfig, pathConfig);
        }
    }

    public void startup() {
        boot.startup();
    }

    public void stop() {
        boot.stop();
    }
}
