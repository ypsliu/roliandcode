package com.roiland.platform.examples.netty.protocol.behaviour.db;

import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.DBOptionFactory;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.IInsertBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.ISelectBehaviour;
import com.roiland.platform.examples.netty.protocol.model.bean.DBBean;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;
import com.roiland.platform.examples.netty.protocol.model.bean.DBOptionBean;
import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/8
 */
public class DBBiz implements IBehaviour<Void> {

    private DBBean dbBean = null;
    private DBOptionBean dbOptionBean = null;
    private List<DBColumnBean> dbColumnBeans = null;
    private IBehaviour behaviour = null;

    public DBBiz(DBBean dbBean, DBOptionBean dbOptionBean, List<DBColumnBean> dbColumnBeans) {
        this.dbBean = dbBean;
        this.dbOptionBean = dbOptionBean;
        this.dbColumnBeans = dbColumnBeans;
        this.behaviour = new DBOptionFactory().getOptionBehaviour(this);
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        this.behaviour.done(protocol, buffer, params);
        return null;
    }

    public DBBean getDBBean() {
        return dbBean;
    }

    public void setDBBean(DBBean dbBean) {
        this.dbBean = dbBean;
    }

    public DBOptionBean getDBOptionBean() {
        return dbOptionBean;
    }

    public void setDBOptionBean(DBOptionBean dbOptionBean) {
        this.dbOptionBean = dbOptionBean;
    }

    public List<DBColumnBean> getDBColumnBeans() {
        return dbColumnBeans;
    }

    public void setDBColumnBeans(List<DBColumnBean> dbColumnBeans) {
        this.dbColumnBeans = dbColumnBeans;
    }

}
