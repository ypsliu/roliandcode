package com.roiland.platform.metric.core;

import com.roiland.platform.spi.annotation.SPI;

import java.util.List;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/7/22
 */
@SPI
public interface MetricStrategy {

    /**
     * @param data
     */
    void saveMeter(Map<String, Object> data);

    /**
     * @param id
     */
    List<Map<String,Object>> getMeter(String id);
}
