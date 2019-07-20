package com.roiland.platform.socket.bean;

import com.alibaba.fastjson.JSONObject;
import com.roiland.platform.socket.IDownStream;
import com.roiland.platform.lang.ByteUtils;
import com.roiland.platform.lang.NumUtils;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * 类名：DownStreamBean<br/>
 * 作用：指令下发处理Bean<br/>
 * 描述：根据不同的指令封装下发数据，支持字符串、楼兰协议，楼兰协议回执、楼兰JSON协议<br/>
 * <table>
 * <tr>
 * <td>字符串</td>
 * <td>new DownStreamBean(String protocol);</td>
 * </tr>
 * <tr>
 * <td>楼兰协议</td>
 * <td>new DownStreamBean(String source, String target, int type, byte[]
 * command, byte[] body);</td>
 * </tr>
 * <tr>
 * <td>楼兰协议回执</td>
 * <td>new DownStreamBean(String source, String target, int type, byte serial);</td>
 * </tr>
 * <tr>
 * <td>楼兰JSON协议</td>
 * <td>new DownStreamBean(String source, String target, int type, Map<String,
 * Object> params);</td>
 * </tr>
 * </table>
 *
 * @author 杨昆
 * @since 2015/5/21
 */
public final class DownStreamBean implements IDownStream {

	/** 平台至设备回执头 */
	private static final byte HEAD_PLATFORM_TO_WIT = (byte) 0x89;

	/** 设备至平台回执头 */
	// private static final byte HEAD_WIT_TO_PLATFORM = (byte)0x09;

	/** 占位符号 */
	private static final byte PLACEHOLDER = 0x00;

	/** 头类型：01 业务相关指令，02 流水号回执 */
	private static final byte HEAD_TYPE_BUSINESS = 0x01;
	private static final byte HEAD_TYPE_RESPONSE = 0x02;

	/** 逗号 */
	private static final byte COMMA = 0x2C;
	/** 字符 R */
	private static final byte CHAR_R = 0x52;
	/** 字符 G */
	private static final byte CHAR_G = 0x47;

	private static final Map<String, Integer> serials = new HashMap<String, Integer>();

	private byte[] protocol;

	/**
	 * 协议透传
	 * 
	 * @param protocol 透传协议
	 */
	public DownStreamBean(final String protocol) {
		this.protocol = protocol.getBytes();
	}

	/**
	 * 楼兰指令流水号回执指令封装。协议体以890002开头流水号回执
	 * 
	 * @param source 源端
	 * @param target 目标端
	 * @param type 协议类型
	 * @param serial 流水号
	 */
	public DownStreamBean(final String source, final String target, final int type, final byte serial) {
		byte[] temp = { HEAD_PLATFORM_TO_WIT, PLACEHOLDER, HEAD_TYPE_RESPONSE, serial };
		this.protocol = buildOuterProtocol(source, target, type, temp);
	}

	/**
	 * 楼兰指令封装。协议体以890001开头的指令封装
	 * 
	 * @param source 源端
	 * @param target 目标端
	 * @param type 协议类型
	 * @param body 业务协议部分
	 */
	public DownStreamBean(final String source, final String target, final int type, final byte[] body) {
		int innerLen = body.length + 4;								// 协议体中长度
		int innerLenSize = body.length + 1;						// 协议体内长度位	
		int innerLenNoCrc = innerLen - 2; 

		/**
		 * 处理 ① <br/>
		 * 命令帧 + 长度 + 业务逻辑协议
		 */
		byte[] byteBody = new byte[innerLenNoCrc];
		byteBody[0] = body[0];
		byteBody[1] = (byte) (0xff & innerLenSize >> 8);
		byteBody[2] = (byte) (innerLenSize & 0xff);
		if(body.length > 1) {
			System.arraycopy(body, 1, byteBody, 3, body.length - 1);
		}
		byte[] innerCrc = ByteUtils.crc(byteBody); 						// CRC校验

		// 协议体加密
		byte[] tempBody = new byte[innerLen + 4];
		tempBody[0] = HEAD_PLATFORM_TO_WIT;
		tempBody[1] = PLACEHOLDER;
		tempBody[2] = HEAD_TYPE_BUSINESS;
		tempBody[3] = NumUtils.intToByte(this.serial(target));
		System.arraycopy(byteBody, 0, tempBody, 4, innerLenNoCrc);
		tempBody[innerLen + 2] = innerCrc[0];
		tempBody[innerLen + 3] = innerCrc[1];
		this.protocol = buildOuterProtocol(source, target, type, tempBody);
	}

