package com.roiland.platform.socket.core.handler;

import com.roiland.platform.socket.core.Constants;
import com.roiland.platform.socket.core.IDataHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * <p>通道初始化加载器</p>
 *
 * @author 杨昆
 * @since 16/8/22
 */
public class RoilandChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ChannelInboundHandlerAdapter[] adapters;
    private final IDataHandler handler;
    private final EventExecutorGroup eventExecutorGroup;

    public RoilandChannelInitializer(IDataHandler handler, ChannelInboundHandlerAdapter... adapters) {
        this.handler = handler;
        this.adapters = adapters;

        // 多线程设置
        final String MULTI_WORK = System.getProperty(Constants.CONFIG_SOCKET_THREADS);
        if (MULTI_WORK != null) {
            eventExecutorGroup = new DefaultEventExecutorGroup(Integer.valueOf(MULTI_WORK));
        } else {
            eventExecutorGroup = null;
        }
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 解码器
        ch.pipeline().addLast(new RoilandDecoder());

        // 协议封装
        ch.pipeline().addLast(new RoilandProtocolHandler());

        // 回执设置
        ch.pipeline().addLast(new RoilandResponseHandler());

        // 前置处理
        if (this.adapters != null) {
            for (ChannelInboundHandlerAdapter adapter : adapters) {
                ch.pipeline().addLast(adapter);
            }
        }

        // 业务处理
        if (eventExecutorGroup == null) {
            ch.pipeline().addLast(new RoilandDataHandler(handler));
        } else {
            ch.pipeline().addLast(eventExecutorGroup, new RoilandDataHandler(handler));
        }
        // 编码器
        ch.pipeline().addLast(new RoilandEncoder());
    }
}
