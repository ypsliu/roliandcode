package com.roiland.platform.examples.http;

import com.roiland.platform.restful.annotation.Resource;
import com.roiland.platform.restful.core.Constants;
import com.roiland.platform.restful.core.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import java.util.Map;

/**
 * Created by user on 2015/11/17.
 */
@Path("/")
@Produces(Constants.APPLICATION_JSON_UTF_8)
@Resource
public class TestServiceImpl {

    private static final Log LOGGER = LogFactory.getLog(TestServiceImpl.class);

    @GET
    @Path("get")
    public String testGet(@HeaderParam(Constants.BID) String bid, @QueryParam("data") String data) {
        return data;
    }

    @POST
    @Path("post")
    public String testPost(@HeaderParam(Constants.BID) String bid, @FormParam("data") String data) {
        return data;
    }

    @GET
    @Path("/map/get")
    public Map<String, Object> testMapGet(@HeaderParam(Constants.BID) String bid, @QueryParam("data") String data) {
        return Response.build().ok(data);
    }

    @POST
    @Path("/map/post")
    public Map<String, Object> testMapPost(@HeaderParam(Constants.BID) String bid, @FormParam("data") String data) {
        return Response.build().ok(data);
    }
}

