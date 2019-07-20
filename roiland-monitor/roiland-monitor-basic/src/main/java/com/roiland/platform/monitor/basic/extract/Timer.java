package com.roiland.platform.monitor.basic.extract;

import com.roiland.platform.monitor.basic.transform.DataBean;
import com.roiland.platform.monitor.basic.transform.InfoBean;
import com.roiland.platform.monitor.basic.transform.TransformCenter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Timer {

    private static final Timer timer = new Timer();

    protected final Map<InfoBean, AtomicInteger> concurrents = new ConcurrentHashMap<>();

    private Timer() {}

    public static Timer getInstance() {
        return timer;
    }

    public class Context {

        private InfoBean monitorBean;
        private final long start;

        public Context(InfoBean monitorBean) {
            this.monitorBean = monitorBean;
            this.start = System.currentTimeMillis();
        }

        public void stop(Integer success, Integer failure, Integer input, Integer output) {
            Integer concurrent;
            synchronized (this) {
                concurrent = concurrents.get(monitorBean).decrementAndGet();
            }
            DataBean dataBean = new DataBean(success, failure, input, output, Long.valueOf(System.currentTimeMillis() - start).intValue(), concurrent);
            TransformCenter.getInstance().publish(monitorBean, dataBean);
        }
    }

    public Context start(InfoBean monitorBean) {
        synchronized (this) {
            if (concurrents.containsKey(monitorBean)) {
                concurrents.get(monitorBean).getAndIncrement();
            } else {
                concurrents.put(monitorBean, new AtomicInteger(1));
            }
        }
        return new Context(monitorBean);
    }
}
