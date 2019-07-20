package com.roiland.platform.zookeeper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * 初始化配置信息。
 * <p>使用：
 * <p>① 初始化配置信息：{@code RoilandPropertyLoader.getInstance().initialize(project, keys)}
 * <p>② 配置信息调用：{@code RoilandPropertyLoader.getInstance().getProperties()}、 {@code RoilandPropertyLoader.getInstance().getValue(key)}
 *
 * @author 杨昆
 */
public class RoilandProperties extends RoilandBasicProperties {

    private static final long serialVersionUID = 3297984545759109358L;

    private static final Log LOGGER = LogFactory.getLog(RoilandProperties.class);

    public RoilandProperties(String project) {
        this(project, new PropertyListener[]{new LoggerPropertyListener() {
            @Override
            public void onPropertyDone(RoilandBasicProperties properties) {
//                System.setProperty("AsyncLoggerConfig.WaitStrategy", "Yield");
                // 日志配置重启
                ((LoggerContext) LogManager.getContext(false)).reconfigure();
            }
        }});
    }

    public RoilandProperties(String project, PropertyListener[] listeners) {
        super(project, listeners);
    }
}
