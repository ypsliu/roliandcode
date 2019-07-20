package com.roiland.platform.restful.plugins.signature.version;

import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.restful.SystemErrorEnum;
import com.roiland.platform.restful.factory.ResponseFactory;
import com.roiland.platform.restful.plugins.Constant;
import com.roiland.platform.restful.request.IHttpRequest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

/**
 * 兼容之前的个人验证方案
 *
 * @author leon.chen
 * @since 2016/3/2
 */
public class V0_0_1SignatureHandler implements ISignatureHandler {

    @Override
    public void signature(final IHttpRequest request,final Map<String, String> values) {
        // 过期时间校验
        final String expiredTime = values.get(Constant.EXPIRED_TIME);
        if (StringUtils.isNotEmpty(expiredTime) && System.currentTimeMillis() > Long.valueOf(expiredTime)) {
            throw new WebApplicationException(ResponseFactory.error(SystemErrorEnum.CODE_433.getCode(), SystemErrorEnum.CODE_433.getMessage()));
        }
        final MultivaluedMap<String, String> headers = request.getHeaders();
        final String version = headers.getFirst(Constant.VERSION);
        final String accessToken = headers.getFirst(Constant.ACCESS_TOKEN);
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(version)) {
            throw new WebApplicationException(ResponseFactory.error(SystemErrorEnum.CODE_424.getCode(), SystemErrorEnum.CODE_424.getMessage()));
        }
    }

    @Override
    public String getAccessToken(IHttpRequest request) {
        return request.getHeaderString(Constant.ACCESS_TOKEN);
    }

    @Override
    public String getPrefix() {
        return Constant.SESSION;
    }
}
