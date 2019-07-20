package com.roiland.platform.examples.netty.protocol.trigger;

import com.roiland.platform.examples.netty.protocol.behaviour.obd.OBDBodyBiz;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDCellBean;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDFrameBean;
import com.roiland.platform.examples.netty.protocol.model.dao.OBDCellDao;
import com.roiland.platform.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class OBDBodyTrigger implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBDBodyTrigger.class);

    private OBDCellDao obdCellDao = new OBDCellDao();

    private Map<String, OBDBodyBiz> obdBodyBizMap = new HashMap<>();

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof OBDFrameBean) {
            OBDFrameBean obdFrameBean = (OBDFrameBean) arg;
            String command = obdFrameBean.getCommand();

            Integer count = this.obdCellDao.count(command);

            if (count > 0) {
                LOGGER.info("=============== 加载协议（" + command + "）解析器 =============== ");
                OBDBodyBiz[] children = this.build(command, null, 0);
                OBDBodyBiz first = children[0];
                for (int i = 1; i < children.length; i++) {
                    first.setNext(children[i]);
                }
                this.obdBodyBizMap.put(command, first);
            }
        }
    }

    public OBDBodyBiz get(String command) {
        return this.obdBodyBizMap.get(command);
    }

    private OBDBodyBiz[] build(String command, Integer parent, Integer deep) {
        List<OBDCellBean> obdCellBeans = this.obdCellDao.findByCommand(command, parent);
        if (obdCellBeans == null || obdCellBeans.isEmpty()) {
            return null;
        }

        final Integer size = obdCellBeans.size();
        OBDBodyBiz[] obdBodyBizs = new OBDBodyBiz[size];
        for (int i = 0; i < size; i++) {
            OBDCellBean obdCellBean = obdCellBeans.get(i);
            LOGGER.info(this.space(command, deep) + this.message(obdCellBean));

            OBDBodyBiz[] children = StringUtils.isEmpty(obdCellBean.getOption()) ? new OBDBodyBiz[0] : this.build(command, obdCellBean.getSerial(), deep + 1);
            obdBodyBizs[i] = new OBDBodyBiz(obdCellBean, children);
        }
        return obdBodyBizs;
    }

    private String message(OBDCellBean obdCellBean) {
        StringBuilder builder = new StringBuilder();
        if (!StringUtils.isEmpty(obdCellBean.getName())) builder.append(", name: " + obdCellBean.getName());
        if (!StringUtils.isEmpty(obdCellBean.getLength())) builder.append(", length: " + obdCellBean.getLength());
        if (!StringUtils.isEmpty(obdCellBean.getType())) builder.append(", type: " + obdCellBean.getType());
        if (obdCellBean.getRate() != null) builder.append(", rate: " + obdCellBean.getRate());
        if (!StringUtils.isEmpty(obdCellBean.getSize())) builder.append(", size: " + obdCellBean.getSize());
        if (!StringUtils.isEmpty(obdCellBean.getOption())) builder.append(", option: " + obdCellBean.getOption());
        if (!StringUtils.isEmpty(obdCellBean.getValue())) builder.append(", value: " + obdCellBean.getValue());
        if (!StringUtils.isEmpty(obdCellBean.getDescription()))
            builder.append(", description: " + obdCellBean.getDescription() + "  ");
        return "[" + obdCellBean.getCommand() + "]" + "[" + obdCellBean.getSerial() + "] => " + builder.toString().substring(1);
    }

    private String space(String command, Integer deep) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= deep; i++) {
            builder.append("|" + StringUtils.repeat(" ", command.length() + 2));
            if (i > 1) {
                builder.append("  ");
            }
        }
        builder.append("|-");
        return deep == 0 ? "" : builder.toString();
    }
}
