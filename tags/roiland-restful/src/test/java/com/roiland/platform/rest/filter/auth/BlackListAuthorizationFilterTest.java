package com.roiland.platform.rest.filter.auth;

import com.roiland.platform.rest.AbstractTest;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

public class BlackListAuthorizationFilterTest extends AbstractTest {

    @Test
    public void testDoFilter() throws Exception {

        AuthorizationSingleton singleton = mock(AuthorizationSingleton.class);
        when(singleton.containsInBlack("/test/string5", "appId-8")).thenReturn(true);
        when(singleton.containsInBlack("/test/string5", "appId-9")).thenReturn(true);
        when(singleton.containsInBlack("/test/string5", "appId-10")).thenReturn(true);
        when(singleton.containsInWhite(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        mockStatic(AuthorizationSingleton.class);
        when(AuthorizationSingleton.class, "instance").thenReturn(singleton);

        String response = post(client, "http://localhost:8080/services/test/string5", request("appId-8", "REJWQJKLJFSLD4J32KJFKSALJK4J", new String[]{"username", "avatar"}, "1.0.0", new String[]{"abc", "bcd"}), MediaType.APPLICATION_JSON_TYPE);
        assertEquals("{\"message\":\"In Request Black List\",\"code\":422}", response);

        response = post(client, "http://localhost:8080/services/test/string5", request("appId-9", "REJWQJKLJFSLD4J32KJFKSALJK4J", new String[]{"username", "avatar"}, "1.0.0", new String[]{"abc", "bcd"}), MediaType.APPLICATION_JSON_TYPE);
        assertEquals("{\"message\":\"In Request Black List\",\"code\":422}", response);

        response = post(client, "http://localhost:8080/services/test/string5", request("appId-10", "REJWQJKLJFSLD4J32KJFKSALJK4J", new String[]{"username", "avatar"}, "1.0.0", new String[]{"abc", "bcd"}), MediaType.APPLICATION_JSON_TYPE);
        assertEquals("{\"message\":\"In Request Black List\",\"code\":422}", response);

        response = post(client, "http://localhost:8080/services/test/string5", request("appId-11", "REJWQJKLJFSLD4J32KJFKSALJK4J", new String[]{"username", "avatar"}, "1.0.0", new String[]{"abc", "bcd"}), MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        assertEquals("{\"data\":\"resp[abc, bcd]\",\"code\":200}", response);
    }


}