package com.roiland.platform.examples.netty.protocol.behaviour.notice;

import com.alibaba.fastjson.JSON;
import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.model.bean.NoticeBean;
import com.roiland.platform.examples.netty.protocol.model.bean.NoticePropertyBean;
import com.roiland.platform.socket.core.DownStream;
import com.roiland.platform.socket.core.ProtocolBean;
import com.roiland.platform.socket.core.Socket;
import com.roiland.platform.socket.core.exception.GroupNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/8
 */
public class NoticeBiz implements IBehaviour<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeBiz.class);

    private String uuid = null;
    private NoticeBean noticeBean = null;
    private List<NoticePropertyBean> noticePropertyBeans = null;

    public NoticeBiz(NoticeBean noticeBean, List<NoticePropertyBean> noticePropertyBeans) {
        this.uuid = System.getProperty("service.id", "0");
        this.noticeBean = noticeBean;
        this.noticePropertyBeans = noticePropertyBeans;
    }

    @Override
    public Void done(ProtocolBean protocol, ByteBuffer buffer, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>(noticePropertyBeans.size());
        for (NoticePropertyBean noticePropertyBean : noticePropertyBeans) {
            Object value = params.get(noticePropertyBean.getValue());
            result.put(noticePropertyBean.getKey(), value);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] 通道: {}, 目标端: {}, 数据: {}"
                    , protocol.getTraceID(), this.noticeBean.getTopic(), this.noticeBean.getTarget(), JSON.toJSONString(result));
        }

        String[] targets = this.noticeBean.getTarget().split(",");
        for (String _target : targets) {
            DownStream steam = new DownStream(this.uuid, _target, 1, result);
            try {
                Socket.writeAndFlush(this.noticeBean.getTopic(), steam);
            } catch (GroupNotFoundException e) {
                LOGGER.warn("[{}][{}] Group not found.", protocol.getTraceID(), noticeBean.getTopic());
            }
        }
        return null;
    }
}
