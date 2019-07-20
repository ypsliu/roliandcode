package com.roiland.platform.rest.filter.auth;

import com.roiland.platform.rest.filter.AbstractFilterChain;
import com.roiland.platform.rest.filter.FilterBean;
import com.roiland.platform.rest.filter.IFilterChain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.WebApplicationException;

/**
 * Created by user on 2015/11/26.
 */
public class StatisticsAuthorizationFilter extends AbstractFilterChain {

    private static final Log LOGGER = LogFactory.getLog(StatisticsAuthorizationFilter.class);

    @Override
    public void doFilter(FilterBean bean) throws WebApplicationException {
//        final String app = params.get("app_id").toString();
//        if (StatisticSingleton.instance().isPrevent(app, path)) {
//            throw new WebApplicationException(ResponseFactory.error(MessageEnum.MAX_ALLOWED_REQUEST));
//        } else {
//            StatisticSingleton.instance().addRequestCount(app, path);
//        }
    }

}
