package com.roiland.platform.socket.core;

/**
 * <p>协议类</p>
 * <p/>
 * <code>
 * traceID: 追踪ID号
 * header: 协议头
 * length: 协议长度
 * source: 源端
 * target: 目标端
 * type: 协议类型
 * body: 解码协议体
 * crc: 协议校验和
 * </code>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/08/23
 */
public class ProtocolBean {

    // 追踪ID
    private final String traceID;
    // 协议头
    private final String header;
    // 协议长度
    private final Integer length;
    // 源端
    private final String source;
    // 目标端
    private final String target;
    // 类型
    private final Integer type;
    // 协议体
    private final byte[] body;
    // 校验和
    private final Integer crc;
    // 原始协议
    private final String original;

    public ProtocolBean(String traceID, String header, Integer length, String source, String target, Integer type, byte[] body, Integer crc, String original) {
        this.traceID = traceID;
        this.header = header;
        this.length = length;
        this.source = source;
        this.target = target;
        this.type = type;
        this.body = body;
        this.crc = crc;
        this.original = original;
    }

    public String getTraceID() {
        return traceID;
    }

    public String getHeader() {
        return header;
    }

    public Integer getLength() {
        return length;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Integer getType() {
        return type;
    }

    public byte[] getBody() {
        return body;
    }

    public Integer getCrc() {
        return crc;
    }

    public String toString() {
        return this.original;
    }
}
