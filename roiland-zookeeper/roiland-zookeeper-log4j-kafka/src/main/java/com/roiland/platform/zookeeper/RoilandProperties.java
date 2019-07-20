package com.roiland.platform.zookeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * @author leon.chen
 * @since 2016/7/5
 */
public class RoilandProperties extends RoilandBasicProperties {

    public RoilandProperties(String project) {
        this(project, new PropertyListener[]{new LoggerPropertyListener() {
            @Override
            public void onPropertyDone(RoilandBasicProperties properties) {
                // 日志配置重启
                String clusters = System.getProperty("kafka.cluster");
                if (clusters != null) {
                    System.setProperty("bootstrap.servers", clusters);
                }
                ((LoggerContext) LogManager.getContext(false)).reconfigure();
            }
        }});
    }

    public RoilandProperties(String project, PropertyListener[] listeners) {
        super(project, listeners);
    }
}
