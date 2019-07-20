package com.roiland.platform.rest.filter.auth;

import com.roiland.platform.rest.filter.AbstractFilterChain;
import com.roiland.platform.rest.filter.FilterBean;

import javax.ws.rs.WebApplicationException;

/**
 * Created by user on 2015/12/10.
 */
public class Filter1 extends AbstractFilterChain {
    @Override
    public void doFilter(FilterBean bean) throws WebApplicationException {
        System.out.println("aaaaa");
        chain.doFilter(bean);
        System.out.println("fffff");
    }
}
