package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.ComparerBean;
import org.skife.jdbi.v2.Handle;

import java.util.List;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/12
 */
public class ComparerDao {

    public ComparerBean findByID(final String uuid) {
        return new DBTemplate<ComparerBean>() {
            @Override
            public ComparerBean execute(Handle handle) {
                return handle.createQuery("" +
                        "SELECT\n" +
                        "\t`uuid` ,\n" +
                        "\t`next` ,\n" +
                        "\t`name` ,\n" +
                        "\t`to` ,\n" +
                        "\t`description`\n" +
                        "FROM\n" +
                        "\tt_template_comparer\n" +
                        "WHERE\n" +
                        "\t`delete` = 0 AND `uuid` = ?;"
                ).map(ComparerBean.class).first();
            }
        }.using("template");
    }

    public List<ComparerBean> findByOBD(final String obd) {
        return new DBTemplate<List<ComparerBean>>() {
            @Override
            public List<ComparerBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                        "\tDISTINCT `comparer`.uuid, comparer.next, comparer.`name`, comparer.`to`, comparer.description\n" +
                        "FROM\n" +
                        "\t`t_template_comparer` `comparer`\n" +
                        "INNER JOIN `t_template_router_obd_db_option` `db_option` ON `comparer`.uuid = `db_option`.comparer\n" +
                        "AND `db_option`.obd = :obd\n" +
                        "INNER JOIN `t_template_router_obd_notice` `notice` ON `comparer`.uuid = `notice`.comparer\n" +
                        "AND `notice`.`obd` = :obd\n" +
                                "WHERE comparer.`delete` = 0;"
                ).bind("obd", obd).map(ComparerBean.class).list();
            }
        }.using("template");
    }
}
