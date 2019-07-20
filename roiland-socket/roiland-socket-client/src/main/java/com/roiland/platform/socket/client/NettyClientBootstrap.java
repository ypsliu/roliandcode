package com.roiland.platform.socket.client;

import com.roiland.platform.socket.client.handler.RoilandLoginHandler;
import com.roiland.platform.socket.core.IDataHandler;
import com.roiland.platform.socket.core.ISocketConnection;
import com.roiland.platform.socket.core.RoilandChannelManager;
import com.roiland.platform.socket.core.RoilandSocketBootstrap;
import com.roiland.platform.socket.core.handler.RoilandChannelInitializer;
import com.roiland.platform.socket.core.task.HeartTimeTask;
import com.roiland.platform.socket.core.task.RetryTimeTask;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by jeffy.yang on 16-6-17.
 */
public final class NettyClientBootstrap extends RoilandSocketBootstrap implements ISocketConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientBootstrap.class);

    private static NettyClientBootstrap instance = null;

    private final EventLoopGroup workLoopGroup;
    private final Bootstrap bootstrap;

    private String uuid = null;

    protected NettyClientBootstrap(IDataHandler handler) {
        super(handler);
        this.workLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }


    public static NettyClientBootstrap with(IDataHandler iDataHandler) {
        if (instance == null) {
            instance = new NettyClientBootstrap(iDataHandler);
        }
        return instance;
    }

    public NettyClientBootstrap login(String uuid) {
        this.uuid = uuid;
        if (uuid != null && uuid.length() != 32) {
            throw new IllegalArgumentException("illegal `uuid` size. " + uuid);
        }
        LOGGER.info("[{}]UUID => {}", super.MSG, this.uuid);
        return this;
    }

    public NettyClientBootstrap build() {
        if (this.uuid == null) {
            throw new IllegalArgumentException("undefined `uuid`. please seeing 'login()'");
        }
        this.bootstrap.group(workLoopGroup);
        this.bootstrap.channel(super.NIO_SOCKET_CHANNEL);
        this.bootstrap.option(ChannelOption.TCP_NODELAY, super.TCP_NODELAY);
        this.bootstrap.option(ChannelOption.SO_KEEPALIVE, super.SO_KEEPALIVE);
        this.bootstrap.option(ChannelOption.SO_REUSEADDR, super.SO_REUSEADDR);
        this.bootstrap.option(ChannelOption.SO_RCVBUF, super.SO_RCVBUF);
        this.bootstrap.option(ChannelOption.SO_SNDBUF, super.SO_SNDBUF);
        this.bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        this.bootstrap.handler(new RoilandChannelInitializer(
                this.handler,
                new RoilandLoginHandler(uuid)
        ));

        // 添加定时程序
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
