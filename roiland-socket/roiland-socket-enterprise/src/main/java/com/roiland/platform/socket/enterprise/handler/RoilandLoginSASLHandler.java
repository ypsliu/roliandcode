package com.roiland.platform.socket.enterprise.handler;


import com.alibaba.fastjson.JSON;
import com.roiland.platform.socket.core.*;
import com.roiland.platform.socket.enterprise.utils.SASLUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>企业客户端登录处理</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2017/07/30
 */
@ChannelHandler.Sharable
public class RoilandLoginSASLHandler extends SimpleChannelInboundHandler<ProtocolBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandLoginSASLHandler.class);

    // JSON标签
    private final String TAG_TYPE = "TYPE";
    private final String TAG_ST = "ST";
    private final String TAG_MSG = "MSG";
    private final String TAG_KS = "KS";

    private final String TYPE_LOGIN = "LOGIN";
    private final String TYPE_LOGOUT = "LOGOUT";

    private final String USERNAME;
    private final String PASSWORD;

    private IDataCallback callback = null;

    public RoilandLoginSASLHandler(String username, String password, IDataCallback callback) {
        this.USERNAME = username;
        this.PASSWORD = password;
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        // 封装登陆协议
        Map<String, String> auth = new HashMap<>(8);
        auth.put("VER", "1.0");
        auth.put("CMD", "LOGIN");
        auth.put("SEQ", "");
        auth.put("M5", "");
        auth.put("MSG", ""); //附加信息
        auth.put("UNAME", USERNAME);
        auth.put("PW", SASLUtil.saltPwd(USERNAME, SASLUtil.hashPwd(USERNAME, PASSWORD)));
        auth.put("MECH", "CRAM-MD5");
        // 下发鉴权指令
        new RoilandChannel(ctx.channel()).writeAndFlush(new DownStream(USERNAME, "0", 1, auth));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        RoilandChannelManager.getInstance().disconnect(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolBean protocol) throws Exception {
        if (RoilandChannelManager.getInstance().contains(ctx.channel())) {
            // 通道已注册
            ctx.fireChannelRead(protocol);
            return;
        }

        final String traceID = protocol.getTraceID();
        byte[] body = protocol.getBody();
        if (body[0] == '{' && body[body.length - 1] == '}') {
            /**
             * 协议体以‘{}’开头结尾，初步判断该协议为JSON格式协议，并对其进行转换。
             */
            LOGGER.debug("{} trying to register channel", ctx.channel());
            final Map result;
            try {
                result = JSON.parseObject(new String(body), Map.class);
            } catch (Exception e) {
                LOGGER.warn("{}[{}] illegal json format. ", ctx.channel(), traceID, e);
                return;
            }

            if (!result.containsKey(TAG_TYPE)
                    || !result.containsKey(TAG_ST)
                    || !result.containsKey(TAG_MSG)) {
                LOGGER.warn("{}[{}] illegal argument `TYPE`, `ST` or `MSG`", ctx.channel(), traceID);
                return;
            }

            // 登录类型  -  LOGIN:登陆, LOGOUT:登出
            final String TYPE = result.get(TAG_TYPE).toString();
            if (TYPE != null && TYPE_LOGIN.equals(TYPE)) {
                final String st = result.get(TAG_ST).toString();

                if (st != null && st.equals("0")) {
                    // 登陆成功, 添加至通道中
                    String group = RoilandChannelManager.getInstance().connect(ctx.channel());
                    RoilandGroupManager.getInstance().put(group, ctx.channel());

                    // 回调
                    String msg = result.get(this.TAG_MSG).toString();
                    String ks = getKs(msg);
                    result.put(TAG_KS, ks);
                    this.callback.callback(ctx.channel(), result, null);
                }
            } else if (TYPE != null && TYPE_LOGOUT.equals(TYPE)) {
                LOGGER.info("{}[{}] ignore `LOGOUT`", ctx.channel(), traceID);
            } else {
                LOGGER.info("{}[{}] unknown property `TYPE`", ctx.channel(), traceID);
            }
        } else {
            LOGGER.info("{}[{}] access fail", ctx.channel(), traceID);
        }
    }

    private String getKs(String msg) {
        String challenge = new String(Base64.decodeBase64(msg));
        String[] str = challenge.split(",");
        if (str == null || str.length != 3) {
            throw new UnsupportedOperationException();
        }
        int index = str[0].indexOf("=");
        return str[0].substring(index + 1);
    }
}
