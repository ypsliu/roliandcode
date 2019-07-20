package com.roiland.platform.restful.plugins.signature.version;

import com.roiland.platform.restful.request.IHttpRequest;

import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/3/2
 */
public interface ISignatureHandler {

    void signature(final IHttpRequest request,final Map<String, String> values);

    String getAccessToken(final IHttpRequest request);

    String getPrefix();
}
