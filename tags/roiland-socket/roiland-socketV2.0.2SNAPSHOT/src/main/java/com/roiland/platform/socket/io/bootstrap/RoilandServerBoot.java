/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roiland.platform.socket.io.bootstrap;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.io.handler.RoilandDecoder;
import com.roiland.platform.socket.io.handler.RoilandEncoder;
import com.roiland.platform.socket.io.handler.RoilandHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.Properties;

/**
 * 创建TCP服务器端，监听服务器端口(Port)。
 * 
 * @author 杨昆
 * @since 2015-05-02
 * @version 1.0.0
 */
public class RoilandServerBoot extends BaseServerBoot{

    public RoilandServerBoot(final ISocketChain iSocketChain, final Properties properties) {
        super();
        super.bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RoilandEncoder());
                ch.pipeline().addLast(new RoilandDecoder());
                ch.pipeline().addLast(new RoilandHandler(iSocketChain));
            }
        });
    }
}
