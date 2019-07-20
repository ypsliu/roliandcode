package com.roiland.platform.socket;

import java.util.List;

import com.roiland.platform.socket.bean.SocketBean;
import com.roiland.platform.socket.io.bootstrap.RoilandServerBoot;
import com.roiland.platform.socket.io.handler.RoilandAcceptHandler;

/**
 * 楼兰服务器端共通处理
 * @author 杨昆
 *
 */
public class RoilandServer {
	
	private RoilandServerBoot roilandServerBoot = null;

	/**
	 * 
	 * @param port
	 * @param acceptBeans
	 * @param iAccpetSocketChain 认证成功后回调函数
	 * @param iSocketChain
	 */
	public void startup(Integer port, List<SocketBean> acceptBeans, ISocketChain iAccpetSocketChain, ISocketChain iSocketChain) {
        this.roilandServerBoot = new RoilandServerBoot(iSocketChain, new RoilandAcceptHandler(acceptBeans, iAccpetSocketChain));
        this.roilandServerBoot.listen(port);
	}
	
	/**
	 * 
	 * @param port
	 * @param acceptBeans
	 * @param iSocketChain
	 */
	public void startup(Integer port, List<SocketBean> acceptBeans, ISocketChain iSocketChain) {
        this.startup(port, acceptBeans, null, iSocketChain);
	}

	/**
	 * 
	 * @param port
	 * @param iSocketChain
	 */
	public void startup(Integer port, ISocketChain iSocketChain) {
        this.startup(port, null, iSocketChain);
	}
}
