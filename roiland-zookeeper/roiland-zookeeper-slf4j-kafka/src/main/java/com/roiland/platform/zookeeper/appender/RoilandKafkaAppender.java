package com.roiland.platform.zookeeper.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author leon.chen
 * @since 2016/7/18
 */
public class RoilandKafkaAppender extends AppenderBase<ILoggingEvent> {

    //required
    private String topic;

    //required
    private String bootstrapServers;

    //required
    private Layout<ILoggingEvent> layout;

    private Producer<byte[], byte[]> producer;

    private String batchSize = "32768";

    private String acks = "0";

    private int timeoutMillis = 30000;

    private boolean sync = false;

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    @Override
    public void start() {
        super.start();
        Properties config = new Properties();
        config.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        config.setProperty("batch.size", getBatchSize());
        //when logback get property is null then return key_IS_UNDEFINED.so we have to check this condition.
        if (getBootstrapServers() != null && !getBootstrapServers().endsWith("_IS_UNDEFINED")) {
            config.setProperty("bootstrap.servers", getBootstrapServers());
        }
        config.setProperty("acks", getAcks());
        //下面两个属性实现at least once delivery以及asynchronous replication
        //稍微影响吞吐量，但保证消息至少一次可达
        //当acks=0的时候，retries设置失效，实现的是at most once delivery，不保证消息可达，会导致丢失消息以及asynchronous replication
        //当acks=all并且retries=1的时候，实现的是at least once delivery以及synchronous replication，极度影响性能
        //当acks=1并且retries=1的时候，实现的是at least once delivery以及asynchronous replication，但由于异步repliation的时候不能保证消息成功，所以也有可能丢失消息
        //但是当replica=1的时候acks=1并且retries=1这种情况严格等价于at least once delivery
        config.setProperty("timeout.ms", this.timeoutMillis + "");
        try{
            this.producer = new KafkaProducer<>(config);
        }catch (KafkaException e){
            // NOP
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (producer != null) {
            // This thread is a workaround for this Kafka issue: https://issues.apache.org/jira/browse/KAFKA-1660
            final Thread closeThread = new Thread(new Runnable() {
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

    /**
     * @param event
     */
    @Override
    protected void append(ILoggingEvent event) {
        String msg = getLayout().doLayout(event);
        try {
            if (producer != null) {
                Future<RecordMetadata> future = producer.send(new ProducerRecord<byte[], byte[]>(getTopic(), msg.getBytes()));
                if (sync) {
                    future.get(timeoutMillis, TimeUnit.MILLISECONDS);
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
