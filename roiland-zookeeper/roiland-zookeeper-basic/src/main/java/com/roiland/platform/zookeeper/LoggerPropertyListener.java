package com.roiland.platform.zookeeper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author leon.chen
 * @since 2016/6/2
 */
public class LoggerPropertyListener extends PropertyListener.Adaptor {

    private static final Log LOGGER = LogFactory.getLog(LoggerPropertyListener.class);

    @Override
    public void onPropertySet(RoilandBasicProperties properties,String key, String value, String from) {
        LOGGER.info(key + " = " + value + " ,lookup from " + from);
    }
}
