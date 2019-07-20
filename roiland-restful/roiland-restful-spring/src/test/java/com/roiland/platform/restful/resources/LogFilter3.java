package com.roiland.platform.restful.resources;

import com.roiland.platform.restful.chain.FilterChain;
import com.roiland.platform.restful.request.IHttpRequest;
import com.roiland.platform.spi.annotation.Extension;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2016/5/12
 */
@Extension("filter")
public class LogFilter3 extends FilterChain {

    @Override
    protected void doFilter(IHttpRequest request) {
        System.out.println("日志打印3");
        request.setParameter("user", "kevin.gong");
    }
}
