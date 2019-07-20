package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.DBColumnBean;
import com.roiland.platform.examples.netty.protocol.model.bean.DBOptionBean;
import org.skife.jdbi.v2.Handle;

import java.util.List;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class DBOptionDao {

    public DBOptionBean findByID(final String uuid) {
        return new DBTemplate<DBOptionBean>() {
            @Override
            public DBOptionBean execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                        "\t`uuid` ,\n" +
                        "\t`dbid` ,\n" +
                        "\t`option` ,\n" +
                        "\t`failover`\n" +
                        "FROM\n" +
                        "\t`t_template_db_option`\n" +
                        "WHERE\n" +
                        "\t`delete` = 0 AND `uuid` = ?;"
                ).bind(0, uuid).map(DBOptionBean.class).first();
            }
        }.using("template");
    }

    public List<DBOptionBean> findByOBD(final String obd) {
        return new DBTemplate<List<DBOptionBean>>() {
            @Override
            public List<DBOptionBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                        "`option`.`uuid` ,\n" +
                        "`option`.`dbid` ,\n" +
                        "`option`.`option` ,\n" +
                        "`option`.`failover`\n" +
                        "FROM\n" +
                        "`t_template_router_obd_db_option` `db_option`\n" +
                        "INNER JOIN\n" +
                        "`t_template_db_option` `option` ON `option`.uuid = db_option.db_option AND `option`.`delete` = 0\n" +
                        "WHERE\n" +
                        "`db_option`.`obd` = ?;"
                ).bind(0, obd).map(DBOptionBean.class).list();
            }
        }.using("template");
    }

    public List<DBOptionBean> findAll() {
        return new DBTemplate<List<DBOptionBean>>() {
            @Override
            public List<DBOptionBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                        "\t`uuid` ,\n" +
                        "\t`dbid` ,\n" +
                        "\t`option` ,\n" +
                        "\t`failover`\n" +
                        "FROM\n" +
                        "\t`t_template_db_option`\n" +
                        "WHERE\n" +
                        "\t`delete` = 0"
                ).map(DBOptionBean.class).list();
            }
        }.using("template");
    }

    public List<DBColumnBean> findColumns(final String uuid) {
        return new DBTemplate<List<DBColumnBean>>() {
            @Override
            public List<DBColumnBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                                "\t`column`.`name`,\n" +
                                "\t`column`.`column`,\n" +
                                "\t`column`.`type`,\n" +
                                "\t`column`.`prefix`\n" +
                                "FROM\n" +
                                "\tt_template_db_option_column `option`\n" +
                                "LEFT JOIN `t_template_db_column` `column` ON `option`.cid = `column`.uuid\n" +
                                "WHERE\n" +
                                "\t`column`.`delete` = 0\n" +
                                "\tAND `option`.oid = ?").bind(0, uuid).map(DBColumnBean.class).list();
            }
        }.using("template");
    }
}
