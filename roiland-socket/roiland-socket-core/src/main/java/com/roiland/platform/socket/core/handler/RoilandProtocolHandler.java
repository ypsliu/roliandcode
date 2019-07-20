package com.roiland.platform.socket.core.handler;

import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.monitor.basic.extract.Meter;
import com.roiland.platform.monitor.basic.transform.InfoBean;
import com.roiland.platform.socket.core.Constants;
import com.roiland.platform.socket.core.ProtocolBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * <p>协议解析处理</p>
 *
 * @author 杨昆
 * @version 3.0.1
 * @since 2016/07/30
 */
public class RoilandProtocolHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoilandProtocolHandler.class);

    private final String PROJECT_NAME;
    private final String ADDRESS;
    private final String LOCATION;
    private final Boolean NEED_MONITOR;

    public RoilandProtocolHandler() {
        this.PROJECT_NAME = System.getProperty(Constants.CONFIG_PROJECT_NAME);
        this.ADDRESS = System.getProperty(Constants.CONFIG_LOCAL_ADDRESS);
        this.LOCATION = System.getProperty(Constants.CONFIG_DEPLOY_LOCATION);

        String monitor = System.getProperty(Constants.CONFIG_MONITOR);
        this.NEED_MONITOR = StringUtils.isEmpty(monitor) ? Boolean.TRUE : Boolean.valueOf(monitor);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {
        final String remote = ctx.channel().remoteAddress().toString();
        final String local = ctx.channel().localAddress().toString();

        final String protocol = new String(data);
        final String[] temp = protocol.split(",");
        final String header = temp[0];
        final String length = temp[1];
        final String source = temp[2];
        final String target = temp[3];
        final String type = temp[4];
        final String body = temp[5];
        final String crc = temp[6];

        if (StringUtils.isEmpty(header)
                || StringUtils.isEmpty(length)
                || StringUtils.isEmpty(source)
                || StringUtils.isEmpty(target)
                || StringUtils.isEmpty(type)
                || StringUtils.isEmpty(body)
                || StringUtils.isEmpty(crc)) {
            // 协议外层格式校验
            LOGGER.error("protocol invalid. {}", protocol);
            this.sendMonitor(remote, local, false, data.length);
        } else {
            // 产生唯一追踪ID
            String trace = UUID.randomUUID().toString();
            LOGGER.info("{}[{}] {}", ctx.channel().toString(), trace, protocol);

            // 封装消息体
            ProtocolBean objProtocolBean = new ProtocolBean(
                    trace,
                    header,
                    Integer.valueOf(length),
                    source,
                    target,
                    Integer.valueOf(type),
                    "0".equals(body) ? new byte[]{'0'} : Base64.decodeBase64(body),
                    Integer.valueOf(crc),
                    protocol
            );
            this.sendMonitor(remote, local, true, data.length);
            // 触发下一级处理
            ctx.fireChannelRead(objProtocolBean);
        }
    }

    /**
     * 发送监控数据
     *
     * @param remote    远程地址
     * @param local     本地地址
     * @param isSuccess 处理结果。true：成功，false：失败
     * @param size      数据大小
     */
    private void sendMonitor(String remote, String local, Boolean isSuccess, int size) {
        if (NEED_MONITOR) {
            Meter.getInstance().mark(InfoBean.TYPE.SOCKET, PROJECT_NAME, ADDRESS, Constants.METHODS.INPUT.name(), LOCATION, null, remote, local, isSuccess ? 1 : 0, isSuccess ? 0 : 1, size, 0);
        }
    }
}
