package com.roiland.platform.restful.request.filter;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
public class AllowCorsFilter extends CorsFilter {

    public AllowCorsFilter() {
        super.allowedOrigins.add("*");
    }
}
