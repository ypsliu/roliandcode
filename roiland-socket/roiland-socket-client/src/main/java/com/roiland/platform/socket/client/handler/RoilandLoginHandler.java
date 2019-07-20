package com.roiland.platform.socket.client.handler;


import com.roiland.platform.socket.core.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p>企业客户端登录处理</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2017/07/30
 */
@ChannelHandler.Sharable
public class RoilandLoginHandler extends SimpleChannelInboundHandler<ProtocolBean> {

    private final String USERNAME;

    public RoilandLoginHandler(String username) {
        this.USERNAME = username;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Channel channel = ctx.channel();
        new RoilandChannel(channel).writeAndFlush(new DownStream("RG,40," + USERNAME + ",0,0,0,0"));
        String group = RoilandChannelManager.getInstance().connect(channel);
        RoilandGroupManager.getInstance().put(group, channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        RoilandChannelManager.getInstance().disconnect(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolBean protocol) throws Exception {
        ctx.fireChannelRead(protocol);
    }
}
