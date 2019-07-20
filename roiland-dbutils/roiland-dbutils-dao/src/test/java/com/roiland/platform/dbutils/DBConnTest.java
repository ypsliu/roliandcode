package com.roiland.platform.dbutils;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.roiland.platform.dbutils.bean.DBConnBean;
import com.roiland.platform.dbutils.helper.DBTemplate;
import com.roiland.platform.zookeeper.RoilandProperties;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DBConnTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new RoilandProperties("roiland-dbutils-dao");
    }

    @Test
    public void test() {
        System.setProperty("druid.driver_class_name", "com.mysql.jdbc.ReplicationDriver");
        System.setProperty("druid.max_active", "8");

        List<DBConnBean> conns = new ArrayList<DBConnBean>();
        String url = "jdbc:mysql:replication://192.168.35.253:3306,192.168.35.252:3306?readFromMasterWhenNoSlaves=false&autoReconnect=true&roundRobinLoadBalance=true";
        conns.add(new DBConnBean("user-center", url, "roiland", "roiland123!@#"));
        DBConn.initialize(conns);

        String name = "master-slave-read-write";
        int threadSize = 100;
        MetricRegistry metricRegistry = new MetricRegistry();
        final Meter meter = metricRegistry.meter(MetricRegistry.name(name));

        //总请求数
        final AtomicInteger total = new AtomicInteger(0);
        try (ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.SECONDS).build()) {
            reporter.start(1, TimeUnit.SECONDS);
            final CountDownLatch latch = new CountDownLatch(threadSize * 2);
            for (int i = 0; i < threadSize; i++) {
                new Thread() {
                    public void run() {
                        try{
                            for (int i = 0; i < 7000; i++) {
                                new DBTemplate<Boolean>() {
                                    @Override
                                    public Boolean execute(Handle handle) {
                                        List<Map<String, Object>> list = handle.select("select * from t_app_vcode where app_id = '6' and target = '18640957357'");
                                        meter.mark();
                                        return true;
                                    }
                                }.using("user-center", true);
                            }
                        }finally {
                            latch.countDown();
                        }
                    }
                }.start();

                new Thread() {
                    public void run() {
                        try {
                            for (int i = 0; i < 7000; i++) {
                                new DBTemplate<Boolean>() {
                                    @Override
                                    public Boolean execute(Handle handle) {
                                        int index = (int) total.getAndAdd(1);
                                        String mobile = String.valueOf(index);
                                        handle.update("REPLACE INTO `t_app_vcode` (`app_id`, `target`, `vcode`, `vcode_starttime`, `vcode_endtime`, `is_used`, `update_time`) VALUES (?, ?, ?, now(), ?, ?, now());",
                                                "3", mobile, "1234", "2018-01-01 00:00:00", 0);
                                        meter.mark();
                                        return true;
                                    }
                                }.using("user-center", false);
                            }
                        } finally {
                            latch.countDown();
                        }
                    }
                }.start();
            }
            latch.await();
            double percent = (meter.getCount() * 100.0 / (total.get()*2));
            System.out.println(name + " percent:" + percent + "%");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        System.setProperty("druid.driver_class_name", "com.mysql.jdbc.Driver");
        System.setProperty("druid.max_active", "8");

        List<DBConnBean> conns = new ArrayList<DBConnBean>();
        String url = "jdbc:mysql://192.168.35.253:3306?autoReconnect=true";
        conns.add(new DBConnBean("user-center", url, "roiland", "roiland123!@#"));
        DBConn.initialize(conns);

        String name = "single-master-read-write";
        int threadSize = 100;
        MetricRegistry metricRegistry = new MetricRegistry();
        final Meter meter = metricRegistry.meter(MetricRegistry.name(name));

        //总请求数
        final AtomicInteger total = new AtomicInteger(0);
        try (ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.SECONDS).build()) {
            reporter.start(1, TimeUnit.SECONDS);
            final CountDownLatch latch = new CountDownLatch(threadSize * 2);
            for (int i = 0; i < threadSize; i++) {
                new Thread() {
                    public void run() {
                        try{
                            for (int i = 0; i < 7000; i++) {
                                new DBTemplate<Boolean>() {
                                    @Override
                                    public Boolean execute(Handle handle) {
                                        List<Map<String, Object>> list = handle.select("select * from t_app_vcode where app_id = '6' and target = '18640957357'");
                                        meter.mark();
                                        return true;
                                    }
                                }.using("user-center", false);
                            }
                        }finally {
                            latch.countDown();
                        }
                    }
                }.start();

                new Thread() {
                    public void run() {
                        try {
                            for (int i = 0; i < 7000; i++) {
                                new DBTemplate<Boolean>() {
                                    @Override
                                    public Boolean execute(Handle handle) {
                                        int index = (int) total.getAndAdd(1);
                                        String mobile = String.valueOf(index);
                                        handle.update("REPLACE INTO `t_app_vcode` (`app_id`, `target`, `vcode`, `vcode_starttime`, `vcode_endtime`, `is_used`, `update_time`) VALUES (?, ?, ?, now(), ?, ?, now());",
                                                "3", mobile, "1234", "2018-01-01 00:00:00", 0);
                                        meter.mark();
                                        return true;
                                    }
                                }.using("user-center", false);
                            }
                        } finally {
                            latch.countDown();
                        }
                    }
                }.start();
            }
            latch.await();
            double percent = (meter.getCount() * 100.0 / (total.get()*2));
            System.out.println(name + " percent:" + percent + "%");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        System.setProperty("druid.driver_class_name", "com.mysql.jdbc.ReplicationDriver");
        System.setProperty("druid.max_active", "8");

        List<DBConnBean> conns = new ArrayList<DBConnBean>();
        String url = "jdbc:mysql:replication://192.168.35.253:3306,192.168.35.252:3306?readFromMasterWhenNoSlaves=false&autoReconnect=true&roundRobinLoadBalance=true";
        conns.add(new DBConnBean("user-center", url, "roiland", "roiland123!@#"));
        DBConn.initialize(conns);

        String name = "master-slave-only-write";
        int threadSize = 100;
        MetricRegistry metricRegistry = new MetricRegistry();
        final Meter meter = metricRegistry.meter(MetricRegistry.name(name));

        //总请求数
        final AtomicInteger total = new AtomicInteger(0);
        try (ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.SECONDS).build()) {
            reporter.start(1, TimeUnit.SECONDS);
            final CountDownLatch latch = new CountDownLatch(threadSize);
            for (int i = 0; i < threadSize; i++) {

                new Thread() {
                    public void run() {
                        try {
                            for (int i = 0; i < 14000; i++) {
                                new DBTemplate<Boolean>() {
                                    @Override
                                    public Boolean execute(Handle handle) {
                                        int index = (int) total.getAndAdd(1);
                                        String mobile = String.valueOf(index);
                                        handle.update("REPLACE INTO `t_app_vcode` (`app_id`, `target`, `vcode`, `vcode_starttime`, `vcode_endtime`, `is_used`, `update_time`) VALUES (?, ?, ?, now(), ?, ?, now());",
                                                "3", mobile, "1234", "2018-01-01 00:00:00", 0);
                                        meter.mark();
                                        return true;
                                    }
                                }.using("user-center", false);
                            }
                        } finally {
                            latch.countDown();
                        }
                    }
                }.start();
            }
            latch.await();
            double percent = (meter.getCount() * 100.0 / (total.get()));
            System.out.println(name + " percent:" + percent + "%");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        System.setProperty("druid.driver_class_name", "com.mysql.jdbc.Driver");
        System.setProperty("druid.max_active", "8");

        List<DBConnBean> conns = new ArrayList<DBConnBean>();
        String url = "jdbc:mysql://192.168.35.253:3306?autoReconnect=true";
        conns.add(new DBConnBean("user-center", url, "roiland", "roiland123!@#"));
        DBConn.initialize(conns);

        String name = "single-master-only-write";
        int threadSize = 100;
        MetricRegistry metricRegistry = new MetricRegistry();
        final Meter meter = metricRegistry.meter(MetricRegistry.name(name));

        //总请求数
        final AtomicInteger total = new AtomicInteger(0);
        try (ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.SECONDS).build()) {
            reporter.start(1, TimeUnit.SECONDS);
            final CountDownLatch latch = new CountDownLatch(threadSize);
            for (int i = 0; i < threadSize; i++) {
                new Thread() {
                    public void run() {
                        try {
                            for (int i = 0; i < 14000; i++) {
                                new DBTemplate<Boolean>() {
                                    @Override
                                    public Boolean execute(Handle handle) {
                                        int index = (int) total.getAndAdd(1);
                                        String mobile = String.valueOf(index);
                                        handle.update("REPLACE INTO `t_app_vcode` (`app_id`, `target`, `vcode`, `vcode_starttime`, `vcode_endtime`, `is_used`, `update_time`) VALUES (?, ?, ?, now(), ?, ?, now());",
                                                "3", mobile, "1234", "2018-01-01 00:00:00", 0);
                                        meter.mark();
                                        return true;
                                    }
                                }.using("user-center", false);
                            }
                        } finally {
                            latch.countDown();
                        }
                    }
                }.start();
            }
            latch.await();
            double percent = (meter.getCount() * 100.0 / (total.get()));
            System.out.println(name + " percent:" + percent + "%");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSql() {
//        MonitorDataDao monitorDataDao = new MonitorDataDao();
//        final Map<String, Object> params = new HashMap<>();
//        params.put("id", 1);
//        params.put("application", "mysql-if");
//        Object[] objects = monitorDataDao.convertMapToValuesStr(params);
//        for (Object obj : objects) System.out.println(obj);
        System.out.println("14759193263".hashCode() % 1024);
    }
}
