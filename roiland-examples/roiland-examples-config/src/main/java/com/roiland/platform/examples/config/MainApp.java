package com.roiland.platform.examples.config;

import com.roiland.platform.zookeeper.RoilandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/9/26
 */
public class MainApp {

    private static Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        RoilandProperties properties = new RoilandProperties("roiland-message-pusher-socket");
        for (String key: properties.stringPropertyNames()) {
            LOGGER.debug("{} => {}", key, properties.getProperty(key));
        }

        String username = System.getProperty("mysql.user-center.username");
        LOGGER.debug("mysql.user-center.username => {}", username);
    }
}
