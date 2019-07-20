package com.roiland.platform.examples.netty.protocol.behaviour.obd.option;

import com.google.common.base.Preconditions;
import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.OBDBodyBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.attribute.Length;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDCellBean;
import com.roiland.platform.lang.ByteUtils;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.ProtocolBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/6
 */
public class OptionStateBehaviour implements ICellBehaviour {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionStateBehaviour.class);

    private String command = null;
    private String name = null;
    private Length length = null;
    private Map<String, IBehaviour> children = null;

    public OptionStateBehaviour(OBDCellBean obdCellBean, OBDBodyBiz... children) {
        Preconditions.checkNotNull(obdCellBean.getName(), "[%s][%s] illegal argument `name`.", obdCellBean.getCommand(), obdCellBean.getSerial());
        Preconditions.checkNotNull(children, "[%s][%s] illegal children size.", obdCellBean.getCommand(), obdCellBean.getSerial());

        this.command = obdCellBean.getCommand();
        this.name = obdCellBean.getName();
        this.length = new Length(obdCellBean.getLength());
        this.children = new HashMap<>(children.length);

        for (OBDBodyBiz child : children) {
            this.children.put(child.getObdCellBean().getName(), child.getBehaviour());
        }
    }

    @Override
    public Object done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        // 主键值
        int length = this.length.decode(buffer);

        byte[] bytes = new byte[length];
        buffer.get(bytes);
        String key = ByteUtils.bytesToHex(bytes);

        Object result = null;
        if (this.children.containsKey(key)) {
            result = this.children.get(key).done(protocol, buffer, params);

            if (!StringUtils.isEmpty(this.name) && result != null) {
                params.put(this.name, result);
                LOGGER.debug("[{}][{}] key: {}, value: {}", protocol.getTraceID(), this.command, this.name, result);
            }
        }
        return result;
    }
}
