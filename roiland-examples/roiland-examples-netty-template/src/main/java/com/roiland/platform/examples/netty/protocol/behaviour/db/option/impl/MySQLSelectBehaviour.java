package com.roiland.platform.examples.netty.protocol.behaviour.db.option.impl;

import com.google.common.base.Preconditions;
import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.dbutils.query.Query;
import com.roiland.platform.dbutils.query.field.Column;
import com.roiland.platform.dbutils.query.field.OrderColumn;
import com.roiland.platform.dbutils.query.field.Table;
import com.roiland.platform.dbutils.query.impl.Select;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.BasicDBBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.ISelectBehaviour;
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
public class MySQLSelectBehaviour extends BasicDBBehaviour implements ISelectBehaviour {

    public MySQLSelectBehaviour(DBBiz dbBiz) {
        super(dbBiz);
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        final String table = dbBean.getTable();
        final String key = dbBean.getKey();
        final Integer size = dbColumnBeans.size();

        final Column[] columns = new Column[size];
        for (int i = 0; i < size; i++) {
            DBColumnBean dbColumnBean = dbColumnBeans.get(i);
            columns[i] = new Column(table, dbColumnBean.getColumn(), dbColumnBean.getPrefix() + dbColumnBean.getName());
        }

        // 封装SQL语句
        Object value = Preconditions.checkNotNull(params.get(key));
        final Query query = new Select(columns).from(new Table(table)).where(new Column(table, key), Query.ColumnOption.EQ, value);
        if (!StringUtils.isEmpty(dbBean.getSort())) {
            query.order(new OrderColumn(table, dbBean.getSort(), OrderColumn.OrderOption.DESC));
        }

        String schema = dbBean.getSchema() +
                (StringUtils.isEmpty(dbBean.getShard()) ? "" : getShardIndex(params, dbBean.getShard()));

        Map<String, Object> result = new DBTemplate<Map<String, Object>>() {
            @Override
            public Map execute(Handle handle) {
                org.skife.jdbi.v2.Query<Map<String, Object>> result = handle.createQuery(query.toString());
                for (int i = 0; i < columns.length; i++) {
                    result.bind(i, columns[i]);
                }
                return result.first();
            }
        }.using(schema);
        params.putAll(result);
        return null;
    }
}
