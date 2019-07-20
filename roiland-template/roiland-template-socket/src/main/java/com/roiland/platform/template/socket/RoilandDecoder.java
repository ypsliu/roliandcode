package com.roiland.platform.template.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 类名：ProtocolDecoder<br/>
 * 作用：对楼兰外层协议进行初步解析。协议格式：RG,length,source,target,type,body,crc<br/>
 *
 * @author 杨昆
 * @since 2015/5/6
 */
public class RoilandDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 循环解析当前ByteBuf数据
        if (byteBuf.writerIndex() - byteBuf.readerIndex() < 14) {
            return;
        }

        byte[] temp = new byte[14];
        byteBuf.getBytes(byteBuf.readerIndex(), temp);

        // 判断协议是否以`RG,`开头
        if (temp[0] != 'R' || temp[1] != 'G' || temp[2] != ',') {            // 非`RG,`开头，弹出一个字节后结束
            byteBuf.readByte();
            return;
        }

        int j = 3;
        int outerLen = 0;
        while (temp[j] != ',') {
            outerLen = outerLen * 10 + temp[j] - 48;
            j ++;
        }
        int length = outerLen + j + 1;

        if (byteBuf.readableBytes() >= length) {
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            list.add(bytes);
        }
    }

}
