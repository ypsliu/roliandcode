package com.roiland.platform.rest;

import com.roiland.platform.dbutils.DBConn;
import com.roiland.platform.dbutils.bean.DBConnBean;
import com.roiland.platform.rest.filter.auth.AuthorizationSingleton;
import com.roiland.platform.rest.model.dao.AuthBlackListDao;
import com.roiland.platform.rest.model.dao.AuthWhiteListDao;
import com.roiland.platform.rest.resources.LogFilterImpl;
import com.roiland.platform.rest.resources.TestServiceImpl;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by user on 2015/12/8.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBConn.class, AuthBlackListDao.class, AuthWhiteListDao.class, AuthorizationSingleton.class})
@PowerMockIgnore({"javax.net.ssl.*"})
public class AbstractTest {

    protected static RoilandRestBoot boot;
    protected static Client client;

    @BeforeClass
    public static void before() throws Exception {
        System.setProperty("restful.context_path", "/services");
        PowerMockito.spy(DBConn.class);
        doNothing().when(DBConn.class, "initialize", Mockito.anyListOf(DBConnBean.class));

        AuthBlackListDao authBlackListDao = mock(AuthBlackListDao.class);
        when(authBlackListDao.findAll()).thenReturn(new ArrayList<Map<String, Object>>());
        mockStatic(AuthBlackListDao.class);
        when(AuthBlackListDao.class, "instance").thenReturn(authBlackListDao);

        AuthWhiteListDao authWhiteListDao = mock(AuthWhiteListDao.class);
        when(authWhiteListDao.findAll()).thenReturn(new ArrayList<Map<String, Object>>());
        mockStatic(AuthWhiteListDao.class);
        when(AuthWhiteListDao.class, "instance").thenReturn(authWhiteListDao);

        client = ClientBuilder.newClient();
        boot = new RoilandRestBoot();
        boot.addService(TestServiceImpl.class);
        boot.addProviders(LogFilterImpl.class);
        boot.startup();
    }

    public static <T> String request(String appId, String accessToken, String[] fields, String version, T request) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("app_id", appId);
        map.put("access_token", accessToken);
        map.put("version", version);
        map.put("fields", fields);
        map.put("request", request);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    public static String post(Client client, String url, String request,MediaType mediaType) {
        WebTarget target = client.target(url);
        Response response = null;
        try {
            response = target.request().post(Entity.entity(request, mediaType));
            if (response.getStatus() != 200) {
                fail("请求失败");
            }
            String responseStr = response.readEntity(String.class);
            return responseStr;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    @Test
    public void test() {

    }

    @AfterClass
    public static void after() {
        if (client != null) {
            client.close();
        }
        if (boot != null) {
            boot.stop();
        }
    }
}
