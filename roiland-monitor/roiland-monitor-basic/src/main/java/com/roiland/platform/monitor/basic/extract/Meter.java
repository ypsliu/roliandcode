package com.roiland.platform.monitor.basic.extract;

import com.roiland.platform.monitor.basic.transform.DataBean;
import com.roiland.platform.monitor.basic.transform.InfoBean;
import com.roiland.platform.monitor.basic.transform.TransformCenter;

/**
 * Created by jeffy.yang on 16-6-29.
 */
public class Meter {

    private static final Meter meter = new Meter();

    private Meter() {}

    public static Meter getInstance() {
        return meter;
    }

    public void mark(InfoBean.TYPE type, String application, String service, String method, String group, String version, String client, String server, Integer success, Integer failure, Integer input, Integer output) {
        InfoBean infoBean = new InfoBean(type, application, service, method, group, version, client, server);
        DataBean dataBean = new DataBean(success, failure, input, output, null, null);
        TransformCenter.getInstance().publish(infoBean, dataBean);
    }


    public void mark(InfoBean.TYPE type, String application, String service, String method, String group, String version, String client, String server, Integer success, Integer failure, Integer input, Integer output, Integer elapsed) {
        InfoBean infoBean = new InfoBean(type, application, service, method, group, version, client, server);
        DataBean dataBean = new DataBean(success, failure, input, output, elapsed, null);
        TransformCenter.getInstance().publish(infoBean, dataBean);
    }
}
