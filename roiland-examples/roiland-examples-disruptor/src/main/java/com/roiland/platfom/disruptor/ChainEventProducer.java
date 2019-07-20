package com.roiland.platfom.disruptor;

import com.lmax.disruptor.RingBuffer;

public class ChainEventProducer {

    private final RingBuffer<ChainEvent> ringBuffer;

    public ChainEventProducer(RingBuffer<ChainEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public final void publish(final Integer count) {
        long sequence = ringBuffer.next();
        try {
            ChainEvent event = ringBuffer.get(sequence);
            event.setCount(count);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
