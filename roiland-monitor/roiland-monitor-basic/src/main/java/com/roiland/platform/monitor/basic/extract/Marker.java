package com.roiland.platform.monitor.basic.extract;

import com.roiland.platform.monitor.basic.transform.DataBean;
import com.roiland.platform.monitor.basic.transform.ExtractBean;
import com.roiland.platform.monitor.basic.transform.InfoBean;
import com.roiland.platform.monitor.basic.transform.TransformCenter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffy.yang on 16-6-30.
 */
public class Marker {

    private static final Marker marker = new Marker();

    protected final Map<InfoBean, AtomicInteger> concurrents = new ConcurrentHashMap<>();
    protected final Map<String, ExtractBean> extracts = new ConcurrentHashMap<>();

    private Marker() {}

    public static Marker getInstance() {
        return marker;
    }

    public void start(String key, InfoBean infoBean, DataBean dataBean) {
        synchronized (this) {
            if (concurrents.containsKey(infoBean)) {
                concurrents.get(infoBean).getAndIncrement();
            } else {
                concurrents.put(infoBean, new AtomicInteger(1));
            }
        }
        ExtractBean extractBean = new ExtractBean();
        extractBean.setInfoBean(infoBean);
        extractBean.setDataBean(dataBean);
        extracts.put(key, extractBean);
    }

    public boolean contains(String key) {
        return extracts.containsKey(key);
    }

    public DataBean getDataBean(String key) {
        if (extracts.containsKey(key)) {
            return extracts.get(key).getDataBean();
        } else {
            return null;
        }
    }

    public void stop(String key, DataBean dataBean) {
        if (extracts.containsKey(key)) {
            ExtractBean extractBean = extracts.remove(key);
            TransformCenter.getInstance().publish(extractBean.getInfoBean(), dataBean);
        }
    }
}
