package com.roiland.platform.zookeeper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author leon.chen
 * @since 2016/7/18
 */
public class TestLog {
    private static final Log logger = LogFactory.getLog(TestLog.class);
    public void test(){
        logger.info("aaaaaaaaaaa");
        logger.error("error",new RuntimeException("eeeeee"));
    }
}
