package com.roiland.platform.socket;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.roiland.platform.socket.bean.SocketBean;
import com.roiland.platform.socket.io.bootstrap.RoilandServerBoot;
import com.roiland.platform.socket.io.chain.impl.AcceptProtocolChain;

/**
 * 楼兰服务器端共通处理
 * @author 杨昆
 *
 */
public class RoilandServer {

	private static final Log LOGGER = LogFactory.getLog(RoilandServer.class);
	
	private RoilandServerBoot roilandServerBoot = null;

	/**
	 * 
	 * @param port
	 * @param acceptBeans
	 * @param iAccpetSocketChain 认证成功后回调函数
	 * @param iSocketChain
	 */
	public void startup(Integer port, List<SocketBean> acceptBeans, ISocketChain iAccpetSocketChain, ISocketChain iSocketChain) {
        if (acceptBeans != null) {
        	LOGGER.info("服务器端接入通道验证");
        	iSocketChain = new AcceptProtocolChain(iAccpetSocketChain, iSocketChain, acceptBeans);         //  服务器端接入通道验证
        }
//		iSocketChain = new ExecutorProtocolChain(iSocketChain);
        this.roilandServerBoot = new RoilandServerBoot(iSocketChain, new Properties());
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
