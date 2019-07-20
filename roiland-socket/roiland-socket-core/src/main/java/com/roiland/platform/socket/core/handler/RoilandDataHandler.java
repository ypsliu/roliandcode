package com.roiland.platform.socket.core.handler;

import com.roiland.platform.socket.core.IDataHandler;
import com.roiland.platform.socket.core.ProtocolBean;
import com.roiland.platform.socket.core.RoilandChannel;
import com.roiland.platform.socket.core.RoilandGroupManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>业务数据处理方法</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/7/28
 */
public class RoilandDataHandler extends SimpleChannelInboundHandler<ProtocolBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandDataHandler.class);

    private final IDataHandler handler;

    public RoilandDataHandler(IDataHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        // 注销通道
        RoilandGroupManager.getInstance().remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.warn("{} channel exception. {}", ctx.channel(), cause.getMessage(), cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolBean protocol) throws Exception {
        try {
            handler.handle(new RoilandChannel(ctx.channel()), protocol);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
