package com.roiland.platform.template.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.template.core.AbstractInvoker;
import com.roiland.platform.template.core.Invoker;
import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.support.TemplateSupport;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.roiland.platform.template.core.support.MapperSupport.readValue;

/**
 * 非线程安全类
 *
 * @author leon.chen
 * @since 2016/6/13
 */
public class SocketInvoker extends AbstractInvoker {

    private static final Log LOGGER = LogFactory.getLog(SocketInvoker.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private SocketInvoker(Map<String, Object> globalParam, ResourceBean resourceBean) {
        super(globalParam, resourceBean);
    }

    public static SocketInvokerBuilder create(String template) throws IOException {
        return new SocketInvokerBuilder().template(template);
    }

    public static SocketInvokerBuilder create(byte[] template) throws IOException {
        return new SocketInvokerBuilder().template(template);
    }

    public static SocketInvokerBuilder create(InputStream template) throws IOException {
        return new SocketInvokerBuilder().template(template);
    }

    public static SocketInvokerBuilder create(Reader template) throws IOException {
        return new SocketInvokerBuilder().template(template);
    }

    public static SocketInvokerBuilder create(File template) throws IOException {
        return new SocketInvokerBuilder().template(template);
    }

    public static SocketInvokerBuilder create(ResourceBean template) throws IOException {
        return new SocketInvokerBuilder().template(template);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    public static class SocketInvokerBuilder {

        private SocketInvokerBuilder() {

        }

        private Map<String, Object> globalParam;

        private ResourceBean resourceBean;

        private TemplateSupport templateSupport = new TemplateSupport();

        public SocketInvokerBuilder context(Map<String, Object> context) {
            this.templateSupport.putContext(context);
            return this;
        }

        public SocketInvokerBuilder context(TemplateSupport templateSupport) {
            this.templateSupport = templateSupport;
            return this;
        }

        public SocketInvokerBuilder context(String key, Object value) {
            this.templateSupport.putContext(key, value);
            return this;
        }

        public SocketInvokerBuilder dateFormat(String pattern) {
            this.templateSupport.setDateFormat(pattern);
            return this;
        }

        private SocketInvokerBuilder template(String template) throws IOException {
            this.resourceBean = templateSupport.parseTemplate(template);
            return this;
        }

        private SocketInvokerBuilder template(byte[] template) throws IOException {
            this.resourceBean = templateSupport.parseTemplate(template);
            return this;
        }

        private SocketInvokerBuilder template(InputStream template) throws IOException {
            this.resourceBean = templateSupport.parseTemplate(template);
            return this;
        }

        private SocketInvokerBuilder template(Reader template) throws IOException {
            this.resourceBean = templateSupport.parseTemplate(template);
            return this;
        }

        private SocketInvokerBuilder template(ResourceBean template) throws IOException {
            this.resourceBean = template;
            return this;
        }

        private SocketInvokerBuilder template(File template) throws IOException {
            return template(new BufferedInputStream(new FileInputStream(template)));
        }

        public SocketInvokerBuilder globalParam(String param) throws IOException {
            this.globalParam = readValue(param, Map.class);
            return this;
        }

        public SocketInvokerBuilder globalParam(byte[] param) throws IOException {
            this.globalParam = readValue(param, Map.class);
            return this;
        }

        public SocketInvokerBuilder globalParam(InputStream param) throws IOException {
            this.globalParam = readValue(param, Map.class);
            return this;
        }

        public SocketInvokerBuilder globalParam(Reader param) throws IOException {
            this.globalParam = readValue(param, Map.class);
            return this;
        }

        public SocketInvokerBuilder globalParam(File param) throws IOException {
            return globalParam(new BufferedInputStream(new FileInputStream(param)));
        }

        public SocketInvokerBuilder globalParam(Map<String, Object> param) throws IOException {
            this.globalParam = param;
            return this;
        }

        public Invoker build(boolean preCompile) {
            if (globalParam == null || resourceBean == null) {
                throw new IllegalArgumentException("globalParam is null or resourceBean is null");
            }
            //预编译模板
            if (preCompile) templateSupport.preCompile(resourceBean);
            SocketInvoker invoker = new SocketInvoker(globalParam, resourceBean) {
                protected TemplateSupport getTemplateSupport() {
                    return templateSupport;
                }
            };
            return invoker;
        }
    }

    //可以线程外
    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    //可以线程外
    private final AtomicReference<Channel> channel = new AtomicReference<>(null);

    //可以线程外
    private final AtomicBoolean closed = new AtomicBoolean(false);

    //线程外
    private final EventLoopGroup workLoopGroup = new NioEventLoopGroup();

    private final long heartBeatPeriod = 60 * 1000;

    private final Timer timer = new Timer();

    private static final String heartBeat = "RG,9,0,0,0,0,0";

    @Override
    protected Object handle(Map<String, Object> object, ResourceBean bean, final InvokerCallback callback) throws Exception {
        Map<String, Object> map = getParam(bean);
        final String host = (String) object.get("host");
        final Integer port = (Integer) object.get("port");
        //sasl登录用
        final Boolean ack = (Boolean) getParamByName("#ack", bean);
        final boolean hasAck = ack == null ? false : ack;
        final String username = (String) getParamByName("#username", bean);
        final String password = (String) getParamByName("#password", bean);
        final String type = (String) map.get("type");
        final String source = (String) map.get("source");
        final String target = (String) map.get("target");
        final String body = (String) map.get("body");
        final boolean needAuth = needSaslAuth(username, password);
        final AtomicBoolean isAuth = new AtomicBoolean(!needAuth);
        if (isStarted.compareAndSet(false, true)) {
            connect(callback, host, port, hasAck, username, password, isAuth);
        }
        while (true) {
            if (this.channel.get() != null && isAuth.get()) break;
            if (closed.get()) return new Object();
            //能被interrupt打断
            Thread.sleep(1000);
        }
        final String protocol = stringProtocolBean(source, target, type, body);
        this.channel.get().writeAndFlush(protocol);
        String rs = "no ack";
        if (callback != null && !hasAck) {
            callback.callback(rs);
        }
        return rs;
    }

    private void connect(final InvokerCallback callback, final String host, final Integer port, final boolean hasAck, final String username, final String password, final AtomicBoolean isAuth) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.option(ChannelOption.SO_RCVBUF, 256 * 1024 * 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 256 * 1024 * 1024);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RoilandDecoder());
                ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
                ch.pipeline().addLast(new SimpleChannelInboundHandler<byte[]>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
                        if (isAuth.get()) {
                            ctx.pipeline().remove(this);
                            ctx.fireChannelRead(msg);
                        } else {
                            String ack = new String(msg);
                            if(ack.equals(heartBeat)){
                                //处理心跳
                                ctx.fireChannelReadComplete();
                                return;
                            }
                            try {
                                String[] ackAry = ack.split(",");
                                String body = ackAry[5];
                                byte[] ary = DatatypeConverter.parseBase64Binary(body);
                                Map<String, String> result = mapper.readValue(ary, Map.class);
                                final String type = result.get("TYPE");
                                final String st = result.get("ST");
                                // LOGIN:登陆 ST=0成功
                                if (type != null && "LOGIN".equals(type) && st != null && st.equals("0")) {
                                    //登录成功
                                    LOGGER.info("login success: " + result);
                                    isAuth.compareAndSet(false, true);
                                    ctx.pipeline().remove(this);
                                } else {
                                    LOGGER.error("login failed: " + result);
                                }
                            } catch (Throwable e) {
                                LOGGER.error("login failed: " + ack+","+e.getMessage());
                            } finally {
                                ctx.fireChannelReadComplete();
                            }
                        }
                    }
                });
                ch.pipeline().addLast(new SimpleChannelInboundHandler<byte[]>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
                        String ack = new String(msg);
                        if (!ack.equals(heartBeat) && callback != null && hasAck) {
                            callback.callback(new String(msg));
                        }
                    }
                });
            }
        });
        ChannelFuture future = bootstrap.connect(host, port).syncUninterruptibly().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.cause() != null) {
                    LOGGER.warn("connect to " + host + ":" + port + " fail", future.cause());
                    return;
                }
                if (isAuth.get()) {
                    //非登录鉴权连接
                    return;
                }
                final Channel channel = future.channel();
                // 登陆协议
                Map<String, String> auth = new HashMap<>(8);
                auth.put("VER", "1.0");
                auth.put("CMD", "LOGIN");
                auth.put("SEQ", "");
                auth.put("M5", "");
                auth.put("MSG", ""); //附加信息
                auth.put("UNAME", username);
                auth.put("PW", SASLUtil.saltPwd(username, SASLUtil.hashPwd(username, password)));
                auth.put("MECH", "CRAM-MD5");
                String json = mapper.writeValueAsString(auth);
                String b64json = DatatypeConverter.printBase64Binary(json.getBytes());
                // 下发鉴权指令
                String loginReq = stringProtocolBean(username, "0", "1", b64json);
                channel.writeAndFlush(loginReq);
                LOGGER.info("connect to " + host + ":" + port + " success. send login reqeust:" + loginReq);
            }
        });
        final Channel channel = future.channel();
        //定时心跳
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                channel.writeAndFlush(heartBeat);
            }
        }, heartBeatPeriod, heartBeatPeriod);
        this.channel.compareAndSet(null, channel);
    }

    private boolean needSaslAuth(String username, String password) {
        return StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password);
    }

    private String stringProtocolBean(String source, String target, String type, String protocolBodyStr) {
        String temp = source + "," + target + "," + type + "," + protocolBodyStr;
        int crcCode = CRC16Util.crc(temp.getBytes());
        int len = source.length() + target.length() + type.length() + protocolBodyStr.length() + ("" + crcCode).length() + 4;
        return "RG," + len +
                "," + source +
                "," + target +
                "," + type +
                "," + protocolBodyStr +
                "," + crcCode;
    }

    /**
     * 另一个线程调用
     */
    @Override
    public void close() {
        if (!this.closed.compareAndSet(false, true)) {
            return;
        }
        timer.cancel();
        try {
            Future<?> future = workLoopGroup.shutdownGracefully();
            future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getParam(ResourceBean bean) {
        Map<String, Object> map = new HashMap<>();
        List<String> paramNames = bean.getParamName();
        for (int i = 0; i < paramNames.size(); i++) {
            if (!paramNames.get(i).startsWith("#")) {
                map.put(bean.getParamName().get(i), bean.getParamValue().get(i));
            }
        }
        return map;
    }

    private Object getParamByName(String name, ResourceBean bean) {
        List<String> paramNames = bean.getParamName();
        for (int i = 0; i < paramNames.size(); i++) {
            final String paramName = paramNames.get(i);
            if (name.equals(paramName)) {
                return bean.getParamValue().get(i);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "SocketInvoker channel:" + channel.toString();
    }
}
