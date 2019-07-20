package com.roiland.platform.restful.plugins.signature.version;

import com.roiland.platform.restful.plugins.Constant;
import com.roiland.platform.restful.request.IHttpRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * 个人验证
 *
 * @author leon.chen
 * @since 2016/3/2
 */
public class V1_0_0SignatureHandler implements ISignatureHandler {

    private static final Log LOGGER = LogFactory.getLog(V1_0_0SignatureHandler.class);

    @Override
    public void signature(final IHttpRequest request, final Map<String, String> values) {
    }

    @Override
    public String getAccessToken(IHttpRequest request) {
        String authorization = request.getHeaderString("authorization");
        String[] ary = authorization.split(",");
        for (String element : ary) {
            int idx = element.indexOf('=');
            if (idx >= 0) {
                String key = element.substring(0, idx);
                String value = element.substring(idx + 1);
                if (key != null && key.trim().equalsIgnoreCase("uid")) {
                    return value.trim();
                }
            }
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return Constant.SESSION;
    }
}
