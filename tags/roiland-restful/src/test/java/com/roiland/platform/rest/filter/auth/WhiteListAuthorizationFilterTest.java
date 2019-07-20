package com.roiland.platform.rest.filter.auth;

import com.roiland.platform.rest.AbstractTest;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;

import static junit.framework.TestCase.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

public class WhiteListAuthorizationFilterTest extends AbstractTest {

    @Test
    public void testDoFilter() throws Exception {
        AuthorizationSingleton singleton = mock(AuthorizationSingleton.class);

        mockStatic(AuthorizationSingleton.class);
        when(AuthorizationSingleton.class, "instance").thenReturn(singleton);
        when(singleton.containsInWhite(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(singleton.containsInWhite("/test/string2", "appId-8")).thenReturn(false);

        String response = post(client, "http://localhost:8080/services/test/string2", request("appId-8", "REJWQJKLJFSLD4J32KJFKSALJK4J", new String[]{"username", "avatar"}, "1.0.0", new String[]{"abc", "bcd"}), MediaType.APPLICATION_JSON_TYPE);
        assertEquals("{\"message\":\"Not In Request White List\",\"code\":423}", response);

        response = post(client, "http://localhost:8080/services/test/string2", request("appId-9", "REJWQJKLJFSLD4J32KJFKSALJK4J", new String[]{"username", "avatar"}, "1.0.0", new String[]{"abc", "bcd"}), MediaType.APPLICATION_JSON_TYPE);
        assertEquals("{\"data\":\"resp[abc, bcd]\",\"code\":200}", response);

        response = post(client, "http://localhost:8080/services/test/string3", request("appId-8", "REJWQJKLJFSLD4J32KJFKSALJK4J", new String[]{"username", "avatar"}, "1.0.0", new String[]{"abc", "bcd"}), MediaType.APPLICATION_JSON_TYPE);
        assertEquals("{\"data\":\"resp[abc, bcd]\",\"code\":200}", response);
    }
}