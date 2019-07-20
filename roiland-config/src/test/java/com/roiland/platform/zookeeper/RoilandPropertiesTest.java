package com.roiland.platform.zookeeper;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoilandPropertiesTest extends TestCase {
    public void testInit() {
        List<String> list = new ArrayList<>();
        for (String key : System.getProperties().stringPropertyNames()) {
            list.add(key);
        }

        RoilandProperties properties = new RoilandProperties("GB07");
        List<String> keys = new ArrayList(System.getProperties().stringPropertyNames());
        Collections.sort(keys);
        for (String key : keys) {
            if (!list.contains(key)) {
                System.out.println(key + "=" + System.getProperty(key));
            }
        }
    }
}