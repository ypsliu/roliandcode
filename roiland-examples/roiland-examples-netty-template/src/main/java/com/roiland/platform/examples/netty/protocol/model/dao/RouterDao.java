package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.RouterBean;
import com.roiland.platform.examples.netty.protocol.utils.ObjectUtils;
import org.skife.jdbi.v2.Handle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class RouterDao {

    public List<RouterBean> findByDBOption(final String option, final String obd) {
        return new DBTemplate<List<RouterBean>>() {
            @Override
            public List<RouterBean> execute(Handle handle) {
                List<Map<String, Object>> rows = handle.createQuery("SELECT\n" +
                        "\t`db_option`.obd AS uuid ,\n" +
                        "\t`db_option`.db_option AS `option` ,\n" +
                        "\t`db_option`.comparer AS `comparer`\n" +
                        "FROM\n" +
                        "\tt_template_router_obd_db_option `db_option`\n" +
                        "LEFT JOIN t_template_db_option `option` ON `option`.uuid = `db_option`.db_option\n" +
                        "AND `option`.`delete` = 0\n" +
                        "WHERE `option`.`option` = ? AND `db_option`.`obd` = ?;").bind(0, option).bind(1, obd).list();

                List<RouterBean> routerBeans = new ArrayList<RouterBean>(rows.size());
                for (Map<String, Object> row: rows) {
                    RouterBean routerBean = new RouterBean();
                    routerBean.setUuid(ObjectUtils.toObject(row.get("uuid"), String.class));
                    routerBean.setOption(ObjectUtils.toObject(row.get("option"), String.class));
                    routerBean.setComparer(ObjectUtils.toObject(row.get("comparer"), String.class));
                    routerBeans.add(routerBean);
                }
                return routerBeans;
            }
        }.using("template");
    }

    public List<RouterBean> findByDBOption(final String option) {
        return new DBTemplate<List<RouterBean>>() {
            @Override
            public List<RouterBean> execute(Handle handle) {
                List<Map<String, Object>> rows = handle.createQuery("SELECT\n" +
                        "\t`db_option`.obd AS uuid ,\n" +
                        "\t`db_option`.db_option AS `option` ,\n" +
                        "\t`db_option`.comparer AS `comparer`\n" +
                        "FROM\n" +
                        "\tt_template_router_obd_db_option `db_option`\n" +
                        "LEFT JOIN t_template_db_option `option` ON `option`.uuid = `db_option`.db_option\n" +
                        "AND `option`.`delete` = 0\n" +
                        "WHERE `option`.`option` = ?;").bind(0, option).list();

                List<RouterBean> routerBeans = new ArrayList<RouterBean>(rows.size());
                for (Map<String, Object> row: rows) {
                    RouterBean routerBean = new RouterBean();
                    routerBean.setUuid(ObjectUtils.toObject(row.get("uuid"), String.class));
                    routerBean.setOption(ObjectUtils.toObject(row.get("option"), String.class));
                    routerBean.setComparer(ObjectUtils.toObject(row.get("comparer"), String.class));
                    routerBeans.add(routerBean);
                }
                return routerBeans;
            }
        }.using("template");
    }

    public List<RouterBean> findByNotice(final String obd) {
        return new DBTemplate<List<RouterBean>>() {
            @Override
            public List<RouterBean> execute(Handle handle) {
                List<Map<String, Object>> rows = handle.createQuery(
                        "SELECT \n" +
                                "\tobd AS uuid ,\n" +
                                "\tnotice AS `option` ,\n" +
                                "\tcomparer AS `comparer`\n" +
                                "FROM \n" +
                                "\tt_template_router_obd_notice\n" +
                                "WHERE `obd` = ?;").bind(0, obd).list();

                List<RouterBean> routerBeans = new ArrayList<RouterBean>(rows.size());
                for (Map<String, Object> row: rows) {
                    RouterBean routerBean = new RouterBean();
                    routerBean.setUuid(ObjectUtils.toObject(row.get("uuid"), String.class));
                    routerBean.setOption(ObjectUtils.toObject(row.get("option"), String.class));
                    routerBean.setComparer(ObjectUtils.toObject(row.get("comparer"), String.class));
                    routerBeans.add(routerBean);
                }
                return routerBeans;
            }
        }.using("template");
    }

    public List<RouterBean> findByNotice() {
        return new DBTemplate<List<RouterBean>>() {
            @Override
            public List<RouterBean> execute(Handle handle) {
                List<Map<String, Object>> rows = handle.createQuery(
                        "SELECT \n" +
                        "\tobd AS uuid ,\n" +
                        "\tnotice AS `option` ,\n" +
                        "\tcomparer AS `comparer`\n" +
                        "FROM \n" +
                        "\tt_template_router_obd_notice;").list();

                List<RouterBean> routerBeans = new ArrayList<RouterBean>(rows.size());
                for (Map<String, Object> row: rows) {
                    RouterBean routerBean = new RouterBean();
                    routerBean.setUuid(ObjectUtils.toObject(row.get("uuid"), String.class));
                    routerBean.setOption(ObjectUtils.toObject(row.get("option"), String.class));
                    routerBean.setComparer(ObjectUtils.toObject(row.get("comparer"), String.class));
                    routerBeans.add(routerBean);
                }
                return routerBeans;
            }
        }.using("template");
    }
}
