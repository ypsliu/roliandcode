package com.roiland.platform.examples.netty;

import com.roiland.platform.dbutils.DBConn;
import com.roiland.platform.dbutils.bean.DBConnBean;
import com.roiland.platform.examples.netty.protocol.trigger.OBDFrameTrigger;
import com.roiland.platform.socket.core.ProtocolBean;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by jeffy.yang on 16-6-15.
 */
public class NettyBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyBootstrap.class);

    public static void main(String[] args) throws InterruptedException {
//        new RoilandProperties("roiland-examples-netty-server");
        System.setProperty("service.id", "01234567890123456789012345678901");

        // DB方式解析
        List<DBConnBean> conns = new ArrayList<>(1);
        conns.add(new DBConnBean("template", "jdbc:mysql://127.0.0.1:3306/template_config?useunicode=true&characterencoding=utf8", "root", ""));
        DBConn.initialize(conns);

        final byte[] bytes = Base64.decodeBase64("CQAB+wQAjwEBEAMJDSYCF0ATAAAEAExyUAAFAAAAAAAGAAAAAAAHAAFfjwAoAAFfkAAJAAAAAAAUAAD96AAfAACvyAAkAAAD6AAlAAK/IAAQAAAv8wBQAAAnEABXAQBWAAAPoABSAADHOABTAADS8ABVAABtYABwAAAAAABxAAAAAAByAAAAAAARAAATiABDAAAABJWE");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length - 8);
        buffer.put(bytes[4]);
        buffer.put(bytes, 7, bytes.length - 9);
        buffer.flip();

        ProtocolBean protocol = new ProtocolBean(UUID.randomUUID().toString(), null, null, null, null, null, null, null);

        Map<String, Object> hash = new HashMap<>();
        hash.put("ei", "E0307FF0005959G5C");

        OBDFrameTrigger obdFrameTrigger = new OBDFrameTrigger();
        obdFrameTrigger.done(protocol, buffer, hash);

//        TemplateBehaviour behaviour = new TemplateBehaviour();
//        behaviour.done(protocol, buffer, hash);
//
//        try {
//            List<Map<String, Object>> _rows = ProtocolManager.getInstance().decode(buffer, new HashMap<String, Object>());
//            for (Map<String, Object> _row: _rows) {
//                NoticeManager.getInstance().send("010301", _row);
//            }
//        } catch (IllegalProtocolException e) {
//            e.printStackTrace();
//        }

//        NettyServerBootstrap.with(new IDataHandler() {
//            @Override
//            public void handle(RoilandChannel channel, ProtocolBean protocolBean) {
//                final byte[] bytes = protocolBean.getBody();
//                ByteBuffer buffer = ByteBuffer.allocate(bytes.length - 8);
//                buffer.put(bytes[4]);
//                buffer.put(bytes, 7, bytes.length - 9);
//                buffer.flip();
//
//                try {
//                    ProtocolManager.getInstance().decode(buffer, new HashMap<String, Object>());
//                } catch (IllegalProtocolException e) {
//                    LOGGER.warn("[{}][{}] illegal protocol. ", protocolBean.getTraceID(), protocolBean.getSource(), e);
//                }
//            }
//        }).build().bind(7000).addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                LOGGER.info("bind to " + 7000, future.cause());
//            }
//        });
    }
}
