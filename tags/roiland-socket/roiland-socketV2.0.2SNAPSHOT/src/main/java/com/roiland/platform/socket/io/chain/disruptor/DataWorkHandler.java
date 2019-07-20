package com.roiland.platform.socket.io.chain.disruptor;

import io.netty.channel.Channel;

import com.lmax.disruptor.WorkHandler;
import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.bean.ProtocolBean;

public class DataWorkHandler implements WorkHandler<DataEventBean> {

	@Override
	public void onEvent(DataEventBean event) throws Exception {
        ISocketChain chain = event.getChain();
        Channel channel = event.getChannel();
        ProtocolBean protocolBean = event.getProtocolBean();
        chain.doChain(channel, protocolBean);
	}
}
