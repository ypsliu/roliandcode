package com.roiland.platform.socket.io.chain.disruptor;

import io.netty.channel.Channel;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.bean.ProtocolBean;

public class DataEventBean {
    private ProtocolBean protocolBean;
    private ISocketChain chain;
    private Channel channel;

    public ProtocolBean getProtocolBean() {
        return protocolBean;
    }
    public ISocketChain getChain() {
        return chain;
    }
    public Channel getChannel() {
        return channel;
    }
    public void setChain(ISocketChain chain) {
        this.chain = chain;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    public void setProtocolBean(ProtocolBean protocolBean) {
        this.protocolBean = protocolBean;
    }
}
