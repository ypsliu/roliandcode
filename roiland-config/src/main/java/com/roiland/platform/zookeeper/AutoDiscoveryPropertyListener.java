package com.roiland.platform.zookeeper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;
import java.util.Map;

/**
 * 注册中心配置改动自动发现
 * 实验性的功能
 * 打开此实验性功能，需要以下步骤
 * 1. 注释掉{@link RoilandProperties#disconnect()}方法中的内容，改成注册{@link Runtime#addShutdownHook}关闭zookeeper连接
 * 2. 在其子工程roiland-zookeeper-log4j或者roiland-zookeeper-slf4j中,使用以下代码片段注册监听器
 * {@code
 * RoilandProperties properties = new RoilandProperties("AAAA",new PropertyListener[]{new AutoDiscoveryPropertyListener()});
 * }
 * @author leon.chen
 * @since 2016/6/3
 */
public class AutoDiscoveryPropertyListener extends PropertyListener.Adaptor {

    private static final Log LOGGER = LogFactory.getLog(LoggerPropertyListener.class);

    @Override
    public void onPropertySet(RoilandProperties properties, String key, String value, String from) {
        LOGGER.info(key + " = " + value + " ,lookup from " + from);
        try {
            Context context = new Context(properties, this, key, value, from);
            //排除default以及config.properties
            if (from.startsWith("/")) {
                properties.client.getData().usingWatcher(watcher(context)).inBackground(backgroundCallback(), context).forPath(from);
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
        }
    }

    @Override
    public void onPropertyDone(RoilandProperties properties) {
        Map<String, Class<? extends LogProvider>> providers = properties.providers;
        if (providers.isEmpty()) {
            LOGGER.info("loaded log plugin is empty");
            return;
        }
        if (providers.size() > 1) {
            throw new UnsupportedOperationException("multi log plugin loaded:" + properties.toString());
        }
        //providers.size = 1
        try {
            for (Map.Entry<String, Class<? extends LogProvider>> entry : providers.entrySet()) {
                final String name = entry.getKey();
                final Class<? extends LogProvider> value = entry.getValue();
                LOGGER.info("loaded log plugin name:" + name + ",class:" + value);
                LogProvider logProvider = value.newInstance();
                logProvider.restart(properties);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("log plugin instance error", e);
        }
    }

    public static CuratorWatcher watcher(final Context context) {
        return new CuratorWatcher() {
            @Override
            public void process(WatchedEvent watchedEvent) throws Exception {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeDataChanged) {
                    LOGGER.info("node changed:" + watchedEvent.getPath());
                    context.properties.client.getData().usingWatcher(this).inBackground(backgroundCallback(), context).forPath(watchedEvent.getPath());
                }
            }
        };
    }

    public static BackgroundCallback backgroundCallback() {
        return new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                if (event.getType() == CuratorEventType.GET_DATA) {
                    Context context = (Context) event.getContext();
                    byte[] bytes = client.getData().forPath(event.getPath());
                    //不要再触发onPropertySet了,否则继续watch就死循环了
                    final String key = context.key;
                    final String value = new String(bytes);
                    if (value == null || value.length() == 0) {
                        LOGGER.warn("value not found from zookeeper. Key:" + key + ",from:" + context.from);
                        return;
                    }
                    context.properties.setProperty(key, value);
                    System.setProperty(key, value);
                    LOGGER.info(key + " = " + value + " ,lookup from " + context.from);
                    //notify other listener expect this
                    List<PropertyListener> listeners = context.properties.listeners;
                    for (PropertyListener listener : listeners) {
                        if (listener != context.listener) {
                            listener.onPropertySet(context.properties, key, value, context.from);
                        }
                    }
                }
            }
        };
    }

    private static class Context {
        private RoilandProperties properties;
        private PropertyListener listener;
        private String key;
        private String value;
        private String from;

        public Context(RoilandProperties properties, PropertyListener listener, String key, String value, String from) {
            this.properties = properties;
            this.listener = listener;
            this.key = key;
            this.value = value;
            this.from = from;
        }

        public RoilandProperties getProperties() {
            return properties;
        }

        public void setProperties(RoilandProperties properties) {
            this.properties = properties;
        }

        public PropertyListener getListener() {
            return listener;
        }

        public void setListener(PropertyListener listener) {
            this.listener = listener;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}
