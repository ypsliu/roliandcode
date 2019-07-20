package com.roiland.platform.examples.netty.protocol.behaviour.obd.option;

import com.google.common.base.Preconditions;
import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.OBDBodyBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.attribute.Length;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDCellBean;
import com.roiland.platform.examples.netty.protocol.utils.BufferUtils;
import com.roiland.platform.lang.ByteUtils;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/6
 */
public class OptionSwitchBehaviour implements IBehaviour<Void> {

    private Length length = null;
    private Length size = null;
    private Map<String, IBehaviour> children = null;

    public OptionSwitchBehaviour(OBDCellBean obdCellBean, OBDBodyBiz... children) {
        Preconditions.checkNotNull(obdCellBean.getLength(), "[%s][%s] illegal argument `length`.", obdCellBean.getCommand(), obdCellBean.getSerial());
        Preconditions.checkState(!("0".equals(obdCellBean.getLength()) || "#{0}".equals(obdCellBean.getLength())), "[%s][%s] illegal `length` value.", obdCellBean.getCommand(), obdCellBean.getSerial());
        Preconditions.checkNotNull(children, "[%s][%s] illegal children size.", obdCellBean.getCommand(), obdCellBean.getSerial());

        this.length = new Length(obdCellBean.getLength());
        this.size = StringUtils.isEmpty(obdCellBean.getSize()) ? null : new Length(obdCellBean.getSize());

        this.children = new HashMap<>(children.length);
        for (OBDBodyBiz child : children) {
            this.children.put(child.getObdCellBean().getName(), child.getBehaviour());
        }
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        // 主键值
        int length = this.length.decode(buffer);

        // 循环次数，如果未设置，则循环至最后一个字节
        int index = this.size == null ? -1 : this.size.decode(buffer);
        while (index == -1 || index-- > 0) {
            String key =  BufferUtils.readBufferAsHex(buffer, length);
            Preconditions.checkState(this.children.containsKey(key), "[%s][%s] unknown child node.", protocol == null? "": protocol.getTraceID(), key);
            this.children.get(key).done(protocol, buffer, params);
        }
        return null;
    }
}
