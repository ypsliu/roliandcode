package com.roiland.platform.rest.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.TimerTask;

/**
 * Created by user on 2015/11/30.
 */
public abstract class AbstractTimerTask extends TimerTask {

    private static final Log LOGGER = LogFactory.getLog(AbstractTimerTask.class);

    protected String taskName;

    public AbstractTimerTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            execute();
        } catch (Throwable e) {
            LOGGER.error(taskName, e);
        }
        long period = System.currentTimeMillis() - start;
        LOGGER.info("完成 " + taskName + " 用时 " + (int) (period / 1000) + "秒");
    }

    protected abstract void execute() throws Exception;
}
