package com.roiland.platform.rest.model.dao;

import com.roiland.platform.dbutils.DBConn;
import com.roiland.platform.rest.util.Constants;
import org.mapdb.BTreeMap;
import org.mapdb.HTreeMap;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.PreparedBatch;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 2015/12/9.
 */
public class AuthStatisticDao {

    private static final AuthStatisticDao INSTANCE = new AuthStatisticDao();

    private static final String STATISTICS_INSERT_SQL = " insert into t_service_daily_request_statistics(request_date,app_id,path,daily_request_count,1min_request_count,max_1min_request_count,create_time,update_time) " +
            " values (?,?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP) on duplicate key update daily_request_count = daily_request_count + VALUES(daily_request_count), " +
            " 1min_request_count = VALUES(1min_request_count),update_time = CURRENT_TIMESTAMP, " +
            " max_1min_request_count = case when VALUES(1min_request_count)>max_1min_request_count then VALUES(1min_request_count) else max_1min_request_count end ";

    private static final String STATISTICS_SELECT_SQL = " select t1.app_id,t1.path,t1.max_daily_request_count,t2.daily_request_count from t_service_daily_request_limitation t1 left join " +
            " t_service_daily_request_statistics t2 on t1.app_id = t2.app_id and t1.path = t2.path and t2.request_date = :request_date where t1.delete_flag = 0";


    private AuthStatisticDao() {

    }

    public static AuthStatisticDao instance() {
        return INSTANCE;
    }

    public void insertStatisticsInfo(BTreeMap<Object[], AtomicInteger> dailyRequestStatistics, Date date) {
        Handle handle = null;
        try {
            handle = DBConn.handle(Constants.RESTFUL_DB_SCHEMA);
            PreparedBatch batch = handle.prepareBatch(STATISTICS_INSERT_SQL);
            for (Map.Entry<Object[], AtomicInteger> entry : dailyRequestStatistics.entrySet()) {
                if (entry.getValue().get() > 0) {
                    int _1minCount = entry.getValue().getAndSet(0);
                    Object[] array = entry.getKey();
                    batch.add(date, array[0], array[1], _1minCount, _1minCount, _1minCount);
                }
            }
            batch.execute();
        } finally {
            if (handle != null) {
                handle.close();
            }
        }
    }

    public List<Map<String, Object>> getDailyRequestLimitationList(Date date) {
        Handle handle = null;
        try {
            handle = DBConn.handle(Constants.RESTFUL_DB_SCHEMA);
            List<Map<String, Object>> dailyRequestLimitationList = handle.createQuery(STATISTICS_SELECT_SQL).bind("request_date", date).list();
            return dailyRequestLimitationList;
        } finally {
            if (handle != null) {
                handle.close();
            }
        }
    }
}
