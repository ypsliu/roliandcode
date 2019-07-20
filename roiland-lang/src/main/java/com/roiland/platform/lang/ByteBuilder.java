package com.roiland.platform.lang;

/**
 * 字节数组处理器
 */
public final class ByteBuilder {

    private final byte[] bytes;
    private final int length;

    private int pos = 0;

    private ByteBuilder(final byte[] bytes) {
        this.length = bytes.length;
        this.bytes = bytes;
    }

    /**
     * 封装字节数组
     *
     * @param bytes 字节数组
     * @return ByteBuilder
     */
    public static ByteBuilder wrap(final byte[] bytes) {
        return new ByteBuilder(bytes);
    }

    /**
     * 跳过当前位置几个字节
     *
     * @param skip 跳过字节数
     */
    public void skip(final int skip) {
        this.pos += skip;
        if (this.pos >= this.length || this.pos < 0) {
            throw new IllegalArgumentException("索引超出数组范围[0~" + this.length + "]，实际位置：" + this.pos);
        }
    }

    /**
     * 获取当前位置字节并位置加一
     *
     * @return 当前位置字节
     */
    public byte pick() {
        return bytes[pos++];
    }

    /**
     * 获取当前位置的4个字节作为Integer类型
     *
     * @return Int类型
     */
    public int getAsInteger() {
        return ByteUtils.bytesToInt(bytes[pos++], bytes[pos++], bytes[pos++], bytes[pos++]);
    }

    /**
     * 获取当前位置的4个字节作为Float类型
     *
     * @return Float类型
     */
    public float getAsFloat() {
        return getAsInteger();
    }

    /**
     * 获取字节所在位置
     *
     * @return 位置号
     */
    public int pos() {
        return this.pos;
    }

    /**
     * 获取AscII码，遇到0x00结束
     *
     * @param size 长度
     * @return AscII码
     */
    public String getAsAscII(final int size) {
        final int curr = this.pos;
        this.skip(size);

        int len = curr;
        while (len < this.pos && bytes[len] != 0x00) {
            len ++;
        }
        return len == 0? "": new String(ByteUtils.copy(bytes, curr, len - curr));
    }
}
