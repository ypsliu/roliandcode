package com.roiland.platform.restful.plugins.signature;

import com.roiland.platform.restful.plugins.Constant;
import com.roiland.platform.restful.plugins.signature.version.ISignatureHandler;
import com.roiland.platform.restful.plugins.signature.version.V0_0_1SignatureHandler;
import com.roiland.platform.restful.plugins.signature.version.V1_0_0SignatureHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2016/3/14
 */
public class SignatureFactory {

    private static final Map<String, ISignatureHandler> versionMap = new HashMap<>();

    static {
        versionMap.put(Constant.V_0_0_1, new V0_0_1SignatureHandler());
        versionMap.put(Constant.V_1_0_0, new V1_0_0SignatureHandler());
    }

    public ISignatureHandler getSignature(String version) {
        return versionMap.get(version);
    }
}
