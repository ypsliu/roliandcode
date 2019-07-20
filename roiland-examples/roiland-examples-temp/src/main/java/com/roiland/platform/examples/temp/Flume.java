package com.roiland.platform.examples.temp;

import com.roiland.platform.zookeeper.RoilandProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Flume {

    private static final Log LOGGER = LogFactory.getLog(Flume.class);

    public static void main(String[] args) {
        new RoilandProperties("roiland-examples-netty-client");

        // Send 10 events to the remote Flume agent. That agent should be
        // configured to listen with an AvroSource.

        ExecutorService executor = Executors.newFixedThreadPool(6);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String sampleData = "2016-06-21 15:25:13.617 [nioEventLoopGroup-2-1] INFO c.r.p.s.c.h.RoilandProtocolHandler: - [id: 0x4e65ab09, /192.168.35.83:40942 => /192.168.35.88:7000][03ab405e-1a2c-4346-a7a8-dcf24b3d163b]RG,151,AW9E011000R250013,83eebee7811847a7ad67f2f00d0a325b,0,CQABIAUAPAEFDwYZDic1DwYZDicnAQcBDP7+/v4BAQMDAwMBAv////8BAwAAAP4BBAAAAP4BCwAAAAEBEAAAAP9hug==,32160";
                while(true) {
                    LOGGER.info(sampleData);
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
