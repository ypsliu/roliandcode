package com.roiland.platform.examples.netty.protocol.behaviour.router;

import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.comparer.ComparerBiz;
import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class RouterBiz implements IBehaviour<Void>{

    private ComparerBiz comparer = null;
    private IBehaviour behaviour = null;

    public RouterBiz(ComparerBiz comparerBiz, IBehaviour behaviour) {
        this.comparer = comparerBiz;
        this.behaviour = behaviour;
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        if (this.comparer == null || this.comparer.done(protocol, buffer, params)) {
            this.behaviour.done(protocol, buffer, params);
        }
        return null;
    }
}
