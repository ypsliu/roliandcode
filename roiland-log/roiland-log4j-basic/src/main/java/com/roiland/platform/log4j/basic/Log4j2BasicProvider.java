package com.roiland.platform.log4j.basic;

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
public class Log4j2BasicProvider implements LogProvider {
    @Override
    public void restart(Properties properties) {
        ((LoggerContext) LogManager.getContext(false)).reconfigure();
    }
}
