package com.roiland.platform.persist.plugin;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableList;
import com.roiland.platform.metric.core.MetricStrategy;
import com.roiland.platform.spi.annotation.Extension;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author leon.chen
 * @since 2016/7/22
 */
@Extension("metric")
public class MemoryMetricStrategy implements MetricStrategy {

    private static final Log LOGGER = LogFactory.getLog(MemoryMetricStrategy.class);

    private static final Cache<String, EvictingQueue<Map<String, Object>>> meterMap = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(5, TimeUnit.MINUTES).build();

    public MemoryMetricStrategy() {
        LOGGER.info("MemoryMetricStrategy init");
    }

    @Override
    public void saveMeter(Map<String, Object> data) {
        LOGGER.info(data.toString());
        String id = (String) data.get("id");
        try {
            synchronized (meterMap){
                meterMap.get(id, new Callable<EvictingQueue<Map<String, Object>>>() {
                    @Override
                    public EvictingQueue<Map<String, Object>> call() throws Exception {
                        return EvictingQueue.create(1000);
                    }
                }).add(data);
            }
        } catch (ExecutionException e) {
        }
    }

    @Override
    public List<Map<String, Object>> getMeter(String id) {
        synchronized (meterMap){
            EvictingQueue queue = meterMap.getIfPresent(id);
            meterMap.invalidate(id);
            if(queue!=null){
                return ImmutableList.copyOf(queue);
            }
        }
        return null;
    }

}
