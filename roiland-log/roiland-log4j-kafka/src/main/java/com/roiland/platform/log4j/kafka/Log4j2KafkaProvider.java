package com.roiland.platform.log4j.kafka;

import com.roiland.platform.spi.annotation.Extension;
import com.roiland.platform.zookeeper.LogProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.Properties;

/**
 * @author leon.chen
 * @since 2016/8/16
 */
@Extension("log")
public class Log4j2KafkaProvider implements LogProvider {
    @Override
    public void restart(Properties properties) {
        // 日志配置重启
        ((LoggerContext) LogManager.getContext(false)).reconfigure();
    }
}
