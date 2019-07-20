package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDBean;
import org.skife.jdbi.v2.Handle;

import java.util.List;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class OBDDao {

    public List<OBDBean> findAll() {
        return new DBTemplate<List<OBDBean>>() {

            @Override
            public List<OBDBean> execute(Handle handle) {
                return handle.createQuery("" +
                                "SELECT\n" +
                                "\t`uuid` ,\n" +
                                "\t`command` ,\n" +
                                "\t`description`\n" +
                                "FROM\n" +
                                "\tt_template_obd\n" +
                                "WHERE\n" +
                                "\t`delete` = 0;"
                ).map(OBDBean.class).list();
            }
        }.using("template");
    }

    public OBDBean findOne(final String command) {
        return new DBTemplate<OBDBean>() {

            @Override
            public OBDBean execute(Handle handle) {
                return handle.createQuery("" +
                                "SELECT\n" +
                                "\t`uuid` ,\n" +
                                "\t`command` ,\n" +
                                "\t`description`\n" +
                                "FROM\n" +
                                "\tt_template_obd\n" +
                                "WHERE\n" +
                                "\t`command` = ? AND `delete` = 0;"
                ).bind(0, command).map(OBDBean.class).first();
            }
        }.using("template");
    }
}
