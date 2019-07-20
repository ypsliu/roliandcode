package com.roiland.platform.monitor.basic.transform;

/**
 * Created by jeffy.yang on 16-6-30.
 */
public class ExtractBean {

    private InfoBean infoBean;
    private DataBean dataBean;
    private Long timestamp;

    public ExtractBean() {
        timestamp = System.currentTimeMillis();
    }

    public ExtractBean(InfoBean infoBean, DataBean dataBean) {
        this();
        this.infoBean = infoBean;
        this.dataBean = dataBean;
    }

    public InfoBean getInfoBean() {
        return infoBean;
    }

    public void setInfoBean(InfoBean infoBean) {
        this.infoBean = infoBean;
    }

    public DataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }
}
