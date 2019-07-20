package com.roiland.platform.rest.filter;

import javax.ws.rs.WebApplicationException;
import java.util.Map;

/**
 * Created by user on 2015/11/26.
 */
public interface IFilterChain {

    public void doFilter(FilterBean bean) throws WebApplicationException;
}
