package com.roiland.platform.socket.core;

import com.roiland.platform.lang.StringUtils;
import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/22
 */
public abstract class RoilandSocketBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandSocketBootstrap.class);

    protected long iHeartPeriod = 60 * 1000;  // 心跳发送间隔(毫秒)
    protected long iRetryPeriod = 10 * 1000;  // 连接重试时间(毫秒)
    protected IDataHandler handler;

    protected final String MSG = "Socket Initialize";
    // 定义Socket参数设置
    protected final Class<NioSocketChannel> NIO_SOCKET_CHANNEL = NioSocketChannel.class;
    protected final Boolean TCP_NODELAY;
    protected final Boolean SO_KEEPALIVE;
    protected final Boolean SO_REUSEADDR;
    protected final Integer SO_RCVBUF;
    protected final Integer SO_SNDBUF;
    protected final AbstractByteBufAllocator ALLOCATOR;

    private Set<Closeable> closeables = new HashSet<>();

    /**
     * 构造器
     *
     * @param handler 业务处理方法
     */
    public RoilandSocketBootstrap(IDataHandler handler) {
        this.handler = handler;

        this.TCP_NODELAY = Boolean.TRUE;
        this.SO_KEEPALIVE = Boolean.TRUE;
        this.SO_REUSEADDR = Boolean.TRUE;

        final String RCVBUF = System.getProperty(Constants.CONFIG_SOCKET_RCVBUF);
        this.SO_RCVBUF = StringUtils.isEmpty(RCVBUF) ? 256 * 1024 * 1024 : Integer.valueOf(RCVBUF);

        final String SNDBUF = System.getProperty(Constants.CONFIG_SOCKET_SNDBUF);
        this.SO_SNDBUF = StringUtils.isEmpty(SNDBUF) ? 256 * 1024 * 1024 : Integer.valueOf(SNDBUF);

        this.ALLOCATOR = PooledByteBufAllocator.DEFAULT;

        LOGGER.info("[{}]Handler => {}", this.MSG, handler.getClass().getName());
        LOGGER.info("[{}]NIO_SOCKET_CHANNEL => {}", this.MSG, this.NIO_SOCKET_CHANNEL.getName());
        LOGGER.info("[{}]TCP_NODELAY => {}", this.MSG, this.TCP_NODELAY);
        LOGGER.info("[{}]SO_KEEPALIVE => {}", this.MSG, this.SO_KEEPALIVE);
        LOGGER.info("[{}]SO_REUSEADDR => {}", this.MSG, this.SO_REUSEADDR);
        LOGGER.info("[{}]SO_RCVBUF => {}", this.MSG, this.SO_RCVBUF);
        LOGGER.info("[{}]SO_SNDBUF => {}", this.MSG, this.SO_SNDBUF);
        LOGGER.info("[{}]ALLOCATOR => {}", this.MSG, this.ALLOCATOR.getClass().getName());
    }

    /**
     * 设置重置时间
     *
     * @param period 周期
     * @param unit   单位
     * @return 当前实例
     */
    public RoilandSocketBootstrap retry(int period, TimeUnit unit) {
        this.iRetryPeriod = unit.toMillis(period);
        return this;
    }

    /**
     * 当前心跳发送频率
     *
     * @param period 周期
     * @param unit   单位
     * @return 当前实例
     */
    public RoilandSocketBootstrap heart(int period, TimeUnit unit) {
        this.iHeartPeriod = unit.toMillis(period);
        return this;
    }

    /**
     * 构建启动器
     *
     * @return 当前实例
     */
    public abstract RoilandSocketBootstrap build();

    protected boolean add(Closeable closeable) {
        return this.closeables.add(closeable);
    }

    protected void close() {
        for (Closeable closeable : this.closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.warn("[{}]线程关闭异常", closeable.toString(), e.getCause());
            }
        }
    }
}
