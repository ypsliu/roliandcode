package com.roiland.platform.examples.netty;

import com.roiland.platform.socket.core.*;
import com.roiland.platform.zookeeper.RoilandProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by jeffy.yang on 16-6-15.
 */
public class NettyServerBootstrap {

    private static final Log LOGGER = LogFactory.getLog(NettyServerBootstrap.class);

    public static void main(String[] args) {
        Map<String, String> accesses = new HashMap<>();
        accesses.put("12345678901234567890123456789012", "GT01");
        accesses.put("12345678901234567890123456789013", "GT01");

        new RoilandProperties("roiland-examples-netty-server");
        com.roiland.platform.socket.server.NettyServerBootstrap.with(new IDataHandler() {
            @Override
            public void handle(RoilandChannel channel, ProtocolBean protocolBean) {
                channel.writeAndFlush(new DownStream("RG,36,0,E2020R30000063F9E,0,iQAC+w==,21223"));
            }
        }).access(accesses).build().bind(7000);
    }
}
