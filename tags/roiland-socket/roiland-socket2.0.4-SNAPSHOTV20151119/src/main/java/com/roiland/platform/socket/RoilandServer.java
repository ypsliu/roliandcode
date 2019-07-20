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
	
	/**
	 * 
	 * @param port
	 * @param acceptBeans
	 * @param iAccpetSocketChain 认证成功后回调函数
	 * @param iSocketChain
	 */
	public void startup(Integer port, List<SocketBean> acceptBeans, ISocketChain iAccpetSocketChain, ISocketChain iSocketChain) {
        new RoilandServerBoot(iSocketChain, acceptBeans, iAccpetSocketChain).listen(port);
	}
}
