package com.roiland.platform.zookeeper;

import com.roiland.platform.spi.annotation.SPI;

import java.util.Properties;

/**
 * @author leon.chen
 * @since 2016/8/16
 */
@SPI
public interface LogProvider {
    public void restart(Properties properties);
}
