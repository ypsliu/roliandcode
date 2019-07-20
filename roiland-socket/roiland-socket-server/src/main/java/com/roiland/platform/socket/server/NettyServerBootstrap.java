package com.roiland.platform.socket.server;

import com.roiland.platform.socket.core.IDataHandler;
import com.roiland.platform.socket.core.RoilandSocketBootstrap;
import com.roiland.platform.socket.core.handler.RoilandChannelInitializer;
import com.roiland.platform.socket.server.exception.ServiceSignException;
import com.roiland.platform.socket.server.handler.RoilandAccessHandler;
import com.roiland.platform.socket.server.handler.RoilandHeartResponseHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Created by jeffy.yang on 16-6-15.
 */
public final class NettyServerBootstrap extends RoilandSocketBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerBootstrap.class);

    private static NettyServerBootstrap instance = null;

    private final ServerBootstrap bootstrap;
    private final EventLoopGroup objBossGroup;
    private final EventLoopGroup objWorkGroup;

    public NettyServerBootstrap(IDataHandler handler) {
        super(handler);
        this.bootstrap = new ServerBootstrap();
        this.objBossGroup = new NioEventLoopGroup();
        this.objWorkGroup = new NioEventLoopGroup();
    }

    public static NettyServerBootstrap with(IDataHandler handler) {
        if (instance == null) {
            instance = new NettyServerBootstrap(handler);
        }
        return instance;
    }

    public NettyServerBootstrap access(Map<String, String> accesses) {
        if (accesses != null) {
            for (Map.Entry<String, String> entry : accesses.entrySet()) {
                try {
                    RoilandAccessManager.getInstance().add(entry.getKey(), entry.getValue());
                } catch (ServiceSignException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
            }
        }
        return this;
    }

    public NettyServerBootstrap build() {
        // 创建Netty Bootstrap
        bootstrap.group(objBossGroup, objWorkGroup).channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, super.TCP_NODELAY);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, super.SO_KEEPALIVE);
        bootstrap.option(ChannelOption.SO_REUSEADDR, super.SO_REUSEADDR);
        bootstrap.option(ChannelOption.SO_RCVBUF, super.SO_RCVBUF);
        bootstrap.option(ChannelOption.SO_SNDBUF, super.SO_SNDBUF);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childHandler(new RoilandChannelInitializer(
                        handler,
                        new RoilandAccessHandler(),
                        new RoilandHeartResponseHandler())
        );
        return this;
    }

    public ChannelFuture bind(final int PORT) {
        return bootstrap.bind(PORT).syncUninterruptibly().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                LOGGER.info("bind to " + PORT, future.cause());
            }
        });
    }

}
