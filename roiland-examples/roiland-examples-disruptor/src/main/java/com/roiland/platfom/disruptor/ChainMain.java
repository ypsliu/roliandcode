package com.roiland.platfom.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ChainMain {

    private static final int MULTI_THREADS = 4;
    private static final int RING_BUFFER = 16;

    public static void main(String[] args) throws InterruptedException, TimeoutException {
        final ExecutorService executor = Executors.newFixedThreadPool(MULTI_THREADS);

        WorkHandler<ChainEvent>[] workHandlers = new ChainEventHandler[MULTI_THREADS];
        for (int i = 0; i < MULTI_THREADS; i++) {
            workHandlers[i] = new ChainEventHandler<>();
        }

        Disruptor<ChainEvent> disruptor = new Disruptor<>(new ChainEventFactory(),
                RING_BUFFER,
                executor,
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        disruptor.handleEventsWithWorkerPool(workHandlers).then(new ChainResponseEventHandler<>());
        disruptor.start();
        RingBuffer<ChainEvent> ringBuffer = disruptor.getRingBuffer();

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fault = new AtomicInteger();
        for (int i = 1; i <= RING_BUFFER * 64; i++) {
            if (ringBuffer.tryPublishEvent(new ChainEventTranslate<>(i))) {
                success.getAndIncrement();
            } else {
                fault.getAndIncrement();
            }
        }
        Thread.sleep(2000);
        System.out.println("成功：" + success.get() + "  失败：" + fault.get());

        long start = System.currentTimeMillis();
        disruptor.shutdown(1000, TimeUnit.MICROSECONDS);
        executor.shutdown();
        System.out.println(System.currentTimeMillis() - start);
    }
}
