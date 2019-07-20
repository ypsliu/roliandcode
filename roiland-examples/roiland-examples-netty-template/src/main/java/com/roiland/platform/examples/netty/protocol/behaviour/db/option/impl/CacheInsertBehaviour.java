package com.roiland.platform.examples.netty.protocol.behaviour.db.option.impl;

import com.google.common.base.Preconditions;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.BasicDBBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.IInsertBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.bean.CacheKeyBean;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.manager.CacheManager;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;
import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/9
 */
public class CacheInsertBehaviour extends BasicDBBehaviour implements IInsertBehaviour {

    public CacheInsertBehaviour(DBBiz dbBiz) {
        super(dbBiz);
    }

    @Override
    public Integer done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        String key = Preconditions.checkNotNull(dbBean.getKey());
        Object keyValue = Preconditions.checkNotNull(params.get(key));

        Map<CacheKeyBean, Object> hash = new HashMap<>(this.dbColumnBeans.size());
        for (DBColumnBean dbColumnBean : this.dbColumnBeans) {
            Object hashValue = params.get(dbColumnBean.getName());
            if (hashValue != null) {
                hash.put(new CacheKeyBean(dbColumnBean, keyValue), hashValue);
            }
        }
        if (!hash.isEmpty()) {
            CacheManager.getInstance().putAll(hash);
        }
        return hash.size();
    }
}
