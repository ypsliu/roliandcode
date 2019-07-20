package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.DBBean;
import org.skife.jdbi.v2.Handle;

import java.util.List;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class DBDao {

    public List<DBBean> findAll() {
        return new DBTemplate<List<DBBean>>() {
            @Override
            public List<DBBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                                "\t`uuid` ,\n" +
                                "\t`type` ,\n" +
                                "\t`schema` ,\n" +
                                "\t`table` ,\n" +
                                "\t`shard` ,\n" +
                                "\t`key` ,\n" +
                                "\t`sort`\n" +
                                "FROM\n" +
                                "\t`t_template_db`\n" +
                                "WHERE\n" +
                                "\t`delete` = 0\n" +
                                "ORDER BY `type`, `schema`, `table`"
                ).map(DBBean.class).list();
            }
        }.using("template");
    }

    public DBBean findByID(final String uuid) {
        return new DBTemplate<DBBean>() {
            @Override
            public DBBean execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                                "\t`uuid` ,\n" +
                                "\t`type` ,\n" +
                                "\t`schema` ,\n" +
                                "\t`table` ,\n" +
                                "\t`shard` ,\n" +
                                "\t`key` ,\n" +
                                "\t`sort`\n" +
                                "FROM\n" +
                                "\t`t_template_db`\n" +
                                "WHERE\n" +
                                "\t`delete` = 0 AND `uuid` = ?\n" +
                                "ORDER BY `type`, `schema`, `table`"
                ).bind(0, uuid).map(DBBean.class).first();
            }
        }.using("template");
    }
}
