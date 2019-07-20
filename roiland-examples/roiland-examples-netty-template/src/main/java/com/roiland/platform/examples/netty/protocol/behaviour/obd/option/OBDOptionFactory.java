package com.roiland.platform.examples.netty.protocol.behaviour.obd.option;

import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.obd.OBDBodyBiz;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDCellBean;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/8
 */
public class OBDOptionFactory {

    public IBehaviour getOptionBehaviour(OBDCellBean obdCellBean, OBDBodyBiz... children) {
        IBehaviour behaviour;

        final String OPTION = obdCellBean.getOption();
        if ("loop".equalsIgnoreCase(OPTION)) {
            behaviour = new OptionLoopBehaviour(obdCellBean, children);
        } else if ("switch".equalsIgnoreCase(OPTION)) {
            behaviour = new OptionSwitchBehaviour(obdCellBean, children);
        } else if ("state".equalsIgnoreCase(OPTION)) {
            behaviour = new OptionStateBehaviour(obdCellBean, children);
        } else {
            behaviour = new OptionCellBehaviour(obdCellBean);
        }
        return behaviour;
    }
}
