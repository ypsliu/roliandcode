package com.roiland.platfom.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffy.yang on 2015/11/19.
 */
public class ChainEventHandler<T extends ChainEvent> implements EventHandler<T>, WorkHandler<T> {

    public static final AtomicInteger COUNTER;

    static {
        COUNTER = new AtomicInteger();
    }

    @Override
    public void onEvent(T event) throws Exception {
        Thread.sleep(500);
        System.out.println(Thread.currentThread().getName() + "    " + event.getCount());
        COUNTER.getAndIncrement();
    }

    @Override
    public void onEvent(T event, long l, boolean b) throws Exception {
        Thread.sleep(500);
        System.out.println(Thread.currentThread().getName() + "    " + event.getCount());
        COUNTER.getAndIncrement();
    }
}
