package com.roiland.platform.socket.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 类名：ProtocolDecoder<br/>
 * 作用：对楼兰外层协议进行初步解析。协议格式：RG,length,source,target,type,body,crc<br/>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/5/6
 */
public class RoilandDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.writerIndex() - byteBuf.readerIndex() >= 14) {
            /**
             * 数据长度大于等于14，即数据不满足最小格式（RG,9,0,0,0,0,0），退出循环
             */
            byte[] bytes = readBytes(byteBuf);
            if (bytes != null) {
                list.add(bytes);
            }
        }
    }

    /**
     * 从缓冲区读取数据
     *
     * @param byteBuf 字节缓冲区
     * @return 外层协议体
     */
    private byte[] readBytes(ByteBuf byteBuf) {
        int pos = byteBuf.readerIndex();
        // 判断协议是否以`RG,`开头
        if (byteBuf.getByte(pos++) != 'R'
                || byteBuf.getByte(pos++) != 'G'
                || byteBuf.getByte(pos++) != ',') {            // 非`RG,`开头，弹出一个字节后结束
            byteBuf.readByte();
            return null;
        }

        // 计算长度字段
        int outerLen = 0;
        byte c;
        while ((c = byteBuf.getByte(pos++)) != ',') {
            outerLen = outerLen * 10 + c - 48;
        }

        // 计算协议总长度
        int length = pos - byteBuf.readerIndex() + outerLen;

        if (byteBuf.readableBytes() >= length) {
            /**
             * 可读字节大于协议总长度，则截取协议
             */
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            return bytes;
        } else {
            return null;
        }
    }
}
