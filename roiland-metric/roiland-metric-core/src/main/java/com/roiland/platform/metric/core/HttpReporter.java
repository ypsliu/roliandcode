package com.roiland.platform.metric.core;

import com.codahale.metrics.*;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.roiland.open.sdk.RoilandSdk;

import java.io.IOException;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author leon.chen
 * @since 2016/7/22
 */
public class HttpReporter extends ScheduledReporter {

    private RoilandSdk sdk = new RoilandSdk();

    private static final ObjectWriter writer = new ObjectMapper().writer();

    public static Builder forRegistry(MetricRegistry registry) {
        return new Builder(registry);
    }

    public static class Builder {
        private final MetricRegistry registry;
        private String id;
        private String baseUrl;
        private Locale locale;
        private Clock clock;
        private Object metaInfo;
        private TimeZone timeZone;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;

        private Builder(MetricRegistry registry) {
            this.registry = registry;
            this.locale = Locale.CHINA;
            this.clock = Clock.defaultClock();
            this.timeZone = TimeZone.getDefault();
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
            this.baseUrl = System.getProperty("metric.url","http://127.0.0.1:8080");
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder metaInfo(Object metaInfo) {
            this.metaInfo = metaInfo;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder formattedFor(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public Builder formattedFor(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        public HttpReporter build() {
            if(id == null){
                throw new IllegalArgumentException("id required");
            }
            return new HttpReporter(registry,
                    id,
                    baseUrl,
                    metaInfo,
                    locale,
                    clock,
                    timeZone,
                    rateUnit,
                    durationUnit,
                    filter);
        }
    }

    private final Locale locale;
    private final Clock clock;
    private final DateFormat dateFormat;
    private final Object metaInfo;
    private final String id;
    private final String baseUrl;

    private final String gaugesUrl;
    private final String counterUrl;
    private final String histogramsUrl;
    private final String meterUrl;
    private final String timerUrl;
    private final String ip;

    private HttpReporter(MetricRegistry registry,
                         String id,
                         String baseUrl,
                         Object metaInfo,
                         Locale locale,
                         Clock clock,
                         TimeZone timeZone,
                         TimeUnit rateUnit,
                         TimeUnit durationUnit,
                         MetricFilter filter) {
        super(registry, "http-reporter", filter, rateUnit, durationUnit);
        this.id = id;
        this.baseUrl = baseUrl;
        this.metaInfo = metaInfo;
        this.locale = locale;
        this.clock = clock;
        this.dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.MEDIUM,
                locale);
        dateFormat.setTimeZone(timeZone);
        this.gaugesUrl = url(baseUrl, GAUGES);
        this.counterUrl = url(baseUrl, COUNTERS);
        this.histogramsUrl = url(baseUrl, HISTOGRAMS);
        this.meterUrl = url(baseUrl, METERS);
        this.timerUrl = url(baseUrl, TIMERS);
        this.ip = System.getProperty("local.address","127.0.0.1");
    }

    private static String url(String baseUrl, String resource) {
        if (baseUrl.endsWith("/") && resource.startsWith("/")) {
            return baseUrl + resource.substring(1);
        } else if (baseUrl.endsWith("/") && !resource.startsWith("/")) {
            return baseUrl + resource;
        } else if (!baseUrl.endsWith("/") && resource.startsWith("/")) {
            return baseUrl + resource;
        } else {
            return baseUrl + "/" + resource;
        }
    }

    private static final String GAUGES = "metric/gauges";
    private static final String COUNTERS = "metric/counters";
    private static final String HISTOGRAMS = "metric/histograms";
    private static final String METERS = "metric/meters";
    private static final String TIMERS = "metric/timers";

    @Override
    public void report(SortedMap<String, Gauge> gauges,
                       SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms,
                       SortedMap<String, Meter> meters,
                       SortedMap<String, Timer> timers) {
        final String dateTime = dateFormat.format(new Date(clock.getTime()));

        if (!gauges.isEmpty()) {
            outGauge(gauges, dateTime);
        }

        if (!counters.isEmpty()) {
            outCounter(counters, dateTime);
        }

        if (!histograms.isEmpty()) {
            outHistogram(histograms, dateTime);
        }

        if (!meters.isEmpty()) {
            outMeter(meters, dateTime);
        }

        if (!timers.isEmpty()) {
            outTimer(timers, dateTime);
        }
    }

    private void outTimer(SortedMap<String, Timer> timers, final String dateTime) {
        for (Map.Entry<String, Timer> entry : timers.entrySet()) {
            Timer timer = entry.getValue();
            final Snapshot snapshot = timer.getSnapshot();
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id", id);
            map.put("collectTime", dateTime);
            if(metaInfo!=null){
                map.put("meta", metaInfo);
            }
            map.put("ip",ip);
            map.put("rateUnit", getRateUnit());
            map.put("durationUnit",getDurationUnit());
            map.put("count", timer.getCount());
            map.put("meanRate", convertRate(timer.getMeanRate()));
            map.put("1minRate",convertRate(timer.getOneMinuteRate()));
            map.put("5minRate",convertRate(timer.getFiveMinuteRate()));
            map.put("15minRate",convertRate(timer.getFifteenMinuteRate()));
            map.put("min",convertDuration(snapshot.getMin()));
            map.put("max",convertDuration(snapshot.getMax()));
            map.put("mean",convertDuration(snapshot.getMean()));
            map.put("stddev",convertDuration(snapshot.getStdDev()));
            map.put("median",convertDuration(snapshot.getMedian()));
            map.put("75thPercentile",convertDuration(snapshot.get75thPercentile()));
            map.put("95thPercentile",convertDuration(snapshot.get95thPercentile()));
            map.put("98thPercentile",convertDuration(snapshot.get98thPercentile()));
            map.put("99thPercentile",convertDuration(snapshot.get99thPercentile()));
            map.put("999thPercentile",convertDuration(snapshot.get999thPercentile()));
            out(map,timerUrl);
        }
    }

    private void outMeter(SortedMap<String, Meter> meters, final String dateTime) {
        for(Map.Entry<String,Meter> entry : meters.entrySet()){
            Meter meter = entry.getValue();
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id", id);
            map.put("collectTime", dateTime);
            if(metaInfo!=null){
                map.put("meta", metaInfo);
            }
            map.put("ip",ip);
            map.put("count",meter.getCount());
            map.put("rateUnit",getRateUnit());
            map.put("mean",convertRate(meter.getMeanRate()));
            map.put("1minRate",convertRate(meter.getOneMinuteRate()));
            map.put("5minRate",convertRate(meter.getFiveMinuteRate()));
            map.put("15minRate",convertRate(meter.getFifteenMinuteRate()));
            out(map,meterUrl);
        }
    }

    private void outHistogram(SortedMap<String, Histogram> histograms, final String dateTime) {
        for(Map.Entry<String,Histogram> entry : histograms.entrySet()){
            Histogram histogram = entry.getValue();
            Snapshot snapshot = histogram.getSnapshot();
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id", id);
            map.put("collectTime", dateTime);
            if(metaInfo!=null){
                map.put("meta", metaInfo);
            }
            map.put("ip",ip);
            map.put("count",histogram.getCount());
            map.put("min",snapshot.getMin());
            map.put("max",snapshot.getMax());
            map.put("mean",snapshot.getMean());
            map.put("stdDev",snapshot.getStdDev());
            map.put("median",snapshot.getMedian());
            map.put("75thPercentile",snapshot.get75thPercentile());
            map.put("95thPercentile",snapshot.get95thPercentile());
            map.put("98thPercentile",snapshot.get98thPercentile());
            map.put("99thPercentile",snapshot.get99thPercentile());
            map.put("999thPercentile",snapshot.get999thPercentile());
            out(map,histogramsUrl);
        }
    }

    private void outCounter(SortedMap<String, Counter> counters, final String dateTime) {
        for(Map.Entry<String,Counter> entry : counters.entrySet()){
            Counter counter = entry.getValue();
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id", id);
            map.put("collectTime", dateTime);
            if(metaInfo!=null){
                map.put("meta", metaInfo);
            }
            map.put("ip",ip);
            map.put("count",counter.getCount());
            out(map,counterUrl);
        }
    }

    private void outGauge(SortedMap<String, Gauge> gauges, final String dateTime) {
        for(Map.Entry<String,Gauge> entry : gauges.entrySet()){
            Gauge gauge = entry.getValue();
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id", id);
            map.put("collectTime", dateTime);
            if(metaInfo!=null){
                map.put("meta", metaInfo);
            }
            map.put("ip",ip);
            map.put("value",gauge.getValue());
            out(map,counterUrl);
        }
    }

    private void out(Map<String,Object> map, String timerUrl) {
        try {
            sdk.post(timerUrl,writer.writeValueAsString(map),null);
        } catch (IOException e) {
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            sdk.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
