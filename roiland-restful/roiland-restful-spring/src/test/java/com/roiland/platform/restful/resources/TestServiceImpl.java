package com.roiland.platform.restful.resources;

import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2015/11/17.
 */
@Path("test")
@Produces(Constants.APPLICATION_JSON_UTF_8)
@Resource
public class TestServiceImpl {

    private static final Log LOGGER = LogFactory.getLog(TestServiceImpl.class);

    @GET
    @Path("get")
    public Map<String, Object> testGet(@HeaderParam(Constants.BID) String bid, @QueryParam("user") String user, @QueryParam("app_id") String appId) {
        System.out.println("USER NAME：" + user);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", "OK");
        LOGGER.info(bid);
        return map;
    }

    @POST
    @Path("post")
    public Map<String, Object> testPost(@HeaderParam(Constants.BID) String bid, @FormParam("users[]") String[] users, Map<String, Object> params) {
        if (users != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(users));
        }
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }

    @POST
    @Path("post/bean")
    public Map<String, Object> testPostBean(@HeaderParam(Constants.BID) String bid, UserBean userBean) {
        if (userBean != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(userBean.getUsers()));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }

    @POST
    @Path("post1")
    public Map<String, Object> testPost1(@HeaderParam(Constants.BID) String bid, @FormParam("users[]") String[] users, Map<String, Object> params) {
        if (users != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(users));
        }
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }

    @POST
    @Path("post2")
    public Map<String, Object> testPost2(@HeaderParam(Constants.BID) String bid, @FormParam("users[]") String[] users, Map<String, Object> params) {
        if (users != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(users));
        }
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }

    @POST
    @Path("post3")
    public Map<String, Object> testPost3(@HeaderParam(Constants.BID) String bid, @FormParam("users[]") String[] users, Map<String, Object> params) {
        if (users != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(users));
        }
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }

    @POST
    @Path("post4")
    public Map<String, Object> testPost4(@HeaderParam(Constants.BID) String bid, @FormParam("users[]") String[] users, Map<String, Object> params) {
        if (users != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(users));
        }
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }

    @POST
    @Path("post5")
    public Map<String, Object> testPost5(@HeaderParam(Constants.BID) String bid, @FormParam("users[]") String[] users, Map<String, Object> params) {
        if (users != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(users));
        }
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }

    @POST
    @Path("post6")
    public Map<String, Object> testPost6(@HeaderParam(Constants.BID) String bid, @FormParam("users[]") String[] users, Map<String, Object> params) {
        if (users != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(users));
        }
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }

    @POST
    @Path("post7")
    public Map<String, Object> testPost7(@HeaderParam(Constants.BID) String bid, @FormParam("users[]") String[] users, Map<String, Object> params) {
        if (users != null) {
            System.out.println("USER NAME：" + Arrays.deepToString(users));
        }
        params.put("code", 200);
        params.put("message", "OK");
        return params;
    }
}

