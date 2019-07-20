package com.roiland.platform.examples.netty.protocol.behaviour.obd.option;

import com.google.common.base.Preconditions;
import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.OBDBodyBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.TreeNode;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.attribute.Length;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDCellBean;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/6
 */
public class OptionLoopBehaviour implements IBehaviour<List<Map>> {

    private Length size = null;
    private TreeNode children = null;

    public OptionLoopBehaviour(OBDCellBean obdCellBean, OBDBodyBiz... children) {
        Preconditions.checkNotNull(children, "[%s][%s] illegal children size.", obdCellBean.getCommand(), obdCellBean.getSerial());

        this.size = StringUtils.isEmpty(obdCellBean.getSize()) ? null : new Length(obdCellBean.getSize());
        for (OBDBodyBiz child : children) {
            if (this.children == null) {
                this.children = child;
            } else {
                this.children.setNext(child);
            }
        }
    }

    @Override
    public List<Map> done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        List<Map> result = new ArrayList<>();
        // loop循环体
        // 循环次数，如果未设置，则循环至最后一个字节
        int index = this.size == null ? -1 : this.size.decode(buffer);
        while (index == -1 || index-- > 0) {
            result.addAll(this.children.done(protocol, buffer, new HashMap<>(params)));
        }
        return result;
    }
}
