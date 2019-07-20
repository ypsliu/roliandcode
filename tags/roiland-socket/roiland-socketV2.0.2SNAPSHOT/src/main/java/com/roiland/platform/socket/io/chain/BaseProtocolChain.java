package com.roiland.platform.socket.io.chain;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.bean.ProtocolBean;
import io.netty.channel.Channel;

/**
 * **********************************************<br/>
 * 类名：BaseProtocolChain<br/>
 * 作用：楼兰协议处理链<br/>
 * 描述：楼兰协议处理链父类<br/>
 * *************************************************
 *
 * @author jeffy
 * @since 2015/5/21
 */
public abstract class BaseProtocolChain implements ISocketChain {

    protected ISocketChain chain = null;

    public BaseProtocolChain(ISocketChain chain) {
        this.chain = chain;
    }

    @Override
    public void doChain(Channel channel, ProtocolBean bean) {
        chain.doChain(channel, bean);
    }
}
