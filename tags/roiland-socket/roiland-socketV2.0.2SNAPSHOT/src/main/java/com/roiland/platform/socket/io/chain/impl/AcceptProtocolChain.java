package com.roiland.platform.socket.io.chain.impl;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.SocketManager;
import com.roiland.platform.socket.bean.ProtocolBean;
import com.roiland.platform.socket.bean.SocketBean;
import com.roiland.platform.socket.io.chain.BaseProtocolChain;

import io.netty.channel.Channel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 服务器端权限认证Chain
 *
 * @author 杨昆
 * @since 2015/05/11
 * @version 1.0.0
 */
public class AcceptProtocolChain extends BaseProtocolChain {

    private static final Log LOGGER = LogFactory.getLog(AcceptProtocolChain.class);

    private List<SocketBean> socketBeans = new ArrayList<SocketBean>();
    private ISocketChain iAcceptSocketChain = null;

    public AcceptProtocolChain(ISocketChain chain) {
        super(chain);
    }

    public AcceptProtocolChain(ISocketChain iAcceptSocketChain, ISocketChain chain, List<SocketBean> sockets) {
        this(chain);
        this.iAcceptSocketChain = iAcceptSocketChain;
        this.socketBeans = sockets;
    }

    @Override
    public void doChain(Channel channel, ProtocolBean bean) {
        String source = bean.getSource();
        String target = bean.getTarget();
        int type = bean.getType();
        byte[] body = bean.getBody();
        int checker = bean.getChecker();

        // 当前为客户端注册信息。RG,40,XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX,0,0,0,0
        if (checker == 0 && body.length == 1 && body[0] == '0' && !"0".equals(source) && "0".equals(target) && type == 0) {
            String[] localAddress = this.splitAddress(channel.localAddress());
            Integer localPort = Integer.valueOf(localAddress[1]);

            // 客户端通道绑定
            // 判断当前UUID是否允许连接至该主机和端口
            SocketBean socketBean = null;
            if (socketBeans != null ) {
                Iterator<SocketBean> iterator = socketBeans.iterator();
                SocketBean temp;
                while (iterator.hasNext()) {
                	temp = iterator.next();
                    if (source.equals(temp.getUuid()) && localPort.equals(temp.getPort())) {
                        socketBean = temp;
                        break;
                    }
                }
            }

            // 如果能获取到客户端对应分组，则注册至通道管理中心
            if (socketBean != null) {
                LOGGER.info("客户端通过服务器端鉴权认证，UUID：" + source);
                String group = socketBean.getGroup();
                String[] remoteAddress = this.splitAddress(channel.remoteAddress());
                String remoteHost = remoteAddress[0];
                int remotePort = Integer.valueOf(remoteAddress[1]);
                SocketManager.getInstance().add(source, group, remoteHost, remotePort, channel);
                if(iAcceptSocketChain != null) {
                	iAcceptSocketChain.doChain(channel, bean);	
                }
            } else {
                LOGGER.info("客户端未通过服务器端鉴权认证，UUID：" + source + "，端口：" + localPort);
                channel.close();
            }
        } else if (SocketManager.getInstance().exists(channel)) {				// Channel已被注册
            super.doChain(channel, bean);
        } else {																						// Channel未被注册
            channel.close();
        }
    }

    private String[] splitAddress(SocketAddress socketAddress) {
        String address = socketAddress.toString().substring(1);
        return address.split(":");
    }
}
