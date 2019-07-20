package com.roiland.platform.socket.io.handler;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.SocketManager;
import com.roiland.platform.socket.bean.DownStreamBean;
import com.roiland.platform.socket.bean.ProtocolBean;
import com.roiland.platform.socket.property.SocketProperties;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *	楼兰指令接收处理（单线程版）。参见 {@link SimpleChannelInboundHandler}
 *
 * @author 杨昆
 */
public final class RoilandSingleHandler extends SimpleChannelInboundHandler<ProtocolBean> {
    
    private static final Log LOGGER = LogFactory.getLog(RoilandSingleHandler.class);

    private final static Boolean IS_OPEN_LOG = Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_LOGGER_INPUT, "true"));
    
    private ISocketChain iSocketChain;
    private RoilandAcceptHandler roilandAcceptHandler;
    
    public RoilandSingleHandler(ISocketChain iSocketChain) {
    	this.iSocketChain = iSocketChain;
    }

    public RoilandSingleHandler(RoilandAcceptHandler roilandAcceptHandler, ISocketChain iSocketChain) {
    	this(iSocketChain);
    	this.roilandAcceptHandler = roilandAcceptHandler;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketManager.getInstance().remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, ProtocolBean protocol) throws Exception {
    	if(IS_OPEN_LOG && LOGGER.isInfoEnabled()) {
    		LOGGER.info(context.channel().toString() + protocol.toString());
    	}
    	
    	final Channel channel = context.channel();
		protocol.decode();								// 协议解析
    	final int outerLen = protocol.getLen();
		boolean pass = true;
		if (roilandAcceptHandler != null && !SocketManager.getInstance().exists(channel)) {
			pass = roilandAcceptHandler.done(channel, protocol);
		} else if (roilandAcceptHandler != null && outerLen == 9) {
			context.channel().writeAndFlush(new DownStreamBean("RG,9,0,0,0,0,0"));
		}
		
		if (pass && outerLen > 9) {
			// 流水号回执
        	final byte[] body = protocol.getBody();
        	final int innerLen = body.length;
	    	if (innerLen > 4 && body[0] == 0x09 && body[2] == 0x01) {
	    		context.writeAndFlush(new DownStreamBean(protocol.getTarget(), protocol.getSource(), protocol.getType(), body[3]));
            }
	    	iSocketChain.doChain(context.channel(), protocol);
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("非法关闭通道：" + ctx.channel().toString() + " " + cause.getMessage());
        ctx.close();
    }
}
