package com.roiland.platform.log4j.kafka.appender;


import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.mom.kafka.DefaultKafkaProducerFactory;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaProducerFactory;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.util.Log4jThread;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RoilandKafkaManager extends AbstractManager {

    public static final String DEFAULT_TIMEOUT_MILLIS = "30000";

    /**
     * package-private access for testing.
     */
    static KafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory();

    private final Properties config = new Properties();
    private Producer<byte[], byte[]> producer = null;
    private final int timeoutMillis;

    private final String topic;

    public RoilandKafkaManager(final String name, final String topic, final Property[] properties) {
        super(name);
        this.topic = topic;
        config.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        config.setProperty("batch.size", "32768");
        config.setProperty("linger.ms", "5");
        //下面两个属性实现at least once delivery以及asynchronous replication
        //稍微影响吞吐量，但保证消息至少一次可达
        //当acks=0的时候，retries设置失效，实现的是at most once delivery，不保证消息可达，会导致丢失消息以及asynchronous replication
        //当acks=all并且retries=1的时候，实现的是at least once delivery以及synchronous replication，极度影响性能
        //当acks=1并且retries=1的时候，实现的是at least once delivery以及asynchronous replication，但由于异步repliation的时候不能保证消息成功，所以也有可能丢失消息
        //但是当replica=1的时候acks=1并且retries=1这种情况严格等价于at least once delivery
        config.setProperty("acks", "0");
        for (final Property property : properties) {
            config.setProperty(property.getName(), property.getValue());
        }
        this.timeoutMillis = Integer.parseInt(config.getProperty("timeout.ms", DEFAULT_TIMEOUT_MILLIS));
    }

    @Override
    public void releaseSub() {
        if (producer != null) {
            // This thread is a workaround for this Kafka issue: https://issues.apache.org/jira/browse/KAFKA-1660
            final Thread closeThread = new Log4jThread(new Runnable() {
                @Override
                public void run() {
                    producer.close();
                }
            });
            closeThread.setName("KafkaManager-CloseThread");
            closeThread.setDaemon(true); // avoid blocking JVM shutdown
            closeThread.start();
            try {
                closeThread.join(timeoutMillis);
            } catch (final InterruptedException ignore) {
                // ignore
            }
        }
    }

    public void send(final byte[] msg, final boolean sync, final AppenderControl failoverAppender, final LogEvent event) throws ExecutionException, InterruptedException, TimeoutException {
        if (producer != null) {
            //当所有broker全部挂掉的时候，此时send的方法会block住60秒(max.block.ms参数控制)，但并不抛出异常，因此failover策略失效。
            //block在KafkaProducer.doSend()中的long waitedOnMetadataMs = waitOnMetadata(record.topic(), this.maxBlockTimeMs);
            //如果项目启动并且取得waitOnMetaData之后全部broker再挂掉的话,不会block应用
            Future<RecordMetadata> future = producer.send(new ProducerRecord<byte[], byte[]>(topic, msg), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null && failoverAppender != null) {
                        // log4j2为了在线程间可以最少的创建log对象，最少的引发gc,采用的是MutableLogEvent。
                        // 这个对象在此回调中会有问题，会把之前的logEvent在线程间覆盖掉。导致回调时event的Message与Log时的不一致。
                        // work around的方法在LogEventWrapper的构造函数中
                        failoverAppender.callAppender(event);
                    }
                }
            });
            if (sync) {
                future.get(timeoutMillis, TimeUnit.MILLISECONDS);
            }
        } else {
            if (failoverAppender != null) {
                failoverAppender.callAppender(event);
            }
        }
    }

    public void startup() {
        try {
            producer = producerFactory.newKafkaProducer(config);
        } catch (Throwable e) {
            //NOP
        }
    }

    public String getTopic() {
        return topic;
    }

}
