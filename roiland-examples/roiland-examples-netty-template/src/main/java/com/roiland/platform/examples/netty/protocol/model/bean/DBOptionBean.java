package com.roiland.platform.examples.netty.protocol.model.bean;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class DBOptionBean {

    private String uuid;
    private String dbid;
    private String option;
    private String failover;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDbid() {
        return dbid;
    }

    public void setDbid(String dbid) {
        this.dbid = dbid;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getFailover() {
        return failover;
    }

    public void setFailover(String failover) {
        this.failover = failover;
    }
}
