package com.roiland.platform.examples.netty.protocol.trigger;

import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.comparer.ComparerBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.OBDBodyBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.router.RouterBiz;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDBean;
import com.roiland.platform.examples.netty.protocol.model.bean.RouterBean;
import com.roiland.platform.examples.netty.protocol.model.dao.RouterDao;
import com.roiland.platform.socket.core.ProtocolBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class RouterTrigger extends Observable implements Observer, IBehaviour<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouterTrigger.class);

    private ComparerTrigger comparerTrigger = null;
    private DBOptionTrigger dbTrigger = null;
    private NoticeTrigger noticeTrigger = null;

    private RouterDao routerDao = new RouterDao();
    private Map<String, List<IBehaviour>> routers = new HashMap<>();

    public RouterTrigger() {
        this.comparerTrigger = new ComparerTrigger();
        this.dbTrigger = new DBOptionTrigger();
        this.noticeTrigger = new NoticeTrigger();

        this.addObserver(this.comparerTrigger);
        this.addObserver(this.dbTrigger);
        this.addObserver(this.noticeTrigger);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof OBDBean) {
            super.setChanged();
            super.notifyObservers(arg);

            OBDBean obdBean = (OBDBean) arg;
            final String UUID = obdBean.getUuid();
            if (this.routers.containsKey(UUID)) {
                this.routers.remove(UUID);
            }

            this.routers.put(UUID, new ArrayList<IBehaviour>());
            // 顺序不能颠倒，否则比较器可能失效
            this.build(UUID, "select");
            this.build(UUID, "insert");
            this.build(UUID, "notice");
        }
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        final String OBD = params.get("_OBD").toString();
        List<IBehaviour> behaviours = routers.get(OBD);
        for (IBehaviour behaviour: behaviours) {
            behaviour.done(protocol, buffer, params);
        }

        return null;
    }

    private void build(String uuid, String option) {
        List<RouterBean> routerBeans = null;
        if ("select".equals(option) || "insert".equals(option)) {
            routerBeans = this.routerDao.findByDBOption(option, uuid);
        } else if ("notice".equals(option)) {
            routerBeans = this.routerDao.findByNotice(uuid);
        }

        for (RouterBean routerBean: routerBeans) {
            final String BEHAVIOUR = routerBean.getOption();
            ComparerBiz comparerBiz = comparerTrigger.get(routerBean.getComparer());
            IBehaviour behaviour = null;
            if ("select".equals(option) || "insert".equals(option)) {
                behaviour = dbTrigger.get(BEHAVIOUR);
            } else if ("notice".equals(option)) {
                behaviour = noticeTrigger.get(BEHAVIOUR);
            }
            this.routers.get(uuid).add(new RouterBiz(comparerBiz, behaviour));
        }
    }
}
