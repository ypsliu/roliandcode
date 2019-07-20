package com.roiland.platform.restful.plugins;

import com.roiland.platform.restful.SystemErrorEnum;
import com.roiland.platform.restful.chain.FilterChain;
import com.roiland.platform.restful.factory.ResponseFactory;
import com.roiland.platform.restful.request.IHttpRequest;
import com.roiland.platform.spi.annotation.Extension;

import javax.ws.rs.WebApplicationException;

/**
 * @author leon.chen
 * @since 2016/6/22
 */
@Extension("filter")
public class VersionCheckFilterChain extends FilterChain {
    @Override
    protected void doFilter(IHttpRequest request) {
        // 选择签名算法版本
        final String version = request.getHeaderString(Constant.VERSION);
        if (version == null || !version.equals(Constant.V_0_0_1)) {
            throw new WebApplicationException(ResponseFactory.error(SystemErrorEnum.CODE_434.getCode(), SystemErrorEnum.CODE_434.getMessage()));
        }
    }
}
