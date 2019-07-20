package com.roiland.platform.socket.core.task;

import com.roiland.platform.socket.core.DownStream;
import com.roiland.platform.socket.core.RoilandChannel;
import com.roiland.platform.socket.core.RoilandChannelManager;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * <p>心跳任务。客户端检测连接是否正常，协议：RG,9,0,0,0,0,0，默认60秒后启动任务。</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/08/22
 */
public class HeartTimeTask extends TimerTask implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartTimeTask.class);

    private final long DELAY;
    private final long PERIOD;
    private final Timer timer;
    private final String MSG = "Heart Task";

    public HeartTimeTask(long period) {
        this.PERIOD = period;
        this.DELAY = 60 * 1000;

        LOGGER.info("[{}]启动延迟 => {}", this.MSG, TimeUnit.MILLISECONDS.toSeconds(DELAY) + " " + TimeUnit.SECONDS.name());
        LOGGER.info("[{}]重试频率 => {}", this.MSG, TimeUnit.MILLISECONDS.toSeconds(PERIOD) + " " + TimeUnit.SECONDS.name());

        timer = new Timer("task-heart");
        timer.scheduleAtFixedRate(this, 60 * 1000, period);
    }

    @Override
    public void run() {
        Map<String, Channel> channels = RoilandChannelManager.getInstance().findAll();
        for (Map.Entry<String, Channel> entry : channels.entrySet()) {
            final Channel channel = entry.getValue();
            if (channel != null && channel.isActive()) {
                new RoilandChannel(channel).writeAndFlush(new DownStream("RG,9,0,0,0,0,0"));
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.timer.cancel();
    }
}
