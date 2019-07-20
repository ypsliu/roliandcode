package com.roiland.platform.examples.temp;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffy.yang on 16-7-6.
 */
public class Kafka {

    public static void main(String[] args) {
        final String TOPIC = "roiland-examples";

//        ExecutorService executor = Executors.newCachedThreadPool();
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                Properties props = new Properties();
//                props.put("bootstrap.servers","192.168.35.71:9092,192.168.35.72:9092");
//                props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//                props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
////                props.put("acks", "0");
////                props.put("batch.size", 1000);
//
//                KafkaProducer<String, String> producer = new KafkaProducer<>(props);
//
//                while(true) {
//                    for(int i = 0; i < 20000; i++) {
//                        String msg = "key.serializer:org.apache.kafka.common.serialization.StringSerializer" + i;
//                        try {
//                            producer.send(new ProducerRecord<String, String>(TOPIC, msg)).get(30, TimeUnit.MILLISECONDS);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        } catch (TimeoutException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        final Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("group.id", "test");
        kafkaProperties.setProperty("enable.auto.commit", "true");
        kafkaProperties.setProperty("auto.commit.interval.ms", "1000");
        kafkaProperties.setProperty("session.timeout.ms", "30000");
        kafkaProperties.setProperty("bootstrap.servers","192.168.35.71:9092,192.168.35.72:9092");
//        kafkaProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        kafkaProperties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        kafkaProperties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        long last =  System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger();
        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(kafkaProperties);
        consumer.subscribe(Arrays.asList(TOPIC));
        while (true) {
            ConsumerRecords<byte[], byte[]> records = consumer.poll(100);
            for (ConsumerRecord<byte[], byte[]> record : records) {
                if (count.incrementAndGet() % 10000 == 0) {
                    long current = System.currentTimeMillis();
                    System.out.println((current - last) + ":" + count.intValue());
                    last = current;
                }
            }
        }
    }
}
