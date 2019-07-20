package com.roiland.platform.examples.netty.protocol.behaviour.db.option.impl;

import com.google.common.base.Preconditions;
import com.roiland.platform.cache.Cache;
import com.roiland.platform.cache.commands.ICommands;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.BasicDBBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.ISelectBehaviour;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;
import com.roiland.platform.examples.netty.protocol.utils.ConvertUtils;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.ProtocolBean;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/9
 */
public class RedisSelectBehaviour extends BasicDBBehaviour implements ISelectBehaviour {

    public RedisSelectBehaviour(DBBiz dbBiz) {
        super(dbBiz);
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        String schema = Preconditions.checkNotNull(dbBean.getSchema());
        String shardKey = dbBean.getShard();
        String shardValue = StringUtils.isEmpty(shardKey) ? null : Preconditions.checkNotNull(params.get(shardKey)).toString();
        ICommands command = Cache.getInstance().getCmdBySchema(schema, shardValue);

        Integer size = dbColumnBeans.size();
        String[] keys = new String[size];
        for (int i = 0; i < size; i++) {
            keys[i] = dbColumnBeans.get(i).getColumn();
        }

        String key = Preconditions.checkNotNull(params.get(dbBean.getKey())).toString();
        List<String> hash = command.hmget(dbBean.getTable() + ":" + key);
        for (int i = 0; i < size; i++) {
            DBColumnBean dbColumnBean = dbColumnBeans.get(i);
            String value = hash.get(i);
            params.put(dbColumnBean.getPrefix() + dbColumnBean.getName(), ConvertUtils.toObject(value, dbColumnBean.getType(), null));
        }
        return null;
    }
}
