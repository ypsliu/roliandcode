package com.roiland.platform.examples.netty.protocol.model.dao;

import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.examples.netty.protocol.model.bean.NoticePropertyBean;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/4
 */
public final class NoticePropertyDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticePropertyDao.class);

    public List<NoticePropertyBean> findByNotice(final String uuid) {
        return new DBTemplate<List<NoticePropertyBean>>() {
            @Override
            public List<NoticePropertyBean> execute(Handle handle) {
                return handle.createQuery(
                        "SELECT\n" +
                                "\t`key` ,\n" +
                                "\t`value`\n" +
                                "FROM\n" +
                                "\t`t_template_notice_property`\n" +
                                "WHERE\n" +
                                "\t`uuid` = ?" +
                                "\tAND `delete` = 0;"
                ).bind(0, uuid).map(NoticePropertyBean.class).list();
            }
        }.using("template");
    }
}
