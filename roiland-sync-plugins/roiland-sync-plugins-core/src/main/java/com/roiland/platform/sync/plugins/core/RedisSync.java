package com.roiland.platform.sync.plugins.core;

import com.roiland.platform.spi.annotation.SPI;

import javax.annotation.concurrent.ThreadSafe;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/9/9
 */
@SPI
@ThreadSafe
public interface RedisSync extends Closeable {

    /**
     * 初始化，如数据库
     */
    void init();

    /**
     * 批处理数据size
     * @return batch size
     */
    int batchSize();

    /**
     * 同步间隔 毫秒
     * @return sync period
     */
    int syncPeriod();

    /**
     * 保存的线程数
     * @return thread size
     */
    int saveThreads();

    /**
     * 前缀
     * @return accept prefix
     */
    String accept();

    /**
     * 要保存的数据
     * @param records
     */
    void save(List<SyncRecord> records);

    /**
     * 销毁方法
     * @throws IOException
     */
    void close() throws IOException;
}
