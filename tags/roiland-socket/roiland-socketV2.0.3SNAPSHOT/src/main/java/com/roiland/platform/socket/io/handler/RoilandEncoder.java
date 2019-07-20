package com.roiland.platform.socket.io.handler;

import static com.codahale.metrics.MetricRegistry.name;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.roiland.platform.socket.IDownStream;
import com.roiland.platform.socket.property.SocketProperties;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 类名：ProtocolDecoder<br/>
 * 作用：指令下发编码处理<br/>
 *
 * @author 杨昆
 * @since 2015/5/6
 */
public class RoilandEncoder extends MessageToByteEncoder<IDownStream> {

	private static final Log LOGGER = LogFactory.getLog(RoilandEncoder.class);

	private static final boolean METRIC = Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_METRIC, "false")); 
    private static MetricRegistry metrics;
    private static Meter requests;

    static {
    	if (METRIC) {
    		metrics = new MetricRegistry();
    		requests = metrics.meter(name(RoilandDecoder.class, "Roiland Protocol Response"));
    	    JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
            reporter.start();
		}
    }
	
    private final Boolean isOpenLog;
    
    public RoilandEncoder() {
        this.isOpenLog = Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_LOGGER_OUTPUT, "true"));
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, final IDownStream stream, final ByteBuf byteBuf) throws Exception {
		if (METRIC) {
			requests.mark();
		}
		
		if (isOpenLog && LOGGER.isInfoEnabled()) {
			LOGGER.info(ctx.channel().toString() + stream.toString());
		}
		
		byteBuf.writeBytes(stream.toBytes());
		ctx.flush();
	}
}
