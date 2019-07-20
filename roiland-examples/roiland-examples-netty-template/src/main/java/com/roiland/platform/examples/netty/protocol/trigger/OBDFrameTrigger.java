package com.roiland.platform.examples.netty.protocol.trigger;

import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.OBDBodyBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.OBDFrameBiz;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDBean;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDFrameBean;
import com.roiland.platform.examples.netty.protocol.model.dao.OBDFrameDao;
import com.roiland.platform.socket.core.ProtocolBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class OBDFrameTrigger extends Observable implements IBehaviour<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBDFrameTrigger.class);

    private Map<String, OBDFrameBean> obdFrameBeanMap = null;

    private OBDTrigger obdTrigger = null;
    private OBDBodyTrigger obdBodyTrigger = null;

    private OBDFrameBiz obdFrameBiz = null;

    public OBDFrameTrigger() {
        this.obdFrameBeanMap = Collections.synchronizedMap(new HashMap<String, OBDFrameBean>());

        // 添加监听者
        this.obdBodyTrigger = new OBDBodyTrigger();
        this.obdTrigger = new OBDTrigger();
        this.addObserver(this.obdBodyTrigger);
        this.addObserver(this.obdTrigger);

        this.initialize();
    }

    public void initialize() {
        LOGGER.info("=============== 构建协议命令帧结构 ===============");
        this.obdFrameBiz = new OBDFrameBiz();
        OBDFrameDao obdFrameDao = new OBDFrameDao();
        List<OBDFrameBean> obdFrameBeans = obdFrameDao.findAll();
        for (OBDFrameBean obdFrameBean : obdFrameBeans) {
            this.obdFrameBiz.setNext(obdFrameBean);
            this.obdFrameBeanMap.put(obdFrameBean.getCommand(), obdFrameBean);
        }

        // 获取成功后，发送通知
        for (OBDFrameBean obdFrameBean : obdFrameBeans) {
            this.update(obdFrameBean);
        }
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        String command = this.obdFrameBiz.done(protocol, buffer, params);
        OBDBodyBiz obdBodyBiz = this.obdBodyTrigger.get(command);
        if (obdBodyBiz == null) {
            LOGGER.warn("[{}]未识别协议: {}", protocol.getTraceID(), command);
        }
        params.put("_CMD", command);
        List<Map<String, Object>> rows = obdBodyBiz.done(protocol, buffer, params);
        for (Map<String, Object> row: rows) {
            this.obdTrigger.done(protocol, buffer, row);
        }
        return null;
    }

    private void update(OBDFrameBean obdFrameBean) {
        super.setChanged();
        super.notifyObservers(obdFrameBean);
    }
}
