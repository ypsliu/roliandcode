package com.roiland.platform.restful.resources;

import com.roiland.platform.restful.chain.FilterChain;
import com.roiland.platform.restful.request.IHttpRequest;
import com.roiland.platform.spi.annotation.Extension;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
@Extension("filter")
public class LogFilter extends FilterChain {

    @Override
    protected void doFilter(IHttpRequest request) {
        System.out.println("日志打印");
        request.setParameter("user", "jeffy.yang");
    }
}
