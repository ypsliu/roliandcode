package com.roiland.platform.socket.core.handler;

import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.Constants;
import com.roiland.platform.socket.core.DownStream;
import com.roiland.platform.socket.core.ProtocolBean;
import com.roiland.platform.socket.core.RoilandChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p>协议回执处理</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/07/30
 */
public class RoilandResponseHandler extends SimpleChannelInboundHandler<ProtocolBean> {

    private final Boolean NEED_RESPONSE;

    public RoilandResponseHandler() {
        String response = System.getProperty(Constants.CONFIG_RESPONSE);
        this.NEED_RESPONSE = StringUtils.isEmpty(response) ? Boolean.TRUE : Boolean.valueOf(response);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolBean protocol) throws Exception {
        if (protocol.getLength() == 9) {
            // 过滤心跳包
            return;
        }

        if (NEED_RESPONSE) {
            // 需要回执
            final byte[] body = protocol.getBody();
            if (body[0] == 0x09 && body[1] == 0x00 && body[2] == 0x01) {
                /**
                 * 对上传的OBD数据（0x09,0x00,0x01开头）进行确认应答，返回0x89,0x00,0x02
                 */
                final DownStream temp = new DownStream(protocol.getTarget(), protocol.getSource(), protocol.getType(), body[3]);
                new RoilandChannel(ctx.channel()).writeAndFlush(temp);
            }
        }
        ctx.fireChannelRead(protocol);
    }
}
