package com.roiland.platform.socket.io.handler;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import static com.codahale.metrics.MetricRegistry.name;
import com.roiland.platform.socket.bean.ProtocolBean;
import com.roiland.platform.socket.property.SocketProperties;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * 类名：ProtocolDecoder<br/>
 * 作用：对楼兰外层协议进行初步解析。协议格式：RG,length,source,target,type,body,crc<br/>
 *
 * @author 杨昆
 * @since 2015/5/6
 */
public class RoilandDecoder extends ByteToMessageDecoder{

    private static final Log LOGGER = LogFactory.getLog(RoilandDecoder.class);

    private static final boolean METRIC = Boolean.valueOf(System.getProperty(SocketProperties.SOCKET_METRIC, "false")); 
    private static MetricRegistry metrics;
    private static Meter requests;

    static {
    	if (METRIC) {
    		metrics = new MetricRegistry();
    		requests = metrics.meter(name(RoilandDecoder.class, "Roiland Protocol Request"));
    	    JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
            reporter.start();
		}
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
    	while(byteBuf.writerIndex() - byteBuf.readerIndex() >= 14) {
    		final int readerIndex = byteBuf.readerIndex();
            // 判断协议是否以`RG,`开头
            if (byteBuf.getByte(readerIndex) != 'R' || byteBuf.getByte(readerIndex + 1) != 'G' || byteBuf.getByte(readerIndex + 2) != ',') {            // 非`RG,`开头，弹出一个字节后结束
                byteBuf.readByte();											
                return;
            }

    		int pos = 3;
            int outerLen = 0;
            while (byteBuf.getByte(readerIndex + pos) != ',') {
            	outerLen = outerLen * 10 + byteBuf.getByte(readerIndex + pos) - 48;
            	pos ++;
    		}
            int length = outerLen + pos + 1;

            if (byteBuf.readableBytes() >= length) {
                final byte[] bytes = new byte[length];
                byteBuf.readBytes(bytes);
                
                try {
                    list.add(new ProtocolBean(outerLen, bytes));
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
}
