package com.roiland.platfom.disruptor;

import com.lmax.disruptor.EventTranslator;

/**
 * Created by jeffy.yang on 16-4-22.
 */
public class ChainEventTranslate<T extends ChainEvent> implements EventTranslator<T> {

    private int count;

    public ChainEventTranslate(int count) {
        this.count = count;
    }

    @Override
    public void translateTo(T event, long sequence) {
        event.setCount(count);
    }
}
