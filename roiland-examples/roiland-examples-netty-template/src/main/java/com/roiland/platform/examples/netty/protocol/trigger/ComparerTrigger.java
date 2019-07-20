package com.roiland.platform.examples.netty.protocol.trigger;

import com.roiland.platform.examples.netty.protocol.behaviour.comparer.ComparerBiz;
import com.roiland.platform.examples.netty.protocol.model.bean.ComparerBean;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDBean;
import com.roiland.platform.examples.netty.protocol.model.dao.ComparerDao;
import com.roiland.platform.examples.netty.protocol.model.dao.RouterDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class ComparerTrigger implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparerTrigger.class);

    private ComparerDao comparerDao = new ComparerDao();

    private Map<String, ComparerBiz> comparerBizMap = new HashMap<>();

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof OBDBean) {
            OBDBean obdBean = (OBDBean) arg;
            List<ComparerBean> comparerBeans = comparerDao.findByOBD(obdBean.getUuid());
            if (comparerBeans == null || comparerBeans.isEmpty()) {
                return;
            }

            LOGGER.info("=============== [{}]比较操作加载 ===============", obdBean.getCommand());
            for (ComparerBean comparerBean: comparerBeans) {
                final String UUID = comparerBean.getUuid();
                if (this.comparerBizMap.containsKey(UUID)) {
                    LOGGER.info("比较 => {}", printf(comparerBean));
                } else {
                    this.comparerBizMap.put(UUID, create(comparerBean));
                }
            }
        }
    }

    public ComparerBiz get(String uuid) {
        return comparerBizMap.get(uuid);
    }

    private ComparerBiz create(ComparerBean comparerBean) {
        final String UUID = comparerBean.getUuid();
        if (comparerBizMap.containsKey(UUID)) {
            return comparerBizMap.get(UUID);
        }

        ComparerBiz next = null;
        if (comparerBean.getNext() != null) {
            ComparerBean nextComparerBean = this.comparerDao.findByID(comparerBean.getNext());
            next = create(nextComparerBean);
        }
        LOGGER.info("比较 => {}", printf(comparerBean));
        return new ComparerBiz(comparerBean, next);
    }

    private String printf(ComparerBean comparerBean) {
        StringBuilder builder = new StringBuilder("源端: " + comparerBean.getName());
        if (comparerBean.getTo() != null) builder.append("目标: " + comparerBean.getTo());
        if (comparerBean.getDescription() != null) builder.append("描述: " + comparerBean.getDescription());
        if (comparerBean.getNext() != null) builder.append("Next: " + comparerBean.getNext());
        return builder.toString();
    }
}
