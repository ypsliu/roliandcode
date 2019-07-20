package com.roiland.platform.rest.resources;

import com.roiland.platform.rest.Response;
import com.roiland.platform.rest.util.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by user on 2015/11/17.
 */
@Path("test")
@Produces(Constants.APPLICATION_JSON_UTF_8)
public class TestServiceImpl {

    private static final Log LOGGER = LogFactory.getLog(TestServiceImpl.class);

    @GET
    @Path("get")
    public Map<String, Object> testGet(@QueryParam("app_id") String appId, @QueryParam("request_uri") String request_uri, @QueryParam("access_token") String accessToken, @QueryParam("fields") String fields, @QueryParam("request") String request) {
        return Response.ok("resp" + appId + "," + request_uri + "," + accessToken + "," + fields + "," + request);
    }

    @PUT
    @Path("put")
    public Map<String, Object> testPut(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        System.out.println(appId);
        System.out.println(request_uri);
        System.out.println(accessToken);
        System.out.println(Arrays.deepToString(fields));
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @PUT
    @Path("putBean")
    public Map<String, Object> testBean(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, Bean bean) {
        System.out.println(appId);
        System.out.println(request_uri);
        System.out.println(accessToken);
        System.out.println(Arrays.deepToString(bean.getFields()));
        System.out.println(bean.getVersion());
        System.out.println(bean.getAppId());
        return Response.ok("resp" + Arrays.deepToString(bean.getFields()));
    }

    @POST
    @Path("postBean")
    public Map<String, Object> testPostBean(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, Bean bean) {
        System.out.println(appId);
        System.out.println(request_uri);
        System.out.println(accessToken);
        System.out.println(Arrays.deepToString(bean.getFields()));
        System.out.println(bean.getVersion());
        return Response.ok("resp" + Arrays.deepToString(bean.getFields()));
    }

    @POST
    @Path("string0")
    public Map<String, Object> testString0(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string2")
    public Map<String, Object> testString2(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string3")
    public Map<String, Object> testString3(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string4")
    public Map<String, Object> testString4(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string5")
    public Map<String, Object> testString5(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string6")
    public Map<String, Object> testString6(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string7")
    public Map<String, Object> testString7(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string8")
    public Map<String, Object> testString8(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string9")
    public Map<String, Object> testString9(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string10")
    public Map<String, Object> testString10(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string11")
    public Map<String, Object> testString11(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string12")
    public Map<String, Object> testString12(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string13")
    public Map<String, Object> testString13(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string14")
    public Map<String, Object> testString14(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string15")
    public Map<String, Object> testString15(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string16")
    public Map<String, Object> testString16(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string17")
    public Map<String, Object> testString17(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string18")
    public Map<String, Object> testString18(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string19")
    public Map<String, Object> testString19(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string20")
    public Map<String, Object> testString20(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string21")
    public Map<String, Object> testString21(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string22")
    public Map<String, Object> testString22(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string23")
    public Map<String, Object> testString23(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string24")
    public Map<String, Object> testString24(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string25")
    public Map<String, Object> testString25(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string26")
    public Map<String, Object> testString26(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string27")
    public Map<String, Object> testString27(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string28")
    public Map<String, Object> testString28(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string29")
    public Map<String, Object> testString29(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string30")
    public Map<String, Object> testString30(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string31")
    public Map<String, Object> testString31(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string32")
    public Map<String, Object> testString32(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string33")
    public Map<String, Object> testString33(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string34")
    public Map<String, Object> testString34(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string35")
    public Map<String, Object> testString35(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string36")
    public Map<String, Object> testString36(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string37")
    public Map<String, Object> testString37(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string38")
    public Map<String, Object> testString38(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string39")
    public Map<String, Object> testString39(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string40")
    public Map<String, Object> testString40(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string41")
    public Map<String, Object> testString41(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string42")
    public Map<String, Object> testString42(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string43")
    public Map<String, Object> testString43(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string44")
    public Map<String, Object> testString44(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string45")
    public Map<String, Object> testString45(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string46")
    public Map<String, Object> testString46(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string47")
    public Map<String, Object> testString47(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string48")
    public Map<String, Object> testString48(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string49")
    public Map<String, Object> testString49(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string50")
    public Map<String, Object> testString50(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string51")
    public Map<String, Object> testString51(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string52")
    public Map<String, Object> testString52(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string53")
    public Map<String, Object> testString53(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string54")
    public Map<String, Object> testString54(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string55")
    public Map<String, Object> testString55(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string56")
    public Map<String, Object> testString56(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string57")
    public Map<String, Object> testString57(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string58")
    public Map<String, Object> testString58(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string59")
    public Map<String, Object> testString59(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string60")
    public Map<String, Object> testString60(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string61")
    public Map<String, Object> testString61(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string62")
    public Map<String, Object> testString62(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string63")
    public Map<String, Object> testString63(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string64")
    public Map<String, Object> testString64(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string65")
    public Map<String, Object> testString65(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string66")
    public Map<String, Object> testString66(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string67")
    public Map<String, Object> testString67(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string68")
    public Map<String, Object> testString68(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string69")
    public Map<String, Object> testString69(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string70")
    public Map<String, Object> testString70(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string71")
    public Map<String, Object> testString71(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string72")
    public Map<String, Object> testString72(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string73")
    public Map<String, Object> testString73(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string74")
    public Map<String, Object> testString74(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string75")
    public Map<String, Object> testString75(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string76")
    public Map<String, Object> testString76(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string77")
    public Map<String, Object> testString77(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string78")
    public Map<String, Object> testString78(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string79")
    public Map<String, Object> testString79(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string80")
    public Map<String, Object> testString80(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string81")
    public Map<String, Object> testString81(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string82")
    public Map<String, Object> testString82(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string83")
    public Map<String, Object> testString83(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string84")
    public Map<String, Object> testString84(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string85")
    public Map<String, Object> testString85(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string86")
    public Map<String, Object> testString86(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string87")
    public Map<String, Object> testString87(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string88")
    public Map<String, Object> testString88(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string89")
    public Map<String, Object> testString89(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string90")
    public Map<String, Object> testString90(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string91")
    public Map<String, Object> testString91(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string92")
    public Map<String, Object> testString92(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string93")
    public Map<String, Object> testString93(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string94")
    public Map<String, Object> testString94(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string95")
    public Map<String, Object> testString95(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string96")
    public Map<String, Object> testString96(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string97")
    public Map<String, Object> testString97(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string98")
    public Map<String, Object> testString98(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }

    @POST
    @Path("string99")
    public Map<String, Object> testString99(@FormParam("app_id") String appId, @FormParam("request_uri") String request_uri, @FormParam("access_token") String accessToken, @FormParam("fields") String[] fields, @FormParam("request") String[] request) {
        return Response.ok("resp" + Arrays.deepToString(request));
    }
}

