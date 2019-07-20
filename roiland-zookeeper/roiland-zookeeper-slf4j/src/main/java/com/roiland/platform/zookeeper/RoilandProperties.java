package com.roiland.platform.zookeeper;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;

import java.net.URL;

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
                LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
                ContextInitializer initializer = new ContextInitializer(context);
                URL url = initializer.findURLOfDefaultConfigurationFile(true);
                LOGGER.info("加载日志配置文件路径：" + url);

                context.reset();
                context.putProperty(LOG_PATH, System.getProperty(LOG_PATH) + "/" + System.getProperty(PROJECT_NAME));
                context.putProperty(LOG_LEVEL, System.getProperty(LOG_LEVEL));

                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(context);
                try {
                    configurator.doConfigure(url);
                } catch (JoranException e) {
                    LOGGER.error("加载日志配置文件失败", e);
                }
            }
        }});
    }

    public RoilandProperties(String project, PropertyListener[] listeners) {
        super(project, listeners);
    }
}
