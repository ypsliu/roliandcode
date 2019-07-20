package com.roiland.platform.monitor.basic.transform;

import com.roiland.platform.monitor.basic.load.LoadBean;
import com.roiland.platform.monitor.basic.load.LoadCenter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class TransformCenter {

    private static final Log LOGGER = LogFactory.getLog(TransformCenter.class);

    private static TransformCenter ourInstance = new TransformCenter();

    private Timer timer = new Timer("transform");
    private Queue<ExtractBean> extracts = new LinkedBlockingQueue<>();

    public static TransformCenter getInstance() {
        return ourInstance;
    }

    private TransformCenter() {
        new LoadCenter();
        timer.scheduleAtFixedRate(new CycTimerTask(), 500, 500);
        timer.scheduleAtFixedRate(new TransformTimerTask(), 1000, 1000);
    }

    public void publish(InfoBean infoBean, DataBean dataBean) {
        extracts.add(new ExtractBean(infoBean, dataBean));
    }

    private AtomicReference<Long[]> get(InfoBean infoBean) {
        // 获取及初始化监听数据
        AtomicReference<Long[]> reference;
        if (statistics.containsKey(infoBean)) {
            reference = statistics.get(infoBean);
        } else {
            LOCKED.lock();
            try {
                if (!statistics.containsKey(infoBean)) {
                    reference = new AtomicReference<>();
                    statistics.put(infoBean, reference);
                } else {
                    reference = statistics.get(infoBean);
                }
            } finally {
                LOCKED.unlock();
            }
        }
        return reference;
    }

    public void shutdown() {
        timer.cancel();
    }

    private class CycTimerTask extends TimerTask {

        @Override
        public void run() {
            while(!extracts.isEmpty()) {
                ExtractBean extractBean = extracts.poll();
                InfoBean infoBean = extractBean.getInfoBean();
                DataBean dataBean = extractBean.getDataBean();

                AtomicReference<Long[]> reference = get(infoBean);
                // 更新监听数据
                Long[] current;
                Long[] update;
                do {
                    Integer success = dataBean.getSuccess();
                    Integer failure = dataBean.getFailure();
                    Integer input = dataBean.getInput();
                    Integer output = dataBean.getOutput();
                    Integer elapsed = dataBean.getElapsed();
                    Integer concurrent = dataBean.getConcurrent();

                    update = new Long[LENGTH];
                    current = reference.get();
                    if (current == null) {
                        if (success != null) update[0] = Long.valueOf(success);
                        if (failure != null) update[1] = Long.valueOf(failure);
                        if (input != null) update[2] = Long.valueOf(input);
                        if (output != null) update[3] = Long.valueOf(output);
                        if (elapsed != null) update[4] = Long.valueOf(elapsed);
                        if (concurrent != null) update[5] = Long.valueOf(concurrent);
                        if (input != null) update[6] = Long.valueOf(input);
                        if (output != null) update[7] = Long.valueOf(output);
                        if (elapsed != null) update[8] = Long.valueOf(elapsed);
                        if (concurrent != null) update[9] = Long.valueOf(concurrent);
                    } else {
                        if (success != null) update[0] = current[0] + success;					// 成功次数
                        if (failure != null) update[1] = current[1] + failure;					// 失败次数

                        if (input != null) {
                            update[2] = current[2] + input;                                     // ？
                            update[6] = current[6] != null && current[6] > input ? current[6] : input;                // 最大？
                        }
                        if (output != null) {
                            update[3] = current[3] + output;					//  ?
                            update[7] = current[7] != null && current[7] > output ? current[7] : output;	// 最大？
                        }
                        if (elapsed != null) {
                            update[4] = current[4] + elapsed;					//  执行时间
                            update[8] = current[8] != null && current[8] > elapsed ? current[8] : elapsed; // 最大间隔
                        }
                        if (concurrent != null) {
                            update[5] = (current[5] + concurrent) / 2;		//  当前并发
                            update[9] = current[9] != null && current[9] > concurrent ? current[9] : concurrent;	// 最大并发
                        }
                    }
                } while (! reference.compareAndSet(current, update));
            }
        }
    }

    /**
     * 数据合并处理
     */
    private final ReentrantLock LOCKED = new ReentrantLock();
    private final Map<InfoBean, AtomicReference<Long[]>> statistics = new ConcurrentHashMap<>();
    private final Integer LENGTH = 10;

    private final int ELAPSED = 10;
    private Queue<LoadBean> queue = new LinkedBlockingQueue<LoadBean>();
    /**
     * 数据清洗处理
     */
    private class TransformTimerTask extends TimerTask {

        @Override
        public void run() {
            final int size = queue.size();
            if (size > 20000) {
                for (int i = 0; i < 5000; i++) {
                    LOGGER.info(queue.poll());
                }
            }

            int second = Calendar.getInstance(Locale.CHINA).get(Calendar.SECOND);
            if (second % ELAPSED == 0) {
                for (Map.Entry<InfoBean, AtomicReference<Long[]>> entry : statistics.entrySet()) {
                    final InfoBean infoBean = entry.getKey();
                    final AtomicReference<Long[]> reference = entry.getValue();

                    Long[] data = reference.get();
                    if (data[0] == 0 && data[1] == 0) {
                        continue;
                    }

                    Long[] current;
                    Long[] update;
                    do {
                        current = reference.get();
                        update = new Long[LENGTH];
                        if (data[0] != null) update[0] = current[0] - data[0];
                        if (data[1] != null) update[1] = current[1] - data[1];
                        if (data[2] != null) update[2] = current[2] - data[2];
                        if (data[3] != null) update[3] = current[3] - data[3];
                        if (data[4] != null) update[4] = current[4] - data[4];
                        if (data[5] != null) update[5] = current[5] - data[5];
                    } while (! reference.compareAndSet(current, update));
                    queue.add(new LoadBean(infoBean, data));
                }

            }
        }
    }

    public LoadBean poll() {
        return queue.poll();
    }

    public Boolean isEmpty() {
        return queue.isEmpty();
    }
}
