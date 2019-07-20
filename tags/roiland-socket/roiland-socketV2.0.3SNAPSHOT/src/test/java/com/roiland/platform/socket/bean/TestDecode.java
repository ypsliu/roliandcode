package com.roiland.platform.socket.bean;

import static com.codahale.metrics.MetricRegistry.name;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.roiland.platform.socket.io.handler.RoilandDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TestDecode {
	
	private static final MetricRegistry metrics = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
    private static final Meter requests = metrics.meter(name(RoilandDecoder.class, "Roiland Protocol Request"));

    static {
        reporter.start(1, TimeUnit.SECONDS);
    }
    
	public static void main(String[] args) {
		final byte[] needDecodeBytes = "RGGGGGGGGGGGGGGRG,151,AW9E011000R250013,83eebee7811847a7ad67f2f00d0a325b,0,CQABIAUAPAEFDwYZDic1DwYZDicnAQcBDP7+/v4BAQMDAwMBAv////8BAwAAAP4BBAAAAP4BCwAAAAEBEAAAAP9hug==,32160".getBytes();
		ByteBuf byteBuf = Unpooled.buffer(1024);
		while(true) {
			if (byteBuf.writerIndex() - byteBuf.readerIndex() < 14) {
				byteBuf.clear();
				byteBuf.writeBytes(needDecodeBytes);
	        }
			
	        final byte[] temp = new byte[14];
	        byteBuf.getBytes(byteBuf.readerIndex(), temp);
	        
	        // 判断协议是否以`RG,`开头
	        if (temp[0] != 'R' || temp[1] != 'G' || temp[2] != ',') {            // 非`RG,`开头，弹出一个字节后结束
	            byteBuf.readByte();	
	        } else {
		        int j = 3;
		        int outerLen = 0;
		        while (temp[j] != ',') {
		        	outerLen = outerLen * 10 + temp[j] - 48;
		        	j ++;
				}
		        final int length = outerLen + j + 1;
	            final byte[] bytes = new byte[length];
	            byteBuf.readBytes(bytes);
	            requests.mark();
	        }
		}
	}
}
