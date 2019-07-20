package com.roiland.platform.examples.netty.protocol.behaviour.obd;

import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.option.OBDOptionFactory;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDCellBean;
import com.roiland.platform.socket.core.ProtocolBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/8
 */
public class OBDBodyBiz extends TreeNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBDBodyBiz.class);

    private OBDCellBean obdCellBean = null;
    private IBehaviour behaviour = null;

    public OBDBodyBiz(OBDCellBean obdCellBean, OBDBodyBiz... children) {
        this.obdCellBean = obdCellBean;
        this.behaviour = new OBDOptionFactory().getOptionBehaviour(obdCellBean, children);
    }

    @Override
    public List<Map<String, Object>> done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        Object value = behaviour.done(protocol, buffer, params);

        List<Map<String, Object>> values;
        if (value instanceof List) {
            values = (List) value;
        } else {
            values = new ArrayList<>(1);
            values.add(params);
        }

        if (this.next == null) {
            return values;
        } else {
            List<Map<String, Object>> results = new ArrayList<>();
            for (Map<String, Object> result : values) {
                results.addAll(super.next.done(protocol, buffer, result));

            }
            return results;
        }
    }

    public OBDCellBean getObdCellBean() {
        return obdCellBean;
    }

    public void setObdCellBean(OBDCellBean obdCellBean) {
        this.obdCellBean = obdCellBean;
    }

    public IBehaviour getBehaviour() {
        return behaviour;
    }
}
