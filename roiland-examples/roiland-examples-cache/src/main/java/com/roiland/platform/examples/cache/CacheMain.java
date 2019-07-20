package com.roiland.platform.examples.cache;

import com.roiland.platform.cache.Cache;

import java.util.Map;

/**
 * Created by jeffy.yang on 16-4-28.
 */
public class CacheMain {

    public static void main(String args[]) {
        Cache.getInstance().register(Cache.TYPE.REDIS, "USER", "192.168.35.127", 6379);

        Map<String, String> map = Cache.getInstance().getCmdBySchema("USER").hgetAll("t_dea_info:dea-id0");
        for (Map.Entry<String, String> param: map.entrySet()) {
            System.out.println(param.getKey() + ":" + param.getValue());
        }
    }
}
