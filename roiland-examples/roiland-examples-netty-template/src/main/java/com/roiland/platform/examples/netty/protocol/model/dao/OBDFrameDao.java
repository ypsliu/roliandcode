package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDFrameBean;
import org.skife.jdbi.v2.Handle;

import java.util.List;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class OBDFrameDao {

    public List<OBDFrameBean> findAll() {
        return new DBTemplate<List<OBDFrameBean>>() {
            @Override
            public List<OBDFrameBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                                "\t`command` ,\n" +
                                "\t`frame` ,\n" +
                                "\t`parent` ,\n" +
                                "\t`position`,\n" +
                                "\t`description`\n" +
                                "FROM\n" +
                                "\t`t_template_obd_frame`\n" +
                                "WHERE\n" +
                                "\t`delete` = 0\n" +
                                "ORDER BY `command`"
                ).map(OBDFrameBean.class).list();
            }
        }.using("template");
    }

}
