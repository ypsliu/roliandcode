package com.roiland.platform.restful.chain;

import com.roiland.platform.restful.request.IHttpRequest;
import com.roiland.platform.spi.annotation.SPI;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2015/11/26
 */
@SPI
public abstract class FilterChain {

    protected FilterChain chain;

    public void execute(IHttpRequest request) {
        this.doFilter(request);
        if (this.chain != null) {
            this.chain.execute(request);
        }
    }

    /**
     * TODO 如果filter的参数只是request的话，在扩展实现中，需要判断是get请求还是post请求然后才能做相应的处理，这里需要修改吗？
     * @param request
     */
    protected abstract void doFilter(IHttpRequest request);

    public void setChain(FilterChain chain) {
        if (this.chain == null) {
            this.chain = chain;
        } else {
            this.chain.setChain(chain);
        }
    }
}
