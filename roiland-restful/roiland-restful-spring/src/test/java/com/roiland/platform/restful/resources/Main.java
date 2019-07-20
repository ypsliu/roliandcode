package com.roiland.platform.restful.resources;

import com.roiland.platform.restful.spring.RoilandSpringRestBoot;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by user on 2015/12/10.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"webservice_restful.xml"});
        RoilandSpringRestBoot boot = new RoilandSpringRestBoot(context);
        boot.startup();
    }
}
