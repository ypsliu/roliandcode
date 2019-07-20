package com.roiland.platform.zookeeper;

import com.roiland.platform.zookeeper.bean.ConfigBean;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.Set;

public class PropertyPlaceholderConfigurerTest {

    private static final Log LOGGER = LogFactory.getLog(PropertyPlaceholderConfigurerTest.class);

    @Test
    public void testSpring() {
        ApplicationContext context = new ClassPathXmlApplicationContext("common.xml");
        ConfigBean config = context.getBean("config", ConfigBean.class);
        Assert.assertEquals("test", config.getGroup());
        Set<String> keys = System.getProperties().stringPropertyNames();
        for(String key : keys){
            System.out.println(key + "=" + System.getProperty(key));
        }
    }

    @Test
    public void test() throws InterruptedException, UnsupportedEncodingException {
        new RoilandProperties("roiland-log4j2-test");
        Set<String> keys = System.getProperties().stringPropertyNames();
        for (String key : keys) {
            System.out.println(key + "=" + System.getProperty(key));
        }
        LogTest test = new LogTest();
        test.test();
    }
}
