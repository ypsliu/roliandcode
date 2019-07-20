package com.roiland.platform.socket.core.handler;


import com.roiland.platform.socket.core.DownStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 类名：ProtocolDecoder<br/>
 * 作用：指令下发处理<br/>
 *
 * @author 杨昆
 * @since 2015/5/6
 */
public class RoilandEncoder extends MessageToByteEncoder<DownStream> {

    @Override
    protected void encode(ChannelHandlerContext context, DownStream stream, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(stream.toString().getBytes());
        context.flush();
    }
}
