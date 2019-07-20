package com.roiland.platform.examples.netty.protocol.behaviour.db.option.impl;

import com.google.common.base.Preconditions;
import com.roiland.platform.cache.Cache;
import com.roiland.platform.cache.commands.ICommands;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.BasicDBBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.IInsertBehaviour;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;
import com.roiland.platform.lang.StringUtils;
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
public class RedisInsertBehaviour extends BasicDBBehaviour implements IInsertBehaviour {

    public RedisInsertBehaviour(DBBiz dbBiz) {
        super(dbBiz);
    }

    @Override
    public Integer done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        // 获取操作指令
        String schema = Preconditions.checkNotNull(dbBean.getSchema());
        String shardKey = dbBean.getShard();
        String shardValue = StringUtils.isEmpty(shardKey) ? null : Preconditions.checkNotNull(params.get(shardKey)).toString();
        ICommands command = Cache.getInstance().getCmdBySchema(schema, shardValue);

        final Map<String, String> hash = new HashMap<>(dbColumnBeans.size());
        for (DBColumnBean dbColumnBean : dbColumnBeans) {
            String _name = dbColumnBean.getName();
            String _key = dbColumnBean.getColumn();
            if (params.containsKey(_name)) {
                hash.put(_key, String.valueOf(params.get(_name)));
            }
        }

        String key = Preconditions.checkNotNull(params.get(dbBean.getKey())).toString();
        command.hmset(dbBean.getTable() + ":" + key, hash);
        return dbColumnBeans.size();
    }
}
