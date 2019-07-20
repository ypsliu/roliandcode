package com.roiland.platform.zookeeper.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;

import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/7/27
 */
public class LogEventWrapper implements LogEvent {

    private final LogEvent event;

    private final String location;
    private final String ip;
    private final String project;
    public LogEventWrapper(LogEvent event,final String location,final String ip,final String project) {
        if(event instanceof MutableLogEvent){
            event = ((MutableLogEvent)event).createMemento();
        }
        this.event = event;
        this.location = location;
        this.ip = ip;
        this.project = project;
    }

    @Override
    public Map<String, String> getContextMap() {
        return event.getContextMap();
    }

    @Override
    public ThreadContext.ContextStack getContextStack() {
        return event.getContextStack();
    }

    @Override
    @JsonIgnore
    public String getLoggerFqcn() {
        return event.getLoggerFqcn();
    }

    @Override
    public Level getLevel() {
        return event.getLevel();
    }

    @Override
    public String getLoggerName() {
        return event.getLoggerName();
    }

    @Override
    public Marker getMarker() {
        return event.getMarker();
    }

    @Override
    public Message getMessage() {
        return event.getMessage();
    }

    @Override
    public long getTimeMillis() {
        return event.getTimeMillis();
    }

    @Override
    public StackTraceElement getSource() {
        return event.getSource();
    }

    @Override
    public String getThreadName() {
        return event.getThreadName();
    }

    @Override
    public long getThreadId() {
        return event.getThreadId();
    }

    @Override
    public int getThreadPriority() {
        return event.getThreadPriority();
    }

    @Override
    public Throwable getThrown() {
        return event.getThrown();
    }

    @Override
    public ThrowableProxy getThrownProxy() {
        return event.getThrownProxy();
    }

    @Override
    @JsonIgnore
    public boolean isEndOfBatch() {
        return event.isEndOfBatch();
    }

    @Override
    public boolean isIncludeLocation() {
        return event.isIncludeLocation();
    }

    @Override
    public void setEndOfBatch(boolean endOfBatch) {
        event.setEndOfBatch(endOfBatch);
    }

    @Override
    public void setIncludeLocation(boolean locationRequired) {
        event.setIncludeLocation(locationRequired);
    }

    @Override
    public long getNanoTime() {
        return event.getNanoTime();
    }

    public String getLocation() {
        return location;
    }

    public String getProject() {
        return project;
    }

    public String getIp() {
        return ip;
    }
}
