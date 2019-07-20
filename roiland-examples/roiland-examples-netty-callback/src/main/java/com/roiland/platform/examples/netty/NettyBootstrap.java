package com.roiland.platform.examples.netty;

import com.roiland.platform.socket.client.NettyClientBootstrap;
import com.roiland.platform.socket.core.*;
import com.roiland.platform.zookeeper.RoilandProperties;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeffy.yang on 16-6-15.
 */
public class NettyBootstrap {

    private static final Log LOGGER = LogFactory.getLog(NettyBootstrap.class);

    private static final String temp = "RG,228,E2020R30000063F9E,0,0,CQAB+wQAjwEBEAMJDSYCF0ATAAAEAExyUAAFAAAAAAAGAAAAAAAHAAFfjwAoAAFfkAAJAAAAAAAUAAD96AAfAACvyAAkAAAD6AAlAAK/IAAQAAAv8wBQAAAnEABXAQBWAAAPoABSAADHOABTAADS8ABVAABtYABwAAAAAABxAAAAAAByAAAAAAARAAATiABDAAAABJWE,34032";

    public static void main(String[] args) throws InterruptedException {
        new RoilandProperties("roiland-examples-netty-client");

        final NettyClientBootstrap bootstrap = NettyClientBootstrap.with(new IDataHandler() {
            @Override
            public void handle(RoilandChannel channel, ProtocolBean protocolBean) {
                 channel.writeAndFlush(new DownStream(temp));
            }
        }).login("12345678901234567890123456789013").retry(1, TimeUnit.SECONDS).heart(60, TimeUnit.SECONDS).build();

        final String hosts = System.getProperty("target.hosts");
        if (hosts == null || hosts.trim().length() == 0) {
            throw new IllegalArgumentException("target hosts is empty.");
        }

        final String[] tmpHosts = hosts.split(";");
        final int TOTAL = Integer.valueOf(System.getProperty("data.total.callback", "5000")) / tmpHosts.length;

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < tmpHosts.length; i++) {
            final String[] ADDRESS = tmpHosts[i].split(":");
            final String HOST = ADDRESS[0];
            final Integer PORT = Integer.valueOf(ADDRESS[1]);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ChannelFuture future = bootstrap.connect("", HOST, PORT);
                    for (int j = 0; j < TOTAL; j++) {
                        new RoilandChannel(future.channel()).writeAndFlush(new DownStream(temp));
                        if (j % 10000 == 0) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }
}
