/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roiland.platform.socket.io.bootstrap;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.bean.SocketBean;
import com.roiland.platform.socket.io.handler.RoilandAcceptHandler;
import com.roiland.platform.socket.io.handler.RoilandDecodeHandler;
import com.roiland.platform.socket.io.handler.RoilandEncoder;
import com.roiland.platform.socket.io.handler.RoilandHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.List;

/**
 * 创建TCP服务器端，监听服务器端口(Port)。
 * 
 * @author 杨昆
 * @since 2015-05-02
 * @version 1.0.0
 */
public class RoilandServerBoot extends BaseServerBoot{

    public RoilandServerBoot(final ISocketChain iSocketChain, final List<SocketBean> sockets, final ISocketChain iAcceptSocketChain) {
        super();
        super.bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RoilandEncoder());
                ch.pipeline().addLast(new RoilandDecodeHandler());
                ch.pipeline().addLast(new RoilandAcceptHandler(sockets, iAcceptSocketChain));
                ch.pipeline().addLast(new RoilandHandler(iSocketChain));
            }
        });
    }
}
