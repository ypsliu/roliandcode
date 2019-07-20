package com.roiland.platform.socket.io.handler;

import static com.codahale.metrics.MetricRegistry.name;
import akka.actor.ActorRef;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.roiland.platform.socket.IDownStream;
import com.roiland.platform.socket.property.SocketProperties;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 类名：ProtocolDecoder<br/>
 * 作用：指令下发编码处理<br/>
 *
 * @author 杨昆
 * @since 2015/5/6
 */
public class RoilandEncoder extends MessageToByteEncoder<IDownStream> {

	private static final boolean METRIC = Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_METRIC, "false")); 
    private static MetricRegistry metrics;
    private static Meter requests;
    private ActorRef actor = null;

    static {
    	if (METRIC) {
    		metrics = new MetricRegistry();
    		requests = metrics.meter(name(RoilandEncoder.class, "response"));
    	    JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
            reporter.start();
		}
    }
    
    public RoilandEncoder() {
//    	this.actor = system.actorOf(Props.create(LogActor.class));
    }

    @Override
	protected void encode(ChannelHandlerContext context, final IDownStream stream, final ByteBuf byteBuf) throws Exception {
//    	this.actor.tell(new LogActorBean(TYPE.OUTPUT, context.channel(), stream.toString()), ActorRef.noSender());
    	// 开启测量工具
		if (METRIC) requests.mark();
		
		byteBuf.writeBytes(stream.toBytes());
		context.flush();
	}
}
