package com.roiland.platform.template.http;

import com.roiland.platform.template.core.Invoker;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class HttpInvokerTest extends TestCase {

    public void testHandle() throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("host","https://dev-cloudrive-api.roistar.net");
        map.put("content-type","application/json");
        String param1 = FileUtils.readResourceFile("param");
        String template1 = FileUtils.readResourceFile("template");
        try(Invoker invoker = HttpInvoker.create(template1).globalParam(map).build(true)){
            Object object = invoker.invoke(param1,null);
            System.out.println(object);

            object = invoker.invoke(param1,null);
            System.out.println(object);
        }
    }
}