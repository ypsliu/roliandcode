package com.roiland.platform.socket.bean;

import com.roiland.platform.socket.IConnCallback;
import com.roiland.platform.socket.ISocketChain;

/**
 * 类名：ConnBean<br/>
 * 作用：SocketBean扩展Bean。通道连接后的回掉方法。<br/>
 * IConnCallback：通道连接成功后执行操作。<br/>
 * ISocketChain：协议处理链。<br/>
 * 
 * @author 杨昆
 * @since 2015/6/26
 */
public class ConnBean extends SocketBean {
	
	/** 通道连接成功后回调函数 */
    private IConnCallback callback = null;

    /**
     * 构造器
     * @param port 主机端口
     * @param iSocketChain 协议处理链
     */
    public ConnBean(Integer port, ISocketChain iSocketChain){
        this(null, null, null, port, null, iSocketChain);
    }

    /**
     * 构造器
     * @param uuid  通道标识
     * @param group 目标端分组
     * @param host  主机地址
     * @param port  主机端口
     */
    public ConnBean(String uuid, String group, String host, int port) {
        super(uuid, group, host, port);
    }

    /**
     * 构造器
     * @param uuid  通道标识
     * @param group 目标端分组
     * @param host  主机地址
     * @param port  主机端口
     * @param callback 通道连接后回调函数
     */
    public ConnBean(String uuid, String group, String host, int port, IConnCallback callback) {
        this(uuid, group, host, port, callback, null);
    }

    /**
     * 构造器
     * @param uuid  通道标识
     * @param group 目标端分组
     * @param port  主机端口
     * @param callback 通道连接后回调函数
     */
    public ConnBean(String uuid, String group, int port, IConnCallback callback) {
        this(uuid, group, null, port, callback, null);
    }

    /**
     * 构造器
     * @param uuid  通道标识
     * @param group 目标端分组
     * @param port  主机端口
     * @param callback 通道连接后回调函数
     * @param chain 协议处理链
     */
    public ConnBean(String uuid, String group, int port, IConnCallback callback, ISocketChain chain) {
        this(uuid, group, null, port, callback, chain);
    }

    /**
     * 构造器
     * @param uuid  通道标识
     * @param group 目标端分组
     * @param host  主机地址
     * @param port  主机端口
     * @param callback 通道连接后回调函数
     * @param chain 协议处理链
     */
    public ConnBean(String uuid, String group, String host, int port, IConnCallback callback, ISocketChain chain) {
        super(uuid, group, host, port);
        this.callback = callback;
    }

    /**
     * 获取回调函数
     * @return 回调函数
     */
    public IConnCallback getCallback() {
        return callback;
    }

    /**
     * 设置回调函数
     * @param callback 回调函数
     */
    public void setCallback(IConnCallback callback) {
        this.callback = callback;
    }
}
