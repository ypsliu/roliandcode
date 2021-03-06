package com.roiland.platform.zookeeper;

import com.roiland.platform.zookeeper.bean.ConfigBean;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;

public class PropertyPlaceholderConfigurerTest {

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
