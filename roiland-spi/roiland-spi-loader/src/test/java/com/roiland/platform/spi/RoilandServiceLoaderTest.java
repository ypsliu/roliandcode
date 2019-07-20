package com.roiland.platform.spi;

import com.roiland.platform.filter.IFilter;
import com.roiland.platform.interceptor.IInterceptor;
import junit.framework.TestCase;

import java.util.Map;

public class RoilandServiceLoaderTest extends TestCase {

    public void testGetProviders1() throws Exception {
        Map<String, Class<? extends IInterceptor>> map = RoilandServiceLoader.getProviders("interceptor", IInterceptor.class, this.getClass().getClassLoader());
        assertEquals(3, map.size());
    }

    public void testGetProviders2() throws Exception {
        Map<String, Class<? extends IFilter>> map = RoilandServiceLoader.getProviders("filter", IFilter.class, this.getClass().getClassLoader());
        assertEquals(2, map.size());

        map = RoilandServiceLoader.getProviders("other", IFilter.class, this.getClass().getClassLoader());
        assertEquals(1, map.size());
    }
}