package com.roiland.platform.rest.filter.auth;

import com.roiland.platform.rest.filter.AbstractFilterChain;
import com.roiland.platform.rest.filter.FilterBean;
import com.roiland.platform.rest.util.Constants;
import com.roiland.platform.rest.util.MessageEnum;
import com.roiland.platform.rest.util.ResponseFactory;

import javax.ws.rs.WebApplicationException;

/**
 * Created by user on 2015/11/29.
 */
public class BlackListAuthorizationFilter extends AbstractFilterChain {

    @Override
    public void doFilter(FilterBean bean) throws WebApplicationException {
        final String app = bean.getParams().get(Constants.AUTH_PARAM_APP_ID).toString();
        if (AuthorizationSingleton.instance().containsInBlack(bean.getPath(), app)) {
            throw new WebApplicationException(ResponseFactory.error(MessageEnum.IN_BLACK_LIST));
        } else {
            chain.doFilter(bean);
        }
    }
}
