package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDCellBean;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.util.IntegerMapper;

import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class OBDCellDao {

    public List<OBDCellBean> findByCommand(final String command, final Integer parent) {
        return new DBTemplate<List<OBDCellBean>>() {
            @Override
            public List<OBDCellBean> execute(Handle handle) {
                Query<Map<String, Object>> query = handle.createQuery(
                        "SELECT\n" +
                                "\t`command` ,\n" +
                                "\t`serial` ,\n" +
                                "\t`parent` ,\n" +
                                "\t`name` ,\n" +
                                "\t`length` ,\n" +
                                "\t`type` ,\n" +
                                "\t`rate` ,\n" +
                                "\t`size` ,\n" +
                                "\t`option` ,\n" +
                                "\t`value` ,\n" +
                                "\t`description`\n" +
                                "FROM\n" +
                                "\t`t_template_obd_cell`\n" +
                                "WHERE\n" +
                                "\t`command` = ?\n" +
                                "\tAND `parent`" + (parent == null ? " is null" : " = ?") + "\n" +
                                "ORDER BY `serial`"
                ).bind(0, command);
                if (parent != null) query.bind(1, parent);
                return query.map(OBDCellBean.class).list();
            }
        }.using("template");
    }

    public Integer count(final String command) {
        return new DBTemplate<Integer>() {
            @Override
            public Integer execute(Handle handle) {
                return handle.createQuery("SELECT COUNT(*) FROM `t_template_obd_cell` WHERE `command` = ?;")
                        .bind(0, command).map(IntegerMapper.FIRST).first();
            }
        }.using("template");
    }
}
