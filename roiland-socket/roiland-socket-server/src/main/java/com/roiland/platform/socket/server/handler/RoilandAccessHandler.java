package com.roiland.platform.socket.server.handler;

import com.roiland.platform.socket.core.ProtocolBean;
import com.roiland.platform.socket.core.RoilandGroupManager;
import com.roiland.platform.socket.server.RoilandAccessManager;
import com.roiland.platform.socket.server.exception.AuthNotAccessException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>服务器端连接鉴权及认证处理</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/7/28
 */
@ChannelHandler.Sharable
public class RoilandAccessHandler extends SimpleChannelInboundHandler<ProtocolBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandAccessHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        // 通道销毁
        RoilandAccessManager.getInstance().destroy(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolBean data) throws Exception {
        final Channel channel = ctx.channel();
        final String source = data.getSource();
        if (data.getLength() == 40 && source.length() == 32) {
            /**
             * 鉴权协议：RG,40,XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX,0,0,0,0
             */
            try {
                /**
                 * 创建UUID与通道关联关系
                 */
                String group = RoilandAccessManager.getInstance().create(source, channel);
                RoilandGroupManager.getInstance().put(group, channel);
                return;
            } catch (AuthNotAccessException e) {
                /**
                 * 创建失败，即UUID已被使用或Source不在允许列表中时，抛出验证失败异常
                 */
                LOGGER.warn(e.getMessage());
            }
        } else if (RoilandAccessManager.getInstance().access(channel)) {
            /**
             * 通过认证后消息向下传递
             */
            ctx.fireChannelRead(data);
            return;
        } else {
            /**
             * 非鉴权协议并通道无法通过验证
             */
            LOGGER.info("{}[{}] illegal access", channel.toString(), data.getTraceID());
        }
        /**
         * 鉴权失败或通道无法认证，关闭连接。
         */
        ctx.close();
    }
}
