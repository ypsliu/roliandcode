package com.roiland.platform.rest.filter.auth;

import com.roiland.platform.rest.model.dao.AuthStatisticDao;
import com.roiland.platform.rest.task.AbstractTimerTask;
import com.roiland.platform.rest.util.Constants;
import org.mapdb.*;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 2015/12/9.
 */
public class StatisticSingleton {

    private static final AuthStatisticDao DAO = AuthStatisticDao.instance();

    private static final StatisticSingleton INSTANCE = new StatisticSingleton();

    private static final DB MAPDB = DBMaker.memoryDB().make();

    private static BTreeKeySerializer keySerializer = new BTreeKeySerializer.ArrayKeySerializer(
            new Comparator[]{Fun.COMPARATOR, Fun.COMPARATOR},
            new Serializer[]{Serializer.STRING, Serializer.STRING}
    );

    private static final BTreeMap<Object[], AtomicInteger> DAILY_REQUEST_STATISTICS = MAPDB.treeMapCreate(Constants.DAILY_REQUEST_STATISTICS).keySerializer(keySerializer).makeOrGet();

    private static final BTreeMap<Object[], AtomicBoolean> DAILY_REQUEST_PREVENT = MAPDB.treeMapCreate(Constants.DAILY_REQUEST_PREVENT).keySerializer(keySerializer).makeOrGet();

    private StatisticSingleton() {
        syncStatisticsInfo(new Date(System.currentTimeMillis()));
        new Timer().schedule(new StatisticTask("STATISTIC_SYNC_TASK"), Constants.STATISTIC_SYNC_TASK_DELAY, Constants.STATISTIC_SYNC_TASK_PERIOD);
    }

    public static StatisticSingleton instance() {
        return INSTANCE;
    }

    public void addRequestCount(String strAppID, String strPath) {
        Object[] key = new Object[]{strAppID, strPath};
        if (DAILY_REQUEST_STATISTICS.containsKey(key)) {
            DAILY_REQUEST_STATISTICS.get(key).addAndGet(1);
        }
    }

    public boolean isPrevent(String strAppID, String strPath){
        Object[] key = new Object[]{strAppID, strPath};
        return DAILY_REQUEST_PREVENT.containsKey(key) && DAILY_REQUEST_PREVENT.get(key).get();
    }

    public void syncStatisticsInfo(Date date) {
        List<Map<String, Object>> dailyRequestLimitationList = DAO.getDailyRequestLimitationList(date);
        syncLimitationInfo(dailyRequestLimitationList);
        syncPreventInfo(dailyRequestLimitationList);
        DAO.insertStatisticsInfo(DAILY_REQUEST_STATISTICS, date);
        dailyRequestLimitationList = DAO.getDailyRequestLimitationList(date);
        initPreventInfo(dailyRequestLimitationList);
    }

    private void syncLimitationInfo(List<Map<String, Object>> dailyRequestLimitationList) {
        Set<Object[]> dailyRequestLimitationSet = new HashSet<>();
        for (Map<String, Object> record : dailyRequestLimitationList) {
            Object[] key = new Object[]{record.get("app_id").toString(), record.get("path").toString()};
            dailyRequestLimitationSet.add(key);
        }
        for (Object[] key : dailyRequestLimitationSet) {
            DAILY_REQUEST_STATISTICS.putIfAbsent(key, new AtomicInteger(0));
        }
        for (Map.Entry<Object[], AtomicInteger> entry : DAILY_REQUEST_STATISTICS.entrySet()) {
            if (!dailyRequestLimitationSet.contains(entry.getKey())) {
                DAILY_REQUEST_STATISTICS.remove(entry.getKey());
            }
        }
    }

    private void syncPreventInfo(List<Map<String, Object>> dailyRequestLimitationList) {
        Set<Object[]> dailyRequestLimitationSet = new HashSet<>();
        for (Map<String, Object> record : dailyRequestLimitationList) {
            Object[] key = new Object[]{record.get("app_id").toString(), record.get("path").toString()};
            dailyRequestLimitationSet.add(key);
        }
        for (Object[] key : dailyRequestLimitationSet) {
            DAILY_REQUEST_PREVENT.putIfAbsent(key, new AtomicBoolean(false));
        }
        for (Map.Entry<Object[], AtomicBoolean> entry : DAILY_REQUEST_PREVENT.entrySet()) {
            if (!dailyRequestLimitationSet.contains(entry.getKey())) {
                DAILY_REQUEST_PREVENT.remove(entry.getKey());
            }
        }
    }

    private void initPreventInfo(List<Map<String, Object>> dailyRequestLimitationList) {
        for (Map<String, Object> record : dailyRequestLimitationList) {
            Object[] key = new Object[]{record.get("app_id").toString(), record.get("path").toString()};
            Object dailyRequestCountStr = record.get("daily_request_count");
            Integer maxDailyRequestCount = Integer.valueOf(record.get("max_daily_request_count").toString());
            Integer dailyRequestCount = dailyRequestCountStr == null ? 0 : Integer.valueOf(dailyRequestCountStr.toString());
            if (dailyRequestCount >= maxDailyRequestCount) {
                DAILY_REQUEST_PREVENT.get(key).set(true);
            } else {
                DAILY_REQUEST_PREVENT.get(key).set(false);
            }
        }
    }

    private class StatisticTask extends AbstractTimerTask {

        public StatisticTask(String taskName) {
            super(taskName);
        }

        @Override
        protected void execute() throws Exception {
            syncStatisticsInfo(new Date(System.currentTimeMillis()));
        }
    }

}
