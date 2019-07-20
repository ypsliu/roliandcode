package com.roiland.platform.zookeeper.appender;


import com.roiland.platform.zookeeper.wrapper.LogEventWrapper;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.util.StringEncoder;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Sends log events to an Apache Kafka topic.
 */
@Plugin(name = "RoilandKafka", category = Node.CATEGORY, elementType = Appender.ELEMENT_TYPE, printObject = true)
public final class RoilandKafkaAppender extends AbstractAppender {

    @PluginFactory
    public static RoilandKafkaAppender createAppender(
            @PluginElement("Layout") final Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @Required(message = "No name provided for KafkaAppender") @PluginAttribute("name") final String name,
            @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) final boolean ignoreExceptions,
            @PluginAttribute(value = "sync", defaultBoolean = false) final boolean sync,
            @Required(message = "No topic provided for KafkaAppender") @PluginAttribute("topic") final String topic,
            @PluginConfiguration final Configuration config,
            @PluginAttribute("failover") final String failover,
            @PluginElement("Properties") final Property[] properties) {
        final RoilandKafkaManager kafkaManager = new RoilandKafkaManager(name, topic, properties);
        return new RoilandKafkaAppender(name, layout, filter, ignoreExceptions, sync, kafkaManager, config, failover);
    }

    private final RoilandKafkaManager manager;

    private final boolean sync;

    private final String location;

    private final String ip;

    private final String project;

    private final Configuration config;

    private final String failover;

    private AppenderControl failoverAppender;

    private RoilandKafkaAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final boolean sync, final RoilandKafkaManager manager, Configuration config, String failover) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
        this.sync = sync;
        this.ip = System.getProperty("local.address");
        this.location = System.getProperty("deploy.location");
        this.project = System.getProperty("project.name");
        this.config = config;
        this.failover = failover;
    }

    @Override
    public void append(final LogEvent event) {
        if (event.getLoggerName().startsWith("org.apache.kafka")) {
            LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), getName());
        } else {
            byte[] data;
            try {
                final Layout<? extends Serializable> layout = getLayout();
                LogEvent wrapperEvent = new LogEventWrapper(event, location, ip, project);
                if (layout != null) {
                    if (layout instanceof SerializedLayout) {
                        final byte[] header = layout.getHeader();
                        final byte[] body = layout.toByteArray(wrapperEvent);
                        data = new byte[header.length + body.length];
                        System.arraycopy(header, 0, data, 0, header.length);
                        System.arraycopy(body, 0, data, header.length, body.length);
                    } else {
                        data = layout.toByteArray(wrapperEvent);
                    }
                } else {
                    data = StringEncoder.toBytes(wrapperEvent.getMessage().getFormattedMessage(), StandardCharsets.UTF_8);
                }
                manager.send(data, sync, failoverAppender, wrapperEvent);
            } catch (final Exception e) {
                if (failoverAppender != null) {
                    failoverAppender.callAppender(event);
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();
        final Map<String, Appender> map = config.getAppenders();
        if (failover != null) {
            final Appender appender = map.get(failover);
            if (appender != null) {
                failoverAppender = new AppenderControl(appender, null, null);
            } else {
                LOGGER.error("Unable to locate failover Appender " + failover);
            }
        }
        manager.startup();
    }

    @Override
    public void stop() {
        super.stop();
        manager.release();
    }

    private static final ErrorHandler errorHandler = new ErrorHandler() {
        @Override
        public void error(String msg) {
        }

        @Override
        public void error(String msg, Throwable t) {
        }

        @Override
        public void error(String msg, LogEvent event, Throwable t) {
        }
    };

    @Override
    public ErrorHandler getHandler() {
        return errorHandler;
    }

    @Override
    public String toString() {
        return "RoilandKafkaAppender{" +
                "name=" + getName() +
                ", state=" + getState() +
                ", topic=" + manager.getTopic() +
                '}';
    }
}
