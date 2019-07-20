package com.roiland.platform.examples.netty.protocol.behaviour.comparer;

import com.google.common.base.Objects;
import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.model.bean.ComparerBean;
import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/12
 */
public class ComparerBiz implements IBehaviour<Boolean> {

    private ComparerBean comparerBean = null;
    private ComparerBiz next = null;

    public ComparerBiz(ComparerBean comparerBean, ComparerBiz next) {
        this.comparerBean = comparerBean;
        this.next = next;
    }

    @Override
    public Boolean done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        String key = "_" + comparerBean.getUuid();
        if (params.containsKey(key)) {
            return (Boolean)params.get(key);
        } else {
            String name = this.comparerBean.getName();
            String to = this.comparerBean.getTo();

            Boolean result = !Objects.equal(params.get(name), params.get(to));
            if (result && this.next != null) {
                result ^= this.next.done(protocol, buffer, params);
            }
            params.put(key, result);
            return result;
        }
    }

    public ComparerBean getComparerBean() {
        return this.comparerBean;
    }
}
