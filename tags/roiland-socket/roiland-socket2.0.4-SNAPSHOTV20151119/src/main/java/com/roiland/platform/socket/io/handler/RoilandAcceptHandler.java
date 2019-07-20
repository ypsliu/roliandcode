package com.roiland.platform.socket.io.handler;

import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.SocketManager;
import com.roiland.platform.socket.bean.ProtocolBean;
import com.roiland.platform.socket.bean.SocketBean;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.SocketAddress;
import java.util.Iterator;
import java.util.List;

/**
 * 服务器端权限认证Chain
 *
 * @author 杨昆
 * @since 2015/05/11
 * @version 1.0.0
 */
public class RoilandAcceptHandler extends SimpleChannelInboundHandler<ProtocolBean> {

    private static final Log LOGGER = LogFactory.getLog(RoilandAcceptHandler.class);

    private List<SocketBean> socketBeans = null;
    private ISocketChain iAcceptSocketChain = null;

    public RoilandAcceptHandler(List<SocketBean> sockets, ISocketChain iAcceptSocketChain) {
    	if (sockets == null) {
			LOGGER.info("允许接入客户端列表为空，故不对任何客户端进行接入认证");
		} else {
			for (SocketBean socketBean : sockets) {
				LOGGER.info("允许连接服务器端口" + "UUID：" + socketBean.getUuid() + "，分组：" + socketBean.getGroup() + "，端口：" + socketBean.getPort());
			}
		}
        this.socketBeans = sockets;
        this.iAcceptSocketChain = iAcceptSocketChain;
    }

	@Override
	protected void channelRead0(ChannelHandlerContext context, ProtocolBean protocol) throws Exception {
    	final Channel channel = context.channel();
    	final int outerLen = protocol.getLen();
    	final String source = protocol.getSource();
        // 当前为客户端注册信息。RG,40,XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX,0,0,0,0
        if (outerLen == 40 && source.length() == 32) {
            final String[] localAddress = this.splitAddress(channel.localAddress());
            final Integer localPort = Integer.valueOf(localAddress[1]);

            // 客户端通道绑定
            // 判断当前UUID是否允许连接至该主机和端口
            SocketBean socketBean = null;
            if (socketBeans != null ) {
                Iterator<SocketBean> iterator = socketBeans.iterator();
                while (iterator.hasNext()) {
                	final SocketBean temp = iterator.next();
                    LOGGER.info("尝试与UUID：" + temp.getUuid() + "进行比对");
                    if (source.equals(temp.getUuid())) {
                        socketBean = temp;
                        break;
                    }
                }
            }

            // 如果能获取到客户端对应分组，则注册至通道管理中心
            if (socketBeans == null || socketBean != null) {
            	if (socketBeans == null) {
	                LOGGER.info("服务器端不对客户端进行验证");
				} else {
                    LOGGER.info("客户端通过服务器端鉴权认证，UUID：" + source);
				}
            	
            	// 将通过验证中岛放入管理中心
                final String group = socketBean == null? "DEFAULT": socketBean.getGroup();
                final String[] remoteAddress = this.splitAddress(channel.remoteAddress());
                final String remoteHost = remoteAddress[0];
                final int remotePort = Integer.valueOf(remoteAddress[1]);
                if(iAcceptSocketChain != null) {
                	iAcceptSocketChain.doChain(channel, protocol);	
                }
                SocketManager.getInstance().add(source, group, remoteHost, remotePort, channel);
                return;
            } else {
                LOGGER.info("客户端未通过服务器端鉴权认证，UUID：" + source + "，端口：" + localPort);
                context.close();
            }
        } else if (SocketManager.getInstance().exists(channel)) {				// Channel被注册
            context.fireChannelRead(protocol);
        }
	}

    private String[] splitAddress(SocketAddress socketAddress) {
        String address = socketAddress.toString().substring(1);
        return address.split(":");
    }
}
