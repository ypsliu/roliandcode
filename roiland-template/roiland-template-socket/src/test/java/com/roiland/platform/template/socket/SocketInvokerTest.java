package com.roiland.platform.template.socket;

import com.roiland.platform.template.core.Invoker;
import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.support.TemplateSupport;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class SocketInvokerTest extends TestCase {

    public void testInvoke() throws Exception {
        final Map<String, Object> map = new HashMap<>();
        map.put("host", "192.168.34.115");
        map.put("port", 7000);
        final String param1 = FileUtils.readResourceFile("param1");
        final String template1 = FileUtils.readResourceFile("template1");
        int threads = 10;
        final CountDownLatch latch = new CountDownLatch(threads);
        final TemplateSupport templateSupport = new TemplateSupport();
        final ResourceBean rb = templateSupport.parseTemplate(template1);
        templateSupport.preCompile(rb);
        final CyclicBarrier barrier = new CyclicBarrier(threads);
        final Invoker invoker = SocketInvoker.create(rb).globalParam(map).context(templateSupport).build(false);
        for (int i = 0; i < threads; i++) {
            new Thread() {
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            barrier.await();
                            invoker.invoke(param1, new Invoker.InvokerCallback() {
                                @Override
                                public void callback(Object obj) {
                                    System.out.println("ack:" + obj);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }
            }.start();
        }
        latch.await();
        System.out.println("done");

        while (true) {
            Thread.yield();
        }
    }

}