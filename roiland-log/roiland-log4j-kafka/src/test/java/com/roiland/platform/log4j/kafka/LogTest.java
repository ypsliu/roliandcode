package com.roiland.platform.log4j.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author leon.chen
 * @since 2016/7/5
 */
public class LogTest {
    private static final Log LOGGER = LogFactory.getLog(LogTest.class);
    public void test(){
        int i=0;
        while(i<100){
            LOGGER.info("this index is "+i);
            LOGGER.error("error",new Exception("error index is "+i));
            i++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
