package com.roiland.platform.rest.filter.auth;

import com.roiland.platform.rest.filter.AbstractFilterChain;
import com.roiland.platform.rest.filter.FilterBean;
import com.roiland.platform.rest.util.Constants;
import com.roiland.platform.rest.util.MessageEnum;
import com.roiland.platform.rest.util.ResponseFactory;

import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2015/12/3.
 */
public class ValidationAuthorizationFilter extends AbstractFilterChain {

    @Override
    public void doFilter(FilterBean bean) throws WebApplicationException {
        if (validateRequest(bean.getParams())) {
            bean.addExtension("request_uri", bean.getPath());
            chain.doFilter(bean);
        } else {
            throw new WebApplicationException(ResponseFactory.error(MessageEnum.BAD_REQUEST));
        }
    }

    private boolean validateRequest(Map<String, Object> node) {
        Object appId = node.get(Constants.AUTH_PARAM_APP_ID);
        Object accessToken = node.get(Constants.AUTH_PARAM_ACCESS_TOKEN);
        Object version = node.get(Constants.AUTH_PARAM_VERSION);
        Object fields = node.get(Constants.AUTH_PARAM_FIELDS);
        if (appId == null || accessToken == null || version == null || fields == null ) {
            return false;
        }
        if (!(appId instanceof String)) {
            return false;
        }
        if (!(accessToken instanceof String)) {
            return false;
        }
        if (!(version instanceof String)) {
            return false;
        }
        if (!(fields instanceof List)) {
            return false;
        }

        return true;
    }
}
