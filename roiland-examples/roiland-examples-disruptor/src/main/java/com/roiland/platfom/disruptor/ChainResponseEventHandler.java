package com.roiland.platfom.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffy.yang on 2015/11/19.
 */
public class ChainResponseEventHandler<T extends ChainEvent> implements EventHandler<T> {

    @Override
    public void onEvent(T event, long l, boolean b) throws Exception {
        System.out.println(Thread.currentThread().getName() + "    " + event.getCount());
    }
}
