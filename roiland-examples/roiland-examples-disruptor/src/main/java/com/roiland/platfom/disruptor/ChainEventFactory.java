package com.roiland.platfom.disruptor;

import com.lmax.disruptor.EventFactory;

public class ChainEventFactory implements EventFactory {

    @Override
    public ChainEvent newInstance() {
        return new ChainEvent();
    }
}
