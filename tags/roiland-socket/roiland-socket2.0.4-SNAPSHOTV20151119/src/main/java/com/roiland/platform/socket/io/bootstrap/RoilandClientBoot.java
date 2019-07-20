package com.roiland.platform.socket.io.bootstrap;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.io.handler.RoilandDecodeHandler;
import com.roiland.platform.socket.io.handler.RoilandEncoder;
import com.roiland.platform.socket.io.handler.RoilandHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 创建TCP客户端线程。通过主机地址(Host)及端口(Port)连接到服务器端。
 * 
 * @author 杨昆
 * @since 2015-05-02
 * @version 1.0.0
 */
public class RoilandClientBoot extends BaseClientBoot {

    public RoilandClientBoot(final ISocketChain iSocketChain) {
        super();
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RoilandEncoder());
                ch.pipeline().addLast(new RoilandDecodeHandler());
                ch.pipeline().addLast(new RoilandHandler(iSocketChain));
            }
        });
    }
}
