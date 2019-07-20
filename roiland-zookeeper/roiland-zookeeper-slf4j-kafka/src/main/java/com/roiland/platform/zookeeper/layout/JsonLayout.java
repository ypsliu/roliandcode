package com.roiland.platform.zookeeper.layout;

import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/7/19
 */
public class JsonLayout extends LayoutBase<ILoggingEvent> {

    private ThrowableHandlingConverter throwableProxyConverter = new ThrowableProxyConverter();

    private ObjectWriter writer = new ObjectMapper().writer();

    private String location;

    private String ip;

    private String project;

    @Override
    public void start() {
        super.start();
        throwableProxyConverter.start();
        ip = System.getProperty("local.address");
        project = System.getProperty("project.name");
        location = System.getProperty("deploy.location");
    }

    @Override
    public void stop() {
        super.stop();
        throwableProxyConverter.stop();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        //兼容log4j2 排除了fqcn
        Map<String, Object> map = new HashMap<>(10,1);
        map.put("timeMillis", event.getTimeStamp());
        map.put("thread", event.getThreadName());
        map.put("level", String.valueOf(event.getLevel()));
        map.put("loggerName", event.getLoggerName());
        map.put("message", event.getFormattedMessage());
        map.put("threadId", Thread.currentThread().getId());
        map.put("threadPriority", Thread.currentThread().getPriority());
        addThrowableInfo("throwable", event, map);
        if(ip!=null){
            map.put("ip", ip);
        }
        if(location!=null){
            map.put("location", location);
        }
        if(project!=null){
            map.put("project", project);
        }
        try {
            return writer.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    protected void addThrowableInfo(String fieldName, ILoggingEvent value, Map<String, Object> map) {
        if (value != null) {
            IThrowableProxy throwableProxy = value.getThrowableProxy();
            if (throwableProxy != null) {
                String ex = throwableProxyConverter.convert(value);
                if (ex != null && !ex.equals("")) {
                    map.put(fieldName, ex);
                }
            }
        }
    }
}
