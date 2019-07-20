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
	
	/**
	 * 客户端启动类，封装启动客户端共通方法。
	 * 
	 * @param socketBeans
	 *            连接服务器列表
	 * @param iSocketChain
	 *            楼兰消息处理链
	 */
	public void startup(List<SocketBean> socketBeans, ISocketChain iSocketChain) {
		// 启动及定时检测
		final ClientConnTask clientConnTask = new ClientConnTask(iSocketChain);
		List<ConnBean> connBeans = new ArrayList<ConnBean>(socketBeans.size());
		for (SocketBean socketBean : socketBeans) {
			String tmpUUID = socketBean.getUuid();
			String tmpGroup = socketBean.getGroup();
			String tmpHost = socketBean.getHost();
			Integer tmpPort = socketBean.getPort();
			connBeans.add(new ConnBean(tmpUUID, tmpGroup, tmpHost, tmpPort, new RoilandCallBack()));
		}
		clientConnTask.addAll(connBeans);

		Executor executor = Executors.newFixedThreadPool(2);
		executor.execute(clientConnTask);
		executor.execute(new HeartTask());  // 心跳下发
	}
	
}
