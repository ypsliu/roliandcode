package com.roiland.platform.examples.netty.protocol.behaviour.db.option.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.bean.CacheKeyBean;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/10
 */
public class CacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

    private static CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    Cache<CacheKeyBean, Object> cache = null;

    private CacheManager() {
        CacheBuilder builder = CacheBuilder.newBuilder();
        builder.expireAfterAccess(5, TimeUnit.SECONDS);
        builder.removalListener(new RemovalListener<Object, Object>() {
            @Override
            public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                LOGGER.info("[{}] removed from cache.", removalNotification.getKey(), removalNotification.getValue());
            }
        });
        this.cache = builder.build();
    }

    public void put(CacheKeyBean key, Object value) {
        this.cache.put(key, value);
    }

    public void putAll(Map<CacheKeyBean, Object> params) {
        this.cache.putAll(params);
    }

    public Object get(CacheKeyBean key) {
        return this.cache.getIfPresent(key);
    }

    public Map<CacheKeyBean, Object> getAll(List<CacheKeyBean> keys) {
        return this.cache.getAllPresent(keys);
    }

}
