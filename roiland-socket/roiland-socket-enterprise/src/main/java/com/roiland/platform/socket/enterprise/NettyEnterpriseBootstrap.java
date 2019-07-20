package com.roiland.platform.socket.enterprise;

import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.*;
import com.roiland.platform.socket.core.handler.RoilandChannelInitializer;
import com.roiland.platform.socket.core.task.HeartTimeTask;
import com.roiland.platform.socket.core.task.RetryTimeTask;
import com.roiland.platform.socket.enterprise.handler.RoilandLoginSASLHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jeffy.yang on 16-6-17.
 */
public final class NettyEnterpriseBootstrap extends RoilandSocketBootstrap implements ISocketConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyEnterpriseBootstrap.class);

    private static NettyEnterpriseBootstrap instance = null;

    private final Bootstrap bootstrap;
    private final EventLoopGroup workLoopGroup;

    private String username;
    private String password;
    private IDataCallback callback;

    public NettyEnterpriseBootstrap(IDataHandler handler) {
        super(handler);
        this.workLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }

    public static NettyEnterpriseBootstrap with(IDataHandler handler) {
        if (instance == null) {
            instance = new NettyEnterpriseBootstrap(handler);
        }
        return instance;
    }

    public NettyEnterpriseBootstrap login(String username, String password, IDataCallback callback) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("`username` or `password` is empty.");
        }

        this.username = username;
        this.password = password;
        this.callback = callback;
        LOGGER.info("[{}]用户名 => {}", super.MSG, username);
        LOGGER.info("[{}]密码 => {}", super.MSG, password);
        LOGGER.info("[{}]登录回调 => {}", super.MSG, callback == null ? "" : callback.getClass().getName());
        return this;
    }

    public NettyEnterpriseBootstrap build() {
        this.bootstrap.group(workLoopGroup);
        this.bootstrap.channel(super.NIO_SOCKET_CHANNEL);
        this.bootstrap.option(ChannelOption.TCP_NODELAY, super.TCP_NODELAY);
        this.bootstrap.option(ChannelOption.SO_KEEPALIVE, super.SO_KEEPALIVE);
        this.bootstrap.option(ChannelOption.SO_REUSEADDR, super.SO_REUSEADDR);
        this.bootstrap.option(ChannelOption.SO_RCVBUF, super.SO_RCVBUF);
        this.bootstrap.option(ChannelOption.SO_SNDBUF, super.SO_SNDBUF);
        this.bootstrap.option(ChannelOption.ALLOCATOR, super.ALLOCATOR);
        this.bootstrap.handler(new RoilandChannelInitializer(
                handler,
                new RoilandLoginSASLHandler(this.username, this.password, this.callback)
        ));

        super.add(new HeartTimeTask(iHeartPeriod));
        super.add(new RetryTimeTask(this, iRetryPeriod));
        return this;
    }

    public ChannelFuture connect(final String group, final String host, final Integer port) {
        return bootstrap.connect(host, port).syncUninterruptibly().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                final String address = host + ":" + port;
                if (!RoilandChannelManager.getInstance().contains(address)) {
                    RoilandChannelManager.getInstance().add(group, address);
                }
                LOGGER.info("{} 通道创建{}", future.channel(), future.cause() == null ? "成功" : "失败", future.cause());
            }
        });
    }

    public Future shutdown() {
        super.close();
        return workLoopGroup.shutdownGracefully();
    }
}
