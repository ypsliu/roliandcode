package com.roiland.platform.socket.io.chain.disruptor;

import com.lmax.disruptor.EventFactory;

public class DataEventFactory  implements EventFactory<DataEventBean> {

    @Override
    public DataEventBean newInstance() {
        return new DataEventBean();
    }
}
