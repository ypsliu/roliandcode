package com.roiland.platform.socket.core;

import com.alibaba.fastjson.JSON;
import com.roiland.platform.lang.ByteUtils;
import com.roiland.platform.lang.NumUtils;
import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>数据下发指令处理类</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/08/22
 */
public final class DownStream {

    private String target;
    private String protocol;

    /**
     * 透传协议。默认以','进行简易判断。
     *
     * @param protocol 协议
     */
    public DownStream(String protocol) {
        this(protocol, ",");
    }

    /**
     * 透传协议。简易判断当前协议是否由七部分组成。
     *
     * @param protocol 协议
     * @param split    分隔符
     */
    public DownStream(String protocol, String split) {
        String[] temp = protocol.split(split);
        if (temp.length != 7) {
            throw new IllegalArgumentException("protocol length illegal.");
        }
        this.target = temp[3];
        this.protocol = protocol;
    }

    /**
     * 楼兰指令流水号回执指令封装。协议体以890002开头流水号回执
     *
     * @param source 源端
     * @param target 目标端
     * @param type   协议类型
     * @param serial 流水号
     */
    public DownStream(String source, String target, Integer type, byte serial) {
        byte[] body = {(byte) 0x89, 0x00, 0x02, serial};
        this.build(source, target, type, body);
    }

    /**
     * 楼兰协议，协议体为JSON格式指令封装。
     *
     * @param source 源端
     * @param target 目标端
     * @param type   协议类型
     * @param object 对象
     */
    public DownStream(String source, String target, Integer type, Object object) {
        String body = JSON.toJSONString(object);
        this.build(source, target, type, body.getBytes());
    }

    /**
     * 楼兰指令封装。协议体以890001开头的指令封装
     *
     * @param source 源端
     * @param target 目标端
     * @param type   协议类型
     * @param body   业务协议部分
     */
    public DownStream(String source, String target, Integer type, byte[] body) {
        ByteBuffer business = ByteBuffer.allocate(body.length + 2);
        business.put(body[0]);
        business.put(NumUtils.intTo2Bytes(body.length + 1));
        business.put(body, 1, body.length - 1);

        ByteBuffer buffer = ByteBuffer.allocate(body.length + 8);
        buffer.put((byte) 0x89);
        buffer.put((byte) 0x00);
        buffer.put((byte) 0x01);
        buffer.put(this.serial(target));
        buffer.put(business.array());
        buffer.put(ByteUtils.crc(business.array()));
        this.build(source, target, type, buffer.array());
    }

    /**
     * 协议封装
     *
     * @param source 源端
     * @param target 目标端
     * @param type   类型
     * @param body   协议体
     */
    private void build(String source, String target, Integer type, byte[] body) {
        this.target = target;
        final String temp = source + "," + target + "," + type + "," + Base64.encodeBase64String(body);
        final String crc = "," + ByteUtils.bytesToUInt(ByteUtils.crc(temp.getBytes()));
        protocol = "RG," + (temp.length() + crc.length()) + "," + temp + crc;
    }

    /**
     * 协议目标端
     *
     * @return 目标端ID或分组
     */
    public String target() {
        return target;
    }

    /**
     * 协议
     *
     * @return 协议
     */
    public String toString() {
        return protocol;
    }

    /**
     * TODO: 流水号策越需要加强
     */
    private static final Map<String, Integer> serials = new HashMap<String, Integer>();

    /**
     * 流水号获取
     *
     * @param target 目标端
     * @return 流水号
     */
    private byte serial(String target) {
        int i = serials.containsKey(target) ? serials.get(target) & 0xFF : 0;
        int j = i == 0 || i == 255 ? 1 : i + 1;
        serials.put(target, j);
        return (byte) j;
    }
}
