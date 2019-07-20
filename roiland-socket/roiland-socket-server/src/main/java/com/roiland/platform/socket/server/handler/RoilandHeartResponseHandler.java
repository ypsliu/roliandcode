package com.roiland.platform.socket.server.handler;


import com.roiland.platform.socket.core.DownStream;
import com.roiland.platform.socket.core.ProtocolBean;
import com.roiland.platform.socket.core.RoilandChannel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p>服务器端心跳包回执处理</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/07/30
 */
@ChannelHandler.Sharable
public class RoilandHeartResponseHandler extends SimpleChannelInboundHandler<ProtocolBean> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolBean msg) throws Exception {
        if (msg.getLength() == 9) {
            /**
             * 心跳包应答
             */
            new RoilandChannel(ctx.channel()).writeAndFlush(new DownStream("RG,9,0,0,0,0,0"));
        }
        ctx.fireChannelRead(msg);
    }
}
