package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.NoticeBean;
import org.skife.jdbi.v2.Handle;

import java.util.List;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/4
 */
public final class NoticeDao {

    public List<NoticeBean> findAll() {
        return new DBTemplate<List<NoticeBean>>() {
            @Override
            public List<NoticeBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                                "\t`uuid` ,\n" +
                                "\t`command` ,\n" +
                                "\t`topic` ,\n" +
                                "\t`target` ,\n" +
                                "\t`description`\n" +
                                "FROM\n" +
                                "\t`t_template_notice`\n" +
                                "WHERE `delete` = 0;"
                ).map(NoticeBean.class).list();
            }
        }.using("template");
    }

    public List<NoticeBean> findByOBD(final String obd) {
        return new DBTemplate<List<NoticeBean>>() {
            @Override
            public List<NoticeBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                                "\t`notice`.`uuid` ,\n" +
                                "\t`notice`.`command` ,\n" +
                                "\t`notice`.`topic` ,\n" +
                                "\t`notice`.`target` ,\n" +
                                "\t`notice`.`description`\n" +
                                "FROM\n" +
                                "\t`t_template_router_obd_notice` `router_notice`\n" +
                                "INNER JOIN `t_template_notice` `notice` ON `router_notice`.`notice` = `notice`.uuid\n" +
                                "WHERE `router_notice`.`obd` = ? AND `notice`.`delete` = 0;"
                ).bind(0, obd).map(NoticeBean.class).list();
            }
        }.using("template");
    }
}
