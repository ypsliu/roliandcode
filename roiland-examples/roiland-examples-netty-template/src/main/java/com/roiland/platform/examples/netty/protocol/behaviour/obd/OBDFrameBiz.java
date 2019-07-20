package com.roiland.platform.examples.netty.protocol.behaviour.obd;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDFrameBean;
import com.roiland.platform.lang.ByteUtils;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.ProtocolBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>OBD协议命令帧解析器</p>
 *
 * @author 杨昆
 * @since 16/8/8
 */
public final class OBDFrameBiz implements IBehaviour<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBDFrameBiz.class);

    private Integer deep = null;
    private Integer next = null;
    private OBDFrameBean obdFrameBean = null;
    private Map<Byte, OBDFrameBiz> children = new HashMap<>();

    public OBDFrameBiz() {
        this(0, null);
        this.next = 0;
    }

    public OBDFrameBiz(Integer deep, OBDFrameBean obdFrameBean) {
        this.deep = deep;
        this.obdFrameBean = obdFrameBean;
    }

//    @Override
//    public void validate(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
//        Preconditions.checkState(
//                obdFrameBean.getPosition() >= buffer.capacity()
//                , "[{}] illegal buffer length. position: {}, capacity: {}"
//                , protocol.getTraceID(), obdFrameBean.getPosition(), buffer.capacity());
//    }

    protected OBDFrameBean getOBDFrameBean() {
        return this.obdFrameBean;
    }

    @Override
    public String done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        if (this.children.isEmpty()) {
            return this.obdFrameBean.getCommand();
        }

        Byte frame = buffer.get(this.next);

        Preconditions.checkState(
                this.children.containsKey(frame),
                "[%s][%s][%s] unknown child node.",
                protocol.getTraceID(), this.obdFrameBean == null ? "" : this.obdFrameBean.getParent(), ByteUtils.byteToHex(frame));
        return this.children.get(frame).done(protocol, buffer, params);
    }

    public void setNext(OBDFrameBean obdFrameBean) {
        final String parent = obdFrameBean.getParent();

        if (parent == null || deep * 2 == parent.length()) {
            // 父节点或叶子节点时，创建节点
            if (this.children.isEmpty()) {
                // 如果无子节点，将第一个子节点位置设置为下一级节点的位置
                this.next = obdFrameBean.getPosition();
            } else {
                Preconditions.checkArgument(
                        Objects.equal(this.next, obdFrameBean.getPosition())
                        , "illegal child position. expect: %s, actual: %s"
                        , this.next, obdFrameBean.getPosition()
                );
            }

            final Byte frame = StringUtils.hexToByte(obdFrameBean.getFrame());

            // 校验叶子节点是否存在
            Preconditions.checkArgument(!children.containsKey(frame), "[%s] duplicate node.", obdFrameBean.getFrame());

            this.children.put(frame, new OBDFrameBiz(this.deep + 1, obdFrameBean));
            LOGGER.info("{} {}", StringUtils.repeat("|  ", deep) + "|-" + obdFrameBean.getFrame(), obdFrameBean.getDescription() == null ? "" : "=> " + obdFrameBean.getDescription());
        } else {
            String subParent = parent.substring(deep * 2, (deep + 1) * 2);
            Byte frame = StringUtils.hexToByte(subParent);

            Preconditions.checkArgument(
                    children.containsKey(frame)
                    , "[%s][%s] unknown parent node."
                    , this.obdFrameBean == null ? "" : this.obdFrameBean.getParent(), subParent
            );

            children.get(frame).setNext(obdFrameBean);
        }
    }
}
