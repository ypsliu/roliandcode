package com.roiland.platform.examples.netty.protocol.behaviour.db.option;

import com.google.common.base.Preconditions;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.model.bean.DBBean;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;

import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/9
 */
public abstract class BasicDBBehaviour {

    protected DBBean dbBean = null;
    protected List<DBColumnBean> dbColumnBeans = null;

    public BasicDBBehaviour(DBBiz dbBiz) {
        this.dbBean = dbBiz.getDBBean();
        this.dbColumnBeans = dbBiz.getDBColumnBeans();
    }

    protected Integer getShardIndex(Map<String, Object> params, String key) {
        String value = Preconditions.checkNotNull(params.get(key)).toString();
        return Math.abs(value.hashCode() % 1024);
    }
}
