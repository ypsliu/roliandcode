package com.roiland.platform.monitor.basic.load;

import com.roiland.platform.monitor.basic.transform.TransformCenter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.MediaType;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jeffy.yang on 16-6-30.
 */
public class LoadCenter {

    private static final Log LOGGER = LogFactory.getLog(LoadCenter.class);

    private final String URL = System.getProperty("monitor.url");
    private final Timer timer = new Timer("loader");
    private Client client = null;
    private WebResource resource = null;

    public LoadCenter() {
        ClientConfig defaultClientConfig = new DefaultClientConfig();
        defaultClientConfig.getClasses().add(JacksonJsonProvider.class);
        this.client = Client.create(defaultClientConfig);

        if (URL == null || "".equals(URL.trim())) {
            this.resource = null;
        } else {
            try {
                this.resource = client.resource(URL);
            } catch (Exception e) {
                LOGGER.error("illegal argument 'monitor.url' - " + URL);
                throw new IllegalArgumentException("illegal argument 'monitor.url' - " + URL);
            }
        }

        timer.scheduleAtFixedRate(new LoadTimerTask(), 1000, 1000);
    }

    public void shutdown() {
        client.destroy();
        timer.cancel();
    }

    private class LoadTimerTask extends TimerTask {

        private LoadBean output = null;

        @Override
        public void run() {
            if (output == null) {
                output = TransformCenter.getInstance().poll();
            }

            try {
                while (output != null) {
//                    final String path = new LoadFactory().getPath(output.getType());
                    if (resource != null) {
                        resource.path("/").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(output);
                    } else {
                        LOGGER.info(output.toString());
                    }
                    output = TransformCenter.getInstance().isEmpty()? null: TransformCenter.getInstance().poll();
                }
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }
//
//    private class LoadFactory {
//
//        public String getPath(String type) {
//            String result = null;
//            switch (type) {
//                case "dubbo":
//                    result = "/monitor/dubbo";
//                    break;
//                case "http":
//                    result = "/monitor/http";
//                    break;
//                case "socket":
//                    result = "/monitor/socket";
//                    break;
//            }
//            return result;
//        }
//    }
}
