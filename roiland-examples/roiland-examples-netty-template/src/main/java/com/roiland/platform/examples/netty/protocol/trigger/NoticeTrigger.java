package com.roiland.platform.examples.netty.protocol.trigger;

import com.roiland.platform.dbutils.query.field.IField;
import com.roiland.platform.examples.netty.protocol.behaviour.notice.NoticeBiz;
import com.roiland.platform.examples.netty.protocol.model.bean.NoticeBean;
import com.roiland.platform.examples.netty.protocol.model.bean.NoticePropertyBean;
import com.roiland.platform.examples.netty.protocol.model.bean.OBDBean;
import com.roiland.platform.examples.netty.protocol.model.dao.NoticeDao;
import com.roiland.platform.examples.netty.protocol.model.dao.NoticePropertyDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class NoticeTrigger implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeTrigger.class);

    private NoticeDao noticeDao = new NoticeDao();
    private NoticePropertyDao noticePropertyDao = new NoticePropertyDao();
    private Map<String, NoticeBiz> noticeBizMap = new HashMap<>();

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof OBDBean) {
            OBDBean obdBean = (OBDBean) arg;
            List<NoticeBean> noticeBeanList = this.noticeDao.findByOBD(obdBean.getUuid());
            if (noticeBeanList == null || noticeBeanList.isEmpty()) {
                return;
            }

            LOGGER.info("=============== [{}]通知操作加载 ===============", obdBean.getCommand());
            for (NoticeBean noticeBean: noticeBeanList) {
                LOGGER.info("通知 => 命令帧: {}, 通道: {}, 目标: {}, 描述: {}", noticeBean.getCommand(), noticeBean.getTopic(), noticeBean.getTarget(), noticeBean.getDescription());

                final String UUID = noticeBean.getUuid();
                if (noticeBizMap.containsKey(UUID)) {
                    break;
                }

                List<NoticePropertyBean> noticePropertyBeans = noticePropertyDao.findByNotice(UUID);
                this.noticeBizMap.put(UUID, new NoticeBiz(noticeBean, noticePropertyBeans));
            }
        }
    }

    public NoticeBiz get(String uuid) {
        return noticeBizMap.get(uuid);
    }
}
