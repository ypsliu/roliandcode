package com.roiland.platform.socket.io.handler;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.actor.Dispatcher;
import com.roiland.platform.socket.actor.Response;
import com.roiland.platform.socket.bean.ProtocolBean;

import com.roiland.platform.socket.property.SocketProperties;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *	楼兰指令接收处理（多线程版）。参见 {@link SimpleChannelInboundHandler}
 *
 * @author 杨昆
 */
public final class RoilandHandler extends SimpleChannelInboundHandler<ProtocolBean> {
    
    private static final Log LOGGER = LogFactory.getLog(RoilandHandler.class);

    private final boolean LOG_INPUT_OPEN = Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_LOGGER_INPUT, "true"));

    private final ISocketChain iSocketChain;

    private static ActorSystem system = ActorSystem.create("roiland-socket");
    private ActorRef dispatcher = null;

    public RoilandHandler(ISocketChain iSocketChain) {
        this.iSocketChain = iSocketChain;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        dispatcher = system.actorOf(Props.create(Dispatcher.class, ctx.channel()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, ProtocolBean protocol) throws Exception {
        final Channel channel = context.channel();

        dispatcher.tell(protocol, ActorRef.noSender());
        // 日志打印
        if (LOG_INPUT_OPEN && LOGGER.isInfoEnabled())
            LOGGER.info(channel.toString() + protocol.toString());

        try {
            iSocketChain.doChain(channel, protocol);
        } catch (Exception e) {
            LOGGER.error("业务处理发生异常，数据：" + protocol.toString(), e);
        }
    }
}
