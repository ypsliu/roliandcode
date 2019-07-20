package com.roiland.platform.examples.netty.protocol.behaviour.db.option.impl;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.BasicDBBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.IInsertBehaviour;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.socket.core.ProtocolBean;
import org.skife.jdbi.v2.Handle;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/9
 */
public class MySQLInsertBehaviour extends BasicDBBehaviour implements IInsertBehaviour {

    public MySQLInsertBehaviour(DBBiz dbBiz) {
        super(dbBiz);
    }

    @Override
    public Integer done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        final Integer size = dbColumnBeans.size();
        final Object[] values = new Object[size];

        final StringBuilder builder = new StringBuilder("INSERT INTO `" + dbBean.getTable() + "` (");

        // 添加第一列
        final DBColumnBean first = dbColumnBeans.get(0);
        builder.append("`" + first.getColumn() + "`");
        values[0] = params.get(first.getName());

        for (int i = 1; i < size; i++) {
            DBColumnBean dbColumnBean = dbColumnBeans.get(i);
            builder.append("`" + dbColumnBean.getColumn() + "`");
            values[i] = params.get(dbColumnBean.getName());
        }
        builder.append(") VALUE (" + StringUtils.repeat("?", ",", size) + ");");

        String schema = dbBean.getSchema() +
                (StringUtils.isEmpty(dbBean.getShard()) ? "" : getShardIndex(params, dbBean.getShard()));

        return new DBTemplate<Integer>() {
            @Override
            public Integer execute(Handle handle) {
                return handle.insert(builder.toString(), values);
            }
        }.using(schema);
    }
}
