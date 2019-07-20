package com.roiland.platform.socket.core;

import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.monitor.basic.extract.Meter;
import com.roiland.platform.monitor.basic.transform.InfoBean;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jeffy.yang on 16-6-16.
 */
public class RoilandChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandChannel.class);

    private final String PROJECT_NAME;
    private final String ADDRESS;
    private final String LOCATION;
    private final Boolean NEED_MONITOR;

    private final Channel channel;

    public RoilandChannel(Channel channel) {
        this.channel = channel;

        this.PROJECT_NAME = System.getProperty(Constants.CONFIG_PROJECT_NAME);
        this.ADDRESS = System.getProperty(Constants.CONFIG_LOCAL_ADDRESS);
        this.LOCATION = System.getProperty(Constants.CONFIG_DEPLOY_LOCATION);

        String monitor = System.getProperty(Constants.CONFIG_MONITOR);
        this.NEED_MONITOR = StringUtils.isEmpty(monitor) ? Boolean.TRUE : Boolean.valueOf(monitor);
    }

    public ChannelFuture writeAndFlush(final DownStream stream) {
        return channel.writeAndFlush(stream, channel.newPromise().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                final String remote = channel.remoteAddress().toString();
                final String local = channel.localAddress().toString();
                // 监控
                boolean isSuccess = future.cause() == null ? true : false;
                LOGGER.info("{}[{}] {}", channel.toString(), isSuccess ? "SUCCESS" : "FAIL", stream.toString());

                if (NEED_MONITOR) {
                    Meter.getInstance().mark(
                            InfoBean.TYPE.SOCKET
                            , PROJECT_NAME
                            , ADDRESS
                            , Constants.METHODS.OUTPUT.name()
                            , LOCATION
                            , null
                            , remote
                            , local
                            , isSuccess ? 1 : 0, (!isSuccess) ? 1 : 0
                            , 0
                            , stream.toString().length());
                }
            }
        }));
    }
}
