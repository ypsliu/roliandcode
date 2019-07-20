package com.roiland.platform.socket.io.handler;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.SocketManager;
import com.roiland.platform.socket.bean.DownStreamBean;
import com.roiland.platform.socket.bean.ProtocolBean;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *	楼兰指令接收处理。参见 {@link SimpleChannelInboundHandler}
 *
 * @author 杨昆
 */
public final class RoilandHandler extends SimpleChannelInboundHandler<ProtocolBean> {
    
    private static final Log LOGGER = LogFactory.getLog(RoilandHandler.class);
    
	private static ActorSystem system = ActorSystem.create("ActorSystem");
	private ActorRef chainActor = null;

    public RoilandHandler(ISocketChain iSocketChain) {
    	this.chainActor = system.actorOf(Props.create(ChainActor.class, iSocketChain));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketManager.getInstance().remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, ProtocolBean protocol) throws Exception {
    	chainActor.tell(new ChainBean(context.channel(), protocol), ActorRef.noSender());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("非法关闭通道：" + ctx.channel().toString() + " " + cause.getMessage());
        ctx.close();
    }
    
    
    public static class ChainActor extends UntypedActor implements RequiresMessageQueue<BoundedMessageQueueSemantics> {
    	
    	private ISocketChain iSocketChain = null;
    	public ChainActor(ISocketChain iSocketChain) {
    		this.iSocketChain = iSocketChain;
    	}
    	
        public void onReceive(Object message) {
            if (message instanceof ChainBean) {
            	// 协议解密
            	final ProtocolBean protocolBean = ((ChainBean) message).getProtocolBean();
            	final int len = protocolBean.getLen();
            	if (len > 9) {
                	protocolBean.decode();
                	// 流水号回执
                	ActorRef response = this.context().actorOf(Props.create(ResponseActor.class));
                	response.tell(message, ActorRef.noSender());
                	
                	// 协议处理
                	ActorRef handle = this.context().actorOf(Props.create(HandleActor.class, iSocketChain));
                	handle.tell(message, ActorRef.noSender());
				}
            	
            } else {
                unhandled(message);
            }
        }
    }
    
    /**
     * 业务处理Actor
     */
    public static class HandleActor  extends UntypedActor {
    	private ISocketChain iSocketChain = null;
    	public HandleActor(ISocketChain iSocketChain) {
    		this.iSocketChain = iSocketChain;
    	}
        public void onReceive(Object message) {
            if (message instanceof ChainBean) {
            	ChainBean chainBean = (ChainBean) message;
            	iSocketChain.doChain(chainBean.getChannel(), chainBean.getProtocolBean());
            	this.context().stop(getSelf());
            } else {
                unhandled(message);
            }
        }
    }

    /**
     * 回执处理Actor
     */
    public static class ResponseActor  extends UntypedActor {
        public void onReceive(Object message) {
            if (message instanceof ChainBean) {
            	final ChainBean chainBean = (ChainBean) message;
            	final ProtocolBean protocol = chainBean.getProtocolBean();
            	final byte[] body = protocol.getBody();
            	final int length = body.length;
            	final byte zero = body[0];
            	final byte second = body[2];
            	
                if (length > 4 && zero == 0x09 && second == 0x01) {
                	chainBean.getChannel().writeAndFlush(new DownStreamBean(protocol.getTarget(), protocol.getSource(), protocol.getType(), body[3]));
                }
            	this.context().stop(getSelf());
            } else {
                unhandled(message);
            }
        }
    }
    
    public class ChainBean {
    	private ProtocolBean protocolBean;
    	private Channel channel;
    	public ChainBean(Channel channel, ProtocolBean protocolBean) {
    		this.channel = channel;
    		this.protocolBean = protocolBean;
    	}
		public ProtocolBean getProtocolBean() {
			return protocolBean;
		}
		public void setProtocolBean(ProtocolBean protocolBean) {
			this.protocolBean = protocolBean;
		}
		public Channel getChannel() {
			return channel;
		}
		public void setChannel(Channel channel) {
			this.channel = channel;
		}
    }
}
