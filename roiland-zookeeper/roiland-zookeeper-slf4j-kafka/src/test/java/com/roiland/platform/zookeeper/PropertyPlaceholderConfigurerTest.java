package com.roiland.platform.zookeeper;

import org.junit.Test;

public class PropertyPlaceholderConfigurerTest {

    @Test
    public void test() {
        RoilandProperties properties = new RoilandProperties("BBBB");
        TestLog log = new TestLog();
        log.test();
    }
}
