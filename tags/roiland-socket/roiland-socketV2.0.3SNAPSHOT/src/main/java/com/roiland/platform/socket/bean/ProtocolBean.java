package com.roiland.platform.socket.bean;

import org.apache.commons.codec.binary.Base64;

import com.roiland.platform.lang.ByteUtils;

/**
 * 类名：ProtocolBean<br/>
 * 作用：楼兰协议封装Bean<br/>
 *
 * @author 杨昆
 * @since 2015/5/21
 */
public final class ProtocolBean {
    
	private byte[] bytes;
	/** 协议头 */
    private String head;
    /** 协议长度 */
    private int len;
    /** 源端 */
    private String source;
    /** 目标端 */
    private String target;
    /** 协议类型 */
    private int type;
    /** 协议体 */
    private byte[] body;
    /** 校验核 */
    private int checker;

    /**
     * 构造器
     * @param outerLen 楼兰协议
     * @param bytes 楼兰协议
     */
    public ProtocolBean(int outerLen, byte[] bytes) {
    	this.bytes = bytes;
    	this.len = outerLen;
    }
    
    public void decode() {
    	int length = bytes.length;
    	// 从0开始计数
    	int[] positions = new int[6];
    	for (int i = 0, j = 0; j < 6 && i < length; i++) {
			if (bytes[i] == ',') {
				positions[j++] = i;
			}
		}
    	this.head = "RG";
    	this.source = new String(ByteUtils.copy(bytes, positions[1] + 1, positions[2] - positions[1] - 1));
    	this.target = new String(ByteUtils.copy(bytes, positions[2] + 1, positions[3] - positions[2] - 1));
    	this.type = Integer.valueOf(new String(ByteUtils.copy(bytes, positions[3] + 1, positions[4] - positions[3] - 1)));
    	this.checker = Integer.valueOf(new String(ByteUtils.copy(bytes, positions[5] + 1, length - positions[5] - 1)));
    	// Base64 转换
    	byte[] bodyBytes = ByteUtils.copy(bytes, positions[4] + 1, positions[5] - positions[4] - 1);
    	this.body = bodyBytes.length == 1 && bodyBytes[0] == '0'? bodyBytes: Base64.decodeBase64(bodyBytes); 
    }

    /**
     * 获取协议头
     * @return 协议头
     */
    public String getHead() {
        return head;
    }

    /**
     * 获取协议长度
     * @return 长度
     */
    public int getLen() {
        return len;
    }

    /**
     * 获取源端
     * @return 源端
     */
    public String getSource() {
        return source;
    }

    /**
     * 获取目标端
     * @return 目标端
     */
    public String getTarget() {
        return target;
    }

    /**
     * 获取协议类型
     * @return 协议类型
     */
    public int getType() {
        return type;
    }

    /**
     * 获取协议体
     * @return 协议体
     */
    public byte[] getBody() {
        return body;
    }

    public int getChecker() {
		return checker;
	}

	@Override
    public String toString() {
    	return new String(bytes);
    }
}
