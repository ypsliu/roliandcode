package com.roiland.platform.examples.netty.protocol.behaviour.db.option.bean;

import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/12
 */
public class CacheKeyBean {

    private DBColumnBean dbColumnBean;
    private Object key;

    public CacheKeyBean(DBColumnBean dbColumnBean, Object key) {
        this.dbColumnBean = dbColumnBean;
        this.key = key;
    }

    public DBColumnBean getDbColumnBean() {
        return dbColumnBean;
    }

    public void setDbColumnBean(DBColumnBean dbColumnBean) {
        this.dbColumnBean = dbColumnBean;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheKeyBean that = (CacheKeyBean) o;

        if (dbColumnBean != null ? !dbColumnBean.equals(that.dbColumnBean) : that.dbColumnBean != null) return false;
        return !(key != null ? !key.equals(that.key) : that.key != null);

    }

    @Override
    public int hashCode() {
        int result = dbColumnBean != null ? dbColumnBean.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }
}
