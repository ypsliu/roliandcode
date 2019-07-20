package com.roiland.platform.log4j.basic;

import com.roiland.platform.log4j.basic.bean.ConfigBean;
import com.roiland.platform.zookeeper.RoilandProperties;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;

public class Log4j2BasicProviderTest extends TestCase {

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
    public void test() throws InterruptedException {
        RoilandProperties properties = new RoilandProperties("AAAA");
        Assert.assertEquals("roiland123!@#", properties.getProperty("mysql.user-center.password"));
        Set<String> keys = System.getProperties().stringPropertyNames();
        for(String key : keys){
            System.out.println(key + "=" + System.getProperty(key));
        }
    }
}