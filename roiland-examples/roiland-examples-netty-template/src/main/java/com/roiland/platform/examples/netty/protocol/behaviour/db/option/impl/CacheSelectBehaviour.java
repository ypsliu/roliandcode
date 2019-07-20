package com.roiland.platform.examples.netty.protocol.behaviour.db.option.impl;

import com.google.common.base.Preconditions;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.BasicDBBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.ISelectBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.bean.CacheKeyBean;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.manager.CacheManager;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;
import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/9
 */
public class CacheSelectBehaviour extends BasicDBBehaviour implements ISelectBehaviour {

    public CacheSelectBehaviour(DBBiz dbBiz) {
        super(dbBiz);
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        String key = Preconditions.checkNotNull(dbBean.getKey());
        Object keyValue = Preconditions.checkNotNull(params.get(key));

        List<CacheKeyBean> keys = new ArrayList<>(dbColumnBeans.size());
        for (DBColumnBean dbColumnBean : dbColumnBeans) {
            keys.add(new CacheKeyBean(dbColumnBean, keyValue));
        }

        Map<CacheKeyBean, Object> result = CacheManager.getInstance().getAll(keys);
        for (Map.Entry<CacheKeyBean, Object> entry: result.entrySet()) {
            DBColumnBean dbColumnBean = entry.getKey().getDbColumnBean();
            params.put(dbColumnBean.getPrefix() + dbColumnBean.getName(), entry.getValue());
        }
        return null;
    }
}
