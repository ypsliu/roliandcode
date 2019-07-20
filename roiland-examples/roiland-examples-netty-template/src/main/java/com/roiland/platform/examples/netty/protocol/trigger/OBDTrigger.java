package com.roiland.platform.examples.netty.protocol.trigger;

import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDBean;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDFrameBean;
import com.roiland.platform.examples.netty.protocol.model.dao.OBDDao;
import com.roiland.platform.socket.core.ProtocolBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class OBDTrigger extends Observable implements Observer, IBehaviour<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBDTrigger.class);

    private OBDDao obdDao = new OBDDao();

    private RouterTrigger routerTrigger = null;
    private Map<String, OBDBean> commands = new HashMap<>();

    public OBDTrigger() {
        this.routerTrigger = new RouterTrigger();
        this.addObserver(this.routerTrigger);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof OBDFrameBean) {
            OBDFrameBean obdFrameBean = (OBDFrameBean) arg;
            OBDBean obdBean = this.obdDao.findOne(obdFrameBean.getCommand());

            if (!(obdBean == null || this.commands.containsValue(obdBean))) {
                this.commands.put(obdBean.getCommand(), obdBean);
                super.setChanged();
                super.notifyObservers(obdBean);
            }
        }
    }

    public OBDBean get(String uuid) {
        return this.commands.get(uuid);
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        OBDBean obdBean = commands.get(params.get("_CMD"));
        if (obdBean == null) {
            LOGGER.info("[{}]未找到OBD协议业务主键", protocol.getTraceID());
            return null;
        }
        params.put("_OBD", obdBean.getUuid());
        return routerTrigger.done(protocol, buffer, params);
    }
}