	/**
	 * 楼兰协议，协议体为JSON格式指令封装。
	 * 
	 * @param source 源端
	 * @param target 目标端
	 * @param type 协议类型
	 * @param object 对象
	 */
	public <T extends Object> DownStreamBean(final String source, final String target, final int type, final T object) {
		byte[] byteBody = JSONObject.toJSONString(object).getBytes();
		this.protocol = buildOuterProtocol(source, target, type, byteBody);
	}

	/**
	 * 封装楼兰指令
	 * 
	 * @param source 源端
	 * @param target 目标端
	 * @param type 协议类型
	 * @param bytes 协议体
	 */
	private byte[] buildOuterProtocol(final String source, final String target, final int type, final byte[] bytes) {
		byte[] encode = Base64.encodeBase64(bytes);
		byte[] byteSource = source.getBytes();
		byte[] byteTarget = target.getBytes();
		byte[] byteType = String.valueOf(type).getBytes();
		
		int lenSource = byteSource.length;
		int lenTarget = byteTarget.length;
		int lenType = byteType.length;
		int lenBody = encode.length;

		int partProtocolLen = lenSource + lenTarget + lenType + lenBody + 3;
		byte[] tempPartProtocol = new byte[partProtocolLen];
		System.arraycopy(byteSource, 0, tempPartProtocol, 0, lenSource);
		tempPartProtocol[lenSource] = COMMA;
		System.arraycopy(byteTarget, 0, tempPartProtocol, lenSource + 1, lenTarget);
		tempPartProtocol[lenSource + lenTarget + 1] = COMMA;
		System.arraycopy(byteType, 0, tempPartProtocol, lenSource + lenTarget + 2, lenType);
		tempPartProtocol[lenSource + lenTarget + lenType + 2] = COMMA;
		System.arraycopy(encode, 0, tempPartProtocol, lenSource + lenTarget + lenType + 3, lenBody);
		byte[] outerCrc = String.valueOf(ByteUtils.bytesToUInt(ByteUtils.crc(tempPartProtocol))).getBytes();
		int outerCrcLen = outerCrc.length;
		
		byte[] outerLen = String.valueOf(partProtocolLen + outerCrcLen + 1).getBytes();  // 
		int outerLenSize = outerLen.length;
		byte[] outer = new byte[partProtocolLen + outerCrcLen + outerLenSize + 5];
		outer[0] = CHAR_R;
		outer[1] = CHAR_G;
		outer[2] = COMMA;
		System.arraycopy(outerLen, 0, outer, 3, outerLenSize);
		outer[outerLenSize + 3] = COMMA;
		System.arraycopy(tempPartProtocol, 0, outer, outerLenSize + 4, partProtocolLen);
		outer[outerLenSize + partProtocolLen + 4] = COMMA;
		System.arraycopy(outerCrc, 0, outer, outerLenSize + partProtocolLen + 5, outerCrcLen);
		return outer;
	}

	/**
	 * 流水号获取
	 * 
	 * @param target
	 *            目标端
	 * @return 流水号
	 */
	private int serial(final String target) {
		int i = serials.containsKey(target)? serials.get(target) & 0xFF: 0;
		int j = i == 0 || i == 255? 1: i + 1;
		serials.put(target, j);
		return j;
	}

	@Override
	public byte[] toBytes() {
		return protocol;
	}
	
	public String toString() {
    	return new String(protocol);
	}
}
