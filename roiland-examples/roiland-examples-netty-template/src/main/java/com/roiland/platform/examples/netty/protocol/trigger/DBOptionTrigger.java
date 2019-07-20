package com.roiland.platform.examples.netty.protocol.trigger;

import com.roiland.platform.cache.Cache;
import com.roiland.platform.cache.CacheBean;
import com.roiland.platform.dbutils.DBConn;
import com.roiland.platform.dbutils.bean.DBConnBean;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.model.bean.*;
import com.roiland.platform.examples.netty.protocol.model.dao.DBDao;
import com.roiland.platform.examples.netty.protocol.model.dao.DBOptionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/14
 */
public class DBOptionTrigger implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBOptionTrigger.class);

    private DBDao dbDao = new DBDao();
    private DBOptionDao dbOptionDao = new DBOptionDao();

    private Map<String, DBBean> dbBeanMap = new HashMap<>();
    private Map<String, DBBiz> dbBizMap = new HashMap<>();

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof OBDBean) {
            OBDBean obdBean = (OBDBean) arg;
            List<DBOptionBean> dbOptionBeans = this.dbOptionDao.findByOBD(obdBean.getUuid());

            LOGGER.info("=============== [{}]数据操作加载 ===============", obdBean.getCommand());
            for (DBOptionBean dbOptionBean: dbOptionBeans) {
                final String DBID = dbOptionBean.getDbid();
                final DBBean dbBean;
                if (this.dbBeanMap.containsKey(DBID)) {
                    dbBean = this.dbBeanMap.get(DBID);
                } else {
                    dbBean = this.dbDao.findByID(dbOptionBean.getDbid());
                    this.dbBeanMap.put(DBID, dbBean);
                }

                if ("mysql".equalsIgnoreCase(dbBean.getType())) {
                    initMySQL(dbBean);
                } else if ("redis".equalsIgnoreCase(dbBean.getType())) {
                    initRedis(dbBean);
                }

                final String OPTION = dbOptionBean.getOption();
                LOGGER.info("数据 => 类型: {}, 库: {}, 表: {}, 操作: {}", dbBean.getType(), dbBean.getSchema(), dbBean.getTable(), OPTION);
                List<DBColumnBean> dbColumnBeans = this.dbOptionDao.findColumns(dbOptionBean.getUuid());
                this.dbBizMap.put(dbOptionBean.getUuid(), new DBBiz(dbBean, dbOptionBean, dbColumnBeans));
            }
        }
    }

    public DBBiz get(String uuid) {
        return this.dbBizMap.get(uuid);
    }

    // 数据库加载
    private List<DBConnBean>  dbConnBeans = new ArrayList<>();
    private List<String> caches = new ArrayList<>();

    private void initMySQL(DBBean dbBean) {
        if (dbBean.getShard() == null) {
            final String name = dbBean.getSchema();
            final String url = System.getProperty("mysql." + name + ".url");
            final String username = System.getProperty("mysql." + name + ".username");
            final String password = System.getProperty("mysql." + name + ".password");
            this.initMySQL(name, url, username, password);
        } else {
            for (int i = 0; i < 1024; i++) {
                final String name = dbBean.getSchema() + i;
                final String url = System.getProperty("mysql." + name + ".url");
                final String username = System.getProperty("mysql." + name + ".username");
                final String password = System.getProperty("mysql." + name + ".password");
                this.initMySQL(name, url, username, password);
            }
        }
    }

    private void initMySQL(String name, String url, String username, String password) {
        DBConnBean dbConnBean = new DBConnBean(name, url, username, password);
        if (this.dbConnBeans.contains(dbConnBean)) {
            LOGGER.debug("{} exist.", dbConnBean.toString());
        } else {
            DBConn.initialize(dbConnBean);
            this.dbConnBeans.add(dbConnBean);
            LOGGER.info("{} added.", dbConnBean.toString());
        }
    }

    private void initRedis(DBBean dbBean) {
        final String name = dbBean.getSchema();
        if (this.caches.contains(name)) {
            LOGGER.debug("{} exist.", dbBean.toString());
            return;
        } else {
            this.caches.add(name);
        }

        if (dbBean.getShard() == null) {
            final String host = System.getProperty("redis." + name + ".host");
            final Integer port = Integer.valueOf(System.getProperty("redis." + name + ".port", "6379"));
            Cache.getInstance().register(Cache.TYPE.REDIS, name, host, port);
        } else {
            List<CacheBean> cacheBeans = new ArrayList<>();
            for (int i = 0; i < 1024; i++) {
                final String schema = name + i;
                final String host = System.getProperty("redis." + schema + ".host");
                final Integer port = Integer.valueOf(System.getProperty("redis." + schema + ".port", "6379"));
                cacheBeans.add(new CacheBean(host, port, i));
            }
            Cache.getInstance().register(Cache.TYPE.REDIS, name, cacheBeans);
        }
        LOGGER.info("{} added.", dbBean.toString());
    }
}
