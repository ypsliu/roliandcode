package com.roiland.platform.socket.core.task;

import com.roiland.platform.socket.core.ISocketConnection;
import com.roiland.platform.socket.core.RoilandChannelManager;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * <p>重连任务。断开连接后，客户端尝试重新建立连接，默认60秒后启动任务</p>
 */
public class RetryTimeTask extends TimerTask implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryTimeTask.class);

    private final long DELAY;
    private final long PERIOD;
    private final Timer timer;
    private final String MSG = "Retry Task";

    private ISocketConnection connection;

    public RetryTimeTask(ISocketConnection connection, long period) {
        this.connection = connection;
        this.PERIOD = period;
        this.DELAY = 60 * 1000;

        LOGGER.info("[{}]启动延迟 => {}", this.MSG, TimeUnit.MILLISECONDS.toSeconds(DELAY) + " " + TimeUnit.SECONDS.name());
        LOGGER.info("[{}]重试频率 => {}", this.MSG, TimeUnit.MILLISECONDS.toSeconds(PERIOD) + " " + TimeUnit.SECONDS.name());

        this.timer = new Timer("task-retry");
        this.timer.scheduleAtFixedRate(this, DELAY, PERIOD);
    }

    @Override
    public void run() {
        Map<String, Channel> channels = RoilandChannelManager.getInstance().findAll();
        for (Map.Entry<String, Channel> entry : channels.entrySet()) {
            final Channel channel = entry.getValue();
            if (channel == null || !channel.isActive()) {
                try {
                    String group = RoilandChannelManager.getInstance().findGroup(entry.getKey());
                    String[] address = entry.getKey().split(":");

                    this.connection.connect(group, address[0], Integer.valueOf(address[1]));
                } catch (Exception e) {
                    if (e instanceof ConnectException) {
                        LOGGER.warn(e.getMessage());
                    } else {
                        LOGGER.warn(e.getMessage(), e);
                    }
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.timer.cancel();
    }
}
