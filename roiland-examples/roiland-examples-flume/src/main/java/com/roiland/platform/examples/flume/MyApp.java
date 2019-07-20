package com.roiland.platform.examples.flume;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyApp {

    private static final Log LOGGER = LogFactory.getLog(MyApp.class);

//    private static final MetricRegistry metrics = new MetricRegistry();
//    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
//            .convertRatesTo(TimeUnit.SECONDS)
//            .convertDurationsTo(TimeUnit.MILLISECONDS)
//            .build();
//    private static final Meter requests = metrics.meter("requests");

    static {
//        reporter.start(1, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        MyRpcClientFacade2 client = new MyRpcClientFacade2();
        client.init();
        String sampleData = "2016-06-28 16:02:18.884 [nioEventLoopGroup-3-1] INFO c.r.p.s.c.h.RoilandProtocolHandler: - [id: 0x9d4e6d91, /192.168.35.84:53039 => /192.168.35.88:7000][fe0e397a-6074-45d3-9f95-530b1f1fdc89]RG,151,AW9E011000R250013,83eebee7811847a7ad67f2f00d0a325b,0,CQABIAUAPAEFDwYZDic1DwYZDicnAQcBDP7+/v4BAQMDAwMBAv////8BAwAAAP4BBAAAAP4BCwAAAAEBEAAAAP9hug==,32160";

        while (true) {
//            List<String> datas = new ArrayList<>();
//            for (int i = 0; i < 500; i++) {
//                datas.add(sampleData);
//            }
//            client.sendDatasToFlume(datas);
//            requests.mark(500);
            LOGGER.info("hello world");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyRpcClientFacade2 {

    private RpcClient client;

    public void init() {
        Properties props = new Properties();
        props.put("client.type", "default_loadbalance");
        props.put("host-selector", "random"); // For random host selection
        props.put("backoff", "true"); // Disabled by default.

        // List of hosts (space-separated list of user-chosen host aliases)
        props.put("hosts", "h1 h2 h3");

        // host/port pair for each host alias
        String host1 = "192.168.35.71:41414";
        String host2 = "192.168.35.72:41414";
        String host3 = "192.168.35.73:41414";
        props.put("hosts.h1", host1);
        props.put("hosts.h2", host2);
        props.put("hosts.h3", host3);

        // create the client with failover properties
        client = RpcClientFactory.getInstance(props);
    }

    public void sendDataToFlume(String data) {
        Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));

        try {
            client.append(event);
        } catch (EventDeliveryException e) {
            client.close();
            client = null;
        }
    }
    public void sendDatasToFlume(List<String> datas) {
        List<Event> events = new ArrayList<Event>(datas.size());
        for (String data: datas) {
            events.add(EventBuilder.withBody(data, Charset.forName("UTF-8")));
        }

        try {
            client.appendBatch(events);
        } catch (EventDeliveryException e) {
            client.close();
            client = null;
        }
    }

    public void cleanUp() {
        client.close();
    }
}
