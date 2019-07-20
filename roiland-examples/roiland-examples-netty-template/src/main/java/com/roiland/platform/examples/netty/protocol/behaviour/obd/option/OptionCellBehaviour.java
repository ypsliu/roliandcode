package com.roiland.platform.examples.netty.protocol.behaviour.obd.option;

import com.google.common.base.Preconditions;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.attribute.Length;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDCellBean;
import com.roiland.platform.examples.netty.protocol.utils.ConvertUtils;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.ProtocolBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/6
 */
public class OptionCellBehaviour implements ICellBehaviour {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionCellBehaviour.class);

    protected String command = null;
    protected Length length = null;
    protected String name = null;
    protected String type = null;
    protected Integer rate = null;
    protected String value = null;

    public OptionCellBehaviour(OBDCellBean obdCellBean) {
        this.command = obdCellBean.getCommand();
        this.length = new Length(obdCellBean.getLength());
        this.name = obdCellBean.getName();
        this.type = obdCellBean.getType();
        this.rate = obdCellBean.getRate();
        this.value = obdCellBean.getValue();

        Preconditions.checkArgument(
                !(("float".equals(this.type) || "double".equals(this.type)) && rate == null)
                , "[%s][%s] illegal argument `rate`."
                , this.command, obdCellBean.getSerial());

        Preconditions.checkArgument(
                !("0".equals(this.length.toString()) && StringUtils.isEmpty(this.value))
                , "[%s][%s] illegal argument `value`."
                , this.command, obdCellBean.getSerial());

        Preconditions.checkNotNull(
                !(this.type != null && ConvertUtils.findType(this.type) == null)
                , "[%s][%s] illegal argument `type`."
                , this.command, obdCellBean.getSerial());
    }

    @Override
    public Object done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        // 主键值
        int length = this.length.decode(buffer);
        byte[] bytes = new byte[length];
        buffer.get(bytes);

        if (StringUtils.isEmpty(this.name)) {
            return null;
        }

        Object result;
        String type = this.type;
        Integer rate = this.rate;
        if (length == 0) {
            // 如果长度为0, 数据为常量
            String value = this.value;
            result = ConvertUtils.toObject(value, type, rate);
        } else {
            result = ConvertUtils.toObject(bytes, type, rate);

            // 长度非0情况下，设置属性值
            if (!StringUtils.isEmpty(this.name) && result != null) {
                params.put(this.name, result);
                LOGGER.debug("[{}][{}] key: {}, value: {}", protocol.getTraceID(), this.command, this.name, result);
            }
        }
        return result;
    }
}
