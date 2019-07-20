package com.roiland.platform.zookeeper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/6/2
 */
public class LoggerPropertyListener extends PropertyListener.Adaptor {

    private static final Log LOGGER = LogFactory.getLog(LoggerPropertyListener.class);

    @Override
    public void onPropertySet(RoilandProperties properties, String key, String value, String from) {
        LOGGER.info(key + " = " + value + " ,lookup from " + from);
    }

    @Override
    public void onPropertyDone(RoilandProperties properties) {
        Map<String, Class<? extends LogProvider>> providers = properties.providers;
        if (providers.isEmpty()) {
            LOGGER.info("loaded log plugin is empty");
            return;
        }
        if (providers.size() > 1) {
            throw new UnsupportedOperationException("multi log plugin loaded:" + properties.toString());
        }
        //providers.size = 1
        try {
            for (Map.Entry<String, Class<? extends LogProvider>> entry : providers.entrySet()) {
                final String name = entry.getKey();
                final Class<? extends LogProvider> value = entry.getValue();
                LOGGER.info("loaded log plugin name:" + name + ",class:" + value);
                LogProvider logProvider = value.newInstance();
                logProvider.restart(properties);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("log plugin instance error", e);
        }
    }
}
