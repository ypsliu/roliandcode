package com.roiland.platform.restful.chain;

import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.restful.Constants;
import com.roiland.platform.restful.chain.config.FilterConfig;
import com.roiland.platform.restful.chain.config.PathFilterConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeffy.yang on 2016/3/17.
 */
public class ChainSingleton {

    private static final Log LOGGER = LogFactory.getLog(ChainSingleton.class);

    // 过滤器配置
    private FilterConfig filterConfig = null;
    // 默认路径配置
    private PathFilterConfig defaultConfig = null;
    // 路径配置
    private PathFilterConfig pathConfig = null;

    private Map<String, FilterChain> chains = null;

    private static ChainSingleton ourInstance = new ChainSingleton();

    public static ChainSingleton getInstance() {
        return ourInstance;
    }

    private ChainSingleton() {
    }

    public FilterChain findChain(String path) {
        // 初始化过滤器配置
        if (filterConfig == null) {
            this.filterConfig = new FilterConfig();
        }

        if (this.chains == null) {
            this.chains = Collections.synchronizedMap(new HashMap<String, FilterChain>());

            this.buildPathConfig(defaultConfig);
            this.buildPathConfig(pathConfig);
        }

        if (chains.containsKey(path)) {
            return chains.get(path);
        } else if (chains.containsKey(Constants.DEFAULT)) {
            return chains.get(Constants.DEFAULT);
        } else {
            return null;
        }
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void setDefaultConfig(PathFilterConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public void setPathConfig(PathFilterConfig pathConfig) {
        this.pathConfig = pathConfig;
    }

    private void buildPathConfig(PathFilterConfig config) {
        if (config != null) {
            final Map<String, String> configs = config.findAll();
            for (Map.Entry<String, String> temp : configs.entrySet()) {
                final String key = temp.getKey();
                final String value = temp.getValue();
                if (StringUtils.isEmpty(value)) {
                    //by leon.chen
                    //bug.如果已经显示的配置此路径没有过滤器的时候，不应该调用default过滤器。因为有些path就是不应该有过滤的，如登录等
                    this.chains.put(temp.getKey(), null);
                    continue;
                }

                final String[] filters = value.split(",");
                FilterChain chain = null;
                for (String filter : filters) {
                    FilterChain tmpChain = findFromFilterConfig(filter);

                    // 设置过滤链
                    if (tmpChain == null) {
                        LOGGER.error("无法识别过滤器别名：" + filter);
                    } else if (chain == null) {
                        chain = tmpChain;
                    } else {
                        chain.setChain(tmpChain);
                    }
                }

                if (chain != null) this.chains.put(temp.getKey(), chain);
            }
        }
    }

    // 尝试从filterConfig中获取过滤器
    private FilterChain findFromFilterConfig(String name) {
        FilterChain  chain = null;
        if (filterConfig.containsKey(StringUtils.trim(name))) {
            Class<? extends FilterChain> clazz = filterConfig.get(StringUtils.trim(name));
            try {
                chain = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("loading " + clazz.getName() + " fail.", e);
            }
        } else if (defaultConfig.containsKey(name)){
            String[] filters = defaultConfig.get(name).split(",");
            for (String filter : filters) {
                FilterChain temp = findFromFilterConfig(filter);
                if (chain == null) {
                    chain = temp;
                } else {
                    chain.setChain(temp);
                }
            }
        }
        return chain;
    }
}
