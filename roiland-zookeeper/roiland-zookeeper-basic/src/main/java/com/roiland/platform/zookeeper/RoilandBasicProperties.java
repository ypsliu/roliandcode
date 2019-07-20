package com.roiland.platform.zookeeper;

import com.roiland.platform.lang.IPUtils;
import com.roiland.platform.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 单例方法，初始化配置信息。
 * <p>使用：
 * <p>① 初始化配置信息：{@code new RoilandPropertyLoader(project)}
 * <p>② 配置信息调用：{@code RoilandPropertyLoader.getInstance().getProperties()}、 {@code RoilandPropertyLoader.getInstance().getValue(key)}
 * <p/>
 * 系统默认属性：project.name、log.path、local.address、log.level
 *
 * @author 杨昆
 */
public abstract class RoilandBasicProperties extends Properties {

    protected final List<PropertyListener> listeners = new CopyOnWriteArrayList<>();

    private static final long serialVersionUID = -6501904752843141961L;

    /**
     * config.properties 地址及区域设置 *
     */
    public static final String ZOOKEEPER_URLS = "zookeeper.urls";           // zookeeper配置中心的url（必须项，配置在config.properties文件中）
    public static final String DEPLOY_LOCATION = "deploy.location";         // 项目部署位置（必须项，配置在config.properties文件中）

    public static final char PATH_SEPARATOR = '/';                          // zookeeper目录分隔符
    public static final char NAME_SEPARATOR = '.';                          // zookeeper名字分隔符
    /**
     * Zookeeper目录设置 *
     */
    public static final String PLATFORM = "platform";                       // zookeeper根目录
    public static final String PLATFORM_ENVIRONMENT = "environment";        // 环境共通配置
    public static final String PLATFORM_COMMON = "common";                  // 全局共通配置
    public static final String PROJECT = "project";                         // 项目配置
    public static final String PRIVATE = "private";                         // 项目私人配置
    public static final String CUSTOM = "custom";                           // 项目定制配置
    public static final String CONFIG = "config";                           // 项目查找配置

    protected String project;                                               // 项目名称

    protected CuratorFramework client;

    protected static final Log LOGGER = LogFactory.getLog(RoilandBasicProperties.class);

    /**
     * Zookeeper默认属性设置*
     */
    protected static final String PROJECT_NAME = "project.name";
    protected static final String LOG_PATH = "log.path";
    protected static final String LOCAL_ADDRESS = "local.address";
    protected static final String LOG_LEVEL = "log.level";
    protected static final String KAFKA_TOPIC_LOG = "kafka.topic.log";
    protected static final String KAFKA_CLUSTER = "kafka.cluster";

    private static final Properties props = new Properties();

    //构造函数开始
    public RoilandBasicProperties(final String project) {
        this(project, new PropertyListener[]{new LoggerPropertyListener()});
    }

    public RoilandBasicProperties(final String project, PropertyListener[] listeners) {
        if (listeners != null) {
            for (PropertyListener listener : listeners) {
                this.listeners.add(listener);
            }
        }
        this.project = project;
        this.connect();            // 连接Zookeeper
        this.properties();         // 获取属性
        this.disconnect();         // 关闭Zookeeper连接
        notifyPropertyDone();
    }
    //构造函数结束

    private void connect() {
        if (client == null) {
            ResourceBundle bundle = ResourceBundle.getBundle("config");
            for (String key : bundle.keySet()) {
                props.setProperty(key, bundle.getString(key));
            }

            final String zkAddress = props.getProperty(ZOOKEEPER_URLS);
            if (!StringUtils.isEmpty(zkAddress)) {
                client = CuratorFrameworkFactory.builder()
                        .connectString(zkAddress)
                        .namespace(PLATFORM)
                        .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
                        .connectionTimeoutMs(60000).build();
            }
        }

        if (client.getState() == CuratorFrameworkState.LATENT || client.getState() == CuratorFrameworkState.STOPPED) {
            LOGGER.info("zookeeper client is ready to start");
            client.start();
        } else {
            LOGGER.info("zookeeper client is started");
        }
    }

    private void disconnect() {
        if (client != null && client.getState() == CuratorFrameworkState.STARTED) {
            LOGGER.info("zookeeper client is ready to close");
            client.close();
        } else {
            LOGGER.info("zookeeper client is not started.");
        }
    }

