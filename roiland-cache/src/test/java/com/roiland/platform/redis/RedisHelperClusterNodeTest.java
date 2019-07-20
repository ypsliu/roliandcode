package com.roiland.platform.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;

import com.roiland.platform.cache.Cache;
import com.roiland.platform.cache.CacheBean;

public class RedisHelperClusterNodeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	public static void main(String args[]) throws InterruptedException {
		

		
//		final String KEY_PREFIX = "t_veh_realtime_traffic_";
//		for (int i = 0; i < 1000; i++) {
//			Cache.getInstance().getCmd(String.valueOf(i)).set(KEY_PREFIX + i, String.valueOf(i));
//		}
//
//		for (int i = 0; i < 1000; i++) {
//			int result = Integer.valueOf(Cache.getInstance().getCmd(String.valueOf(i)).get(KEY_PREFIX + i));
//			Assert.assertEquals(i, result);
//		}
//
//		for (int i = 0; i < 1000; i++) {
//			Cache.getInstance().getCmd(String.valueOf(i)).del(KEY_PREFIX + i);
//		}

//		try {
		List<CacheBean> caches = new ArrayList<CacheBean>();
		for (int i = 0; i < 1024; i++) {
			caches.add(new CacheBean("192.168.35.251", 6379, i));
		}
		Cache.getInstance().register(caches);
		
		Map<String, String> params = Cache.getInstance().getCmd("LFV2A2BS0E4658216").hgetAll("traffic_realtime_LFV2A2BS0E4658216");
		
		Thread.sleep(60 * 1000);
		
		for (int i = 0; i < 100; i++) {
			params = Cache.getInstance().getCmd("LFV2A2BS0E4658216").hgetAll("traffic_realtime_LFV2A2BS0E4658216");
			System.out.println(params.size());
		}
		
//		Executor executor = Executors.newFixedThreadPool(1000);
//			for (int i = 0; i < 100; i++) {
//				executor.execute(new Runnable() {
//					@Override
//					public void run() {
//						while (true) {
//							Map<String, String> params = Cache.getInstance().getCmd("LFV2A2BS0E4658216").hgetAll("traffic_realtime_LFV2A2BS0E4658216");
//						}
//					}
//				});
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}
