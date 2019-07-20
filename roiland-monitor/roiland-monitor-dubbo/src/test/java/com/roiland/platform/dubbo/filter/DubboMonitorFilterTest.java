package com.roiland.platform.dubbo.filter;

import com.roiland.platform.dubbo.filter.api.IUserService;
import com.roiland.platform.dubbo.filter.api.IUserService.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboMonitorFilterTest {

    public static void main(String args[]) {
        System.getProperties().setProperty("monitor.url",
                "http://192.168.35.81:8080/");
        ClassPathXmlApplicationContext provider = new ClassPathXmlApplicationContext("generic-provider.xml");
        ClassPathXmlApplicationContext consumer = new
                ClassPathXmlApplicationContext("generic-consumer.xml");

        final int id = 1;
        final String name = "name";
        try {
            while (true) {
                IUserService userservice = (IUserService)
                        consumer.getBean("userservice");
                System.out.println(userservice.get(new User(id, name)).getName());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//		ServiceLoader<Filter> filters = ServiceLoader.load(Filter.class);
//		Iterator<Filter> operationIterator = filters.iterator();
//		System.out.println("classPath:" + System.getProperty("java.class.path"));
//		while (operationIterator.hasNext()) {
//			Filter operation = operationIterator.next();
//			System.out.println(operation.getClass().getName());
//		}
    }
}