    private void properties() {
        final String ip = IPUtils.ip();
        Set<String> keys = props.stringPropertyNames();
        for (String key : keys) {
            setPropertyIfAbsent(key, props.getProperty(key), "config.properties");
        }
        setPropertyIfAbsent(PROJECT_NAME, project, "default");
        setPropertyIfAbsent(LOCAL_ADDRESS, ip, "default");

        final String domain = props.getProperty(DEPLOY_LOCATION);
        final String privatePath = PATH_SEPARATOR + PROJECT + PATH_SEPARATOR + project + PATH_SEPARATOR + PRIVATE;
        final String configPath = PATH_SEPARATOR + PROJECT + PATH_SEPARATOR + project + PATH_SEPARATOR + CONFIG;
        Set<String> allKeys = getAllKeys(new String[]{privatePath, configPath});

        Map<String, Boolean> roots = new LinkedHashMap<>();
        roots.put(PATH_SEPARATOR + PROJECT + PATH_SEPARATOR + project + PATH_SEPARATOR + CUSTOM + PATH_SEPARATOR + ip, false);
        roots.put(privatePath, false);
        roots.put(configPath, true);

        final String[] lookupPaths = new String[]{
                PATH_SEPARATOR + PLATFORM_ENVIRONMENT + PATH_SEPARATOR + domain,
                PATH_SEPARATOR + PLATFORM_COMMON,
        };

        for (Map.Entry<String, Boolean> entry : roots.entrySet()) {
            final String root = entry.getKey();
            final List<String> paths = children(root, null);
            final boolean isLookup = entry.getValue();
            for (String path : paths) {
                final int length = root.length() + 1;
                if (path.length() > length) {
                    //exception keyPath = index.do
                    final String keyPath = path.substring(length, path.length());
                    final String key = getKeyFromPath(keyPath);
                    if (!allKeys.contains(key)) {
                        //custom里包含不存在于config,private的key的时候，抛出异常
                        throw new UnsupportedOperationException("key in CUSTOM folder not exists in PRIVATE,CONFIG folders. Key:" + key);
                    }
                    if (isLookup) {
                        setPropertyIfAbsent(keyPath, lookupPaths);
                    } else {
                        setPropertyIfAbsent(keyPath, new String[]{root});
                    }
                    //如果还是不包含key，那就抛出异常吧
                    if (!super.containsKey(key)) {
                        throw new UnsupportedOperationException("value not found from zookeeper. Key:" + key);
                    }
                }
            }
        }
        setPropertyIfAbsent(LOG_PATH.replace(NAME_SEPARATOR, PATH_SEPARATOR), lookupPaths);
        setPropertyIfAbsent(LOG_LEVEL.replace(NAME_SEPARATOR, PATH_SEPARATOR), lookupPaths);
        setPropertyIfAbsent(KAFKA_TOPIC_LOG.replace(NAME_SEPARATOR, PATH_SEPARATOR),lookupPaths);
        setPropertyIfAbsent(KAFKA_CLUSTER.replace(NAME_SEPARATOR, PATH_SEPARATOR),lookupPaths);
    }

    private Set<String> getAllKeys(String[] paths) {
        Set<String> keys = new HashSet<>();
        for (String path : paths) {
            keys.addAll(getAllKeysByPath(path));
        }
        return keys;
    }

    private Set<String> getAllKeysByPath(String root) {
        final List<String> paths = children(root, null);
        final Set<String> keys = new HashSet<>();
        for (String path : paths) {
            final int length = root.length() + 1;
            if (path.length() > length) {
                final String keyPath = path.substring(length, path.length());
                final String key = getKeyFromPath(keyPath);
                keys.add(key);
            }
        }
        return keys;
    }

    private String getKeyFromPath(String keyPath) {
        return keyPath.replace(PATH_SEPARATOR, NAME_SEPARATOR);
    }

    private List<String> children(String root, String node) {
        final List<String> paths = new ArrayList<>();
        final String temp = node == null || "".equals(node) ? "" : (PATH_SEPARATOR + node);
        final String parent = root + temp;
        try {
            List<String> children = client.getChildren().forPath(parent);
            if (children == null || children.isEmpty()) {
                paths.add(parent);
            } else {
                for (String string : children) {
                    paths.addAll(this.children(parent, string));
                }
            }
        } catch (Exception e) {
        }
        return paths;
    }

    private void setPropertyIfAbsent(String key, String value, String fromPath) {
        if (!super.containsKey(key)) {
            setProperty(key, value, fromPath);
        }
    }

    /**
     * 根据路径获取zookeeper属性
     *
     * @param keyPath     key路径
     * @param lookupPaths 查找路径
     * @return 值
     */
    private void setPropertyIfAbsent(String keyPath, String[] lookupPaths) {
        final String key = getKeyFromPath(keyPath);
        for (String lookupPath : lookupPaths) {
            String path = lookupPath + PATH_SEPARATOR + keyPath;
            String value = getZKValue(path);
            if (value != null) {
                setPropertyIfAbsent(key, value, path);
                break;
            }
        }
    }

    public void setProperty(String key, String value, String fromPath) {
        super.setProperty(key, value);
        System.setProperty(key, value);
        notifyPropertySet(key, value, fromPath);
    }

    private void notifyPropertySet(String key, String value, String fromPath) {
        for (PropertyListener listener : listeners) {
            listener.onPropertySet(this, key, value, fromPath);
        }
    }

    private void notifyPropertyDone() {
        for (PropertyListener listener : listeners) {
            listener.onPropertyDone(this);
        }
    }

    /**
     * 根据路径获取zookeeper属性
     *
     * @param path key路径
     * @return 值
     */
    private String getZKValue(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat != null) {
                byte[] temp = client.getData().forPath(path);
                if (temp != null && temp.length > 0) {
                    return new String(temp);
                }
            }
        } catch (Exception e) {
            LOGGER.error("error:", e);
        }
        return null;
    }
}
