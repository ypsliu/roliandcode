package com.roiland.platform.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.roiland.platform.socket.bean.ConnBean;
import com.roiland.platform.socket.bean.SocketBean;
import com.roiland.platform.socket.task.ClientConnTask;
import com.roiland.platform.socket.task.HeartTask;

/**
 * 楼兰客户端启动程序
 * 
 * @author 杨昆
 * @version 1.0.5
 */
public class RoilandClient {

	private static final Log LOGGER = LogFactory.getLog(RoilandClient.class);
	
	private ClientConnTask clientConnTask = null;
	
	/**
	 * 添加客户端Socket
	 * @param connBean
	 */
	public void add(ConnBean connBean) {
		this.clientConnTask.add(connBean);
	}


	/**
	 * 楼兰TCP客户端，包括流水号回执、Base64解密操作、心跳包过滤、定时连接监控、心跳包监控。
	 *
	 * @param socketBeans
	 *            连接服务器地址列表
	 * @param iSocketChain
	 *            楼兰协议自定义处理
	 */
	public void startupWithResponse(List<SocketBean> socketBeans, ISocketChain iSocketChain) {
		startup(socketBeans, iSocketChain);
	}

	/**
	 * 楼兰TCP客户端，包括流水号回执、Base64解密操作、心跳包过滤、定时连接监控、心跳包监控。
	 *
	 * @param socketBeans
	 *            连接服务器地址列表
	 * @param iSocketChain
	 *            楼兰协议自定义处理
	 */
	public void startupWithPlatformResponse(List<SocketBean> socketBeans, ISocketChain iSocketChain) {
		startup(socketBeans, iSocketChain);
	}

	/**
	 * 客户端启动类，封装启动客户端共通方法。
	 * 
	 * @param socketBeans
	 *            连接服务器列表
	 * @param iSocketChain
	 *            楼兰消息处理链
	 */
	public void startup(List<SocketBean> socketBeans, ISocketChain iSocketChain) {
		if (clientConnTask == null) {
			// 启动及定时检测
			this.clientConnTask = new ClientConnTask(iSocketChain);
			
			List<ConnBean> connBeans = new ArrayList<ConnBean>(socketBeans.size());
			for (SocketBean socketBean : socketBeans) {
				String tmpUUID = socketBean.getUuid();
				String tmpGroup = socketBean.getGroup();
				String tmpHost = socketBean.getHost();
				Integer tmpPort = socketBean.getPort();
				connBeans.add(new ConnBean(tmpUUID, tmpGroup, tmpHost, tmpPort, new RoilandCallBack()));
			}
			this.clientConnTask.addAll(connBeans);

			Executor executor = Executors.newFixedThreadPool(2);
			executor.execute(this.clientConnTask);
			executor.execute(new HeartTask());  // 心跳下发
		} else {
			LOGGER.warn("启动失败，客户端已被启动");
		}
	}
	
}
