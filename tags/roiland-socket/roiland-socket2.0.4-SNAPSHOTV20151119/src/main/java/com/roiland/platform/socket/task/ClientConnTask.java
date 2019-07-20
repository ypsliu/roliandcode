package com.roiland.platform.socket.task;

import com.roiland.platform.socket.IConnCallback;
import com.roiland.platform.socket.ISocketChain;
import com.roiland.platform.socket.SocketManager;
import com.roiland.platform.socket.bean.ChannelBean;
import com.roiland.platform.socket.bean.ConnBean;
import com.roiland.platform.socket.io.bootstrap.RoilandClientBoot;
import com.roiland.platform.socket.property.SocketProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.channel.Channel;

import java.util.*;

/**
 * **********************************************<br/>
 * 类名：ClientConnTask<br/>
 * 作用：客户端重连定时类<br/>
 * *************************************************
 *
 * @author jeffy
 * @since 2015/5/13
 */
public class ClientConnTask implements Runnable {

    /*
     *	① 踢掉后EventGroup未释放问题
     *	② EventGroup初始化连接大小问题
     */
	
    private static final Log LOGGER = LogFactory.getLog(ClientConnTask.class);

    private Map<ConnBean, Channel> clientBeans;
    private ISocketChain iSocketChain;
    
    public ClientConnTask(ISocketChain iSocketChain) {
        this.clientBeans = Collections.synchronizedMap(new HashMap<ConnBean, Channel>());
        this.iSocketChain = iSocketChain;
    }

    public void add(ConnBean connBean) {
    	if(!clientBeans.containsKey(connBean)) {
            this.clientBeans.put(connBean, null);
    	}
    }

    public void addAll(Collection<ConnBean> connBeans) {
    	for (ConnBean connBean : connBeans) {
			this.add(connBean);
		}
    }

	@Override
    public void run() {
        int iConnTime = Integer.valueOf(System.getProperty(SocketProperties.SOCKET_CLIENT_RECONN_TIME, "10"));
        LOGGER.info("启动定时重连任务，每隔" + iConnTime + "秒检查一次。");

        final RoilandClientBoot bootstrap = new RoilandClientBoot(iSocketChain);
        while (true) {
            if (clientBeans.size() > 0) {
                Set<ChannelBean> channels = SocketManager.getInstance().findAll();
                for (Map.Entry<ConnBean, Channel> conn : clientBeans.entrySet()) {
                	// 获取当前连接属性
                	ConnBean tmpConn = conn.getKey();
                	
                	// 如果当前连接不在线
                    if (!channels.contains(tmpConn)) {
                    	final String uuid = tmpConn.getUuid();
                    	final String group = tmpConn.getGroup();
                    	final String remoteHost = tmpConn.getHost();
                    	final int remotePort = tmpConn.getPort();
                    	final IConnCallback callback = tmpConn.getCallback();
                    	Channel channel = bootstrap.startup(uuid, group, remoteHost, remotePort, callback);
                		clientBeans.put(tmpConn, channel);
                    }
                }
            }
            try {
                Thread.sleep(iConnTime * 1000);
            } catch (InterruptedException e) {
                LOGGER.error("客户端重新连接休眠中断异常。异常信息：" + e.getMessage());
            }
        }
    }
}
