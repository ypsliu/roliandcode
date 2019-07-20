package com.roiland.platform.socket.io.handler;

import static com.codahale.metrics.MetricRegistry.name;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.roiland.platform.socket.SocketManager;
import com.roiland.platform.socket.bean.ProtocolBean;
import com.roiland.platform.socket.property.SocketProperties;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RoilandDecodeHandler extends ChannelInboundHandlerAdapter {

    protected static final Log LOGGER = LogFactory.getLog(RoilandDecodeHandler.class);

	private static final boolean METRIC = Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_METRIC, "false")); 
    private static MetricRegistry metrics;
    private static Meter requests;
    
//	protected ByteBuf byteBuf;

    static {
    	if (METRIC) {
    		metrics = new MetricRegistry();
    		requests = metrics.meter(name(RoilandEncoder.class, "request"));
    	    JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
            reporter.start();
		}
    }
    
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) {
//    	byteBuf = ctx.alloc().buffer(1024);
//    }
    
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) {
//    	byteBuf.release();
//        byteBuf = null;
//    }
    
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		LOGGER.info("建立连接：" + ctx.channel().toString());
	}

	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketManager.getInstance().remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("非法关闭通道：" + ctx.channel().toString() + " " + cause.getMessage());
        ctx.close();
    }
    
    @Override
	public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
    	ByteBuf byteBuf = (ByteBuf) msg;
//        ByteBuf m = (ByteBuf) msg;
//        byteBuf.writeBytes(m);
//        m.release();
        try {
            while(byteBuf.writerIndex() - byteBuf.readerIndex() >= 14) {
                final int readerIndex = byteBuf.readerIndex();
                // 判断协议是否以`RG,`开头
                if (byteBuf.getByte(readerIndex) != 'R' || byteBuf.getByte(readerIndex + 1) != 'G' || byteBuf.getByte(readerIndex + 2) != ',') {            // 非`RG,`开头，弹出一个字节后结束
                    byteBuf.readByte();
                } else {
                    int pos = 3, outerLen = 0;
                    while (byteBuf.getByte(readerIndex + pos) != ',') {
                        outerLen = outerLen * 10 + byteBuf.getByte(readerIndex + pos) - 48;
                        pos ++;
                    }

                    final int length = outerLen + pos + 1;
                    if (byteBuf.readableBytes() >= length) {
                        final byte[] bytes = new byte[length];
                        byteBuf.readBytes(bytes);
                        try {
                            context.fireChannelRead(new ProtocolBean(outerLen, bytes));
                        } catch (Exception e) {
                            LOGGER.error("协议解析异常，协议：" + new String(bytes), e);
                        } finally {
                            if (METRIC) requests.mark();
                        }
                    } else {
                        break;
                    }
                }
            }
        } finally {
            byteBuf.release();
        }
    }
}

