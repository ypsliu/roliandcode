package com.roiland.platform.restful.plugins;

import com.roiland.platform.cache.Cache;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.restful.SystemErrorEnum;
import com.roiland.platform.restful.chain.FilterChain;
import com.roiland.platform.restful.factory.ResponseFactory;
import com.roiland.platform.restful.plugins.signature.SignatureFactory;
import com.roiland.platform.restful.plugins.signature.version.ISignatureHandler;
import com.roiland.platform.restful.request.IHttpRequest;
import com.roiland.platform.spi.annotation.Extension;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.WebApplicationException;
import java.util.Map;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 * <p/>
 * 签名认证filter
 *
 * @author leon.chen
 * @see [平台接口服务化鉴权文档.xlsx]
 * @since 2016/1/6
 */
@Extension("filter")
public class SignatureRequestFilterChain extends FilterChain {

    private static final Log LOGGER = LogFactory.getLog(SignatureRequestFilterChain.class);

    private static final String SCHEMA = "user";

    static {
        final String HOST = System.getProperty("redis.user.host");
        final String PORT = System.getProperty("redis.user.port");
        if (StringUtils.isNotEmpty(HOST)) {
            Cache.getInstance().register(Cache.TYPE.REDIS, SCHEMA, HOST, StringUtils.isEmpty(PORT) ? 6379 : Integer.valueOf(PORT));
        }
    }

    @Override
    protected void doFilter(IHttpRequest request) {
        // 选择签名算法版本
        final String version = request.getHeaderString(Constant.VERSION);
        final ISignatureHandler authorization = new SignatureFactory().getSignature(version);
        if (authorization == null) {
            throw new WebApplicationException(ResponseFactory.error(SystemErrorEnum.CODE_434.getCode(), SystemErrorEnum.CODE_434.getMessage()));
        }

        final String accessToken = authorization.getAccessToken(request);

        // 缓存Key存在校验
        final String cacheKey = authorization.getPrefix() + accessToken;

        // 从缓存中获取用户或企业基础数据
        final Map<String, String> values = Cache.getInstance().getCmdBySchema(SCHEMA).hgetAll(cacheKey);
        if (values == null || values.isEmpty()) {
            throw new WebApplicationException(ResponseFactory.error(SystemErrorEnum.CODE_435.getCode(), SystemErrorEnum.CODE_435.getMessage()));
        }

        authorization.signature(request, values);

        //type（企业：1，手机：2，设备：3）
        String type = values.get("client_type");
        if(StringUtils.isEmpty(type)){
            throw new WebApplicationException(ResponseFactory.error(SystemErrorEnum.CODE_436.getCode(), SystemErrorEnum.CODE_436.getMessage()));
        }
        request.setParameter("client_type",type);
        // 基础数据设置
        if (type.equals("2")) {
            //手机
            final String uaid = values.get("uuid");
            request.setParameter("uaid", uaid);
            request.setParameter("src_id", uaid);
            request.setParameter("app_id", values.get("app_id"));
            request.setParameter("mobile", values.get("mobile"));
        } else if (type.equals("1")) {
            //企业
            final String deaId = values.get("uuid");
            request.setParameter("dea_id", deaId);
            request.setParameter("src_id", deaId);
        } else {
            throw new WebApplicationException(ResponseFactory.error(SystemErrorEnum.CODE_430.getCode(), SystemErrorEnum.CODE_430.getMessage()));
        }
    }

}
