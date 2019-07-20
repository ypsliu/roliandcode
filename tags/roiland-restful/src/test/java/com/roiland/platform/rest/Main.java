package com.roiland.platform.rest;

import com.roiland.platform.rest.resources.LogFilterImpl;
import com.roiland.platform.rest.resources.TestServiceImpl;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2015/12/10.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(request("appId-11", "REJWQJKLJFSLD4J32KJFKSALJK4J", new String[]{"username", "avatar"}, "1.0.0", new String[]{"abc", "bcd"}));
        RoilandRestBoot boot = new RoilandRestBoot();
        boot.addService(TestServiceImpl.class);
        boot.addProviders(LogFilterImpl.class);
        boot.startup();
    }

    private static String request(String s, String rejwqjkljfsld4J32KJFKSALJK4J, String[] strings, String s1, String[] strings1) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("app_id", s);
        map.put("access_token", rejwqjkljfsld4J32KJFKSALJK4J);
        map.put("version", s1);
        map.put("fields", strings);
        map.put("request", strings1);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }
}
