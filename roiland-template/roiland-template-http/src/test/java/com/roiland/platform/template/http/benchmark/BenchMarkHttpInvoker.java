package com.roiland.platform.template.http.benchmark;

import com.roiland.platform.template.core.Invoker;
import com.roiland.platform.template.core.support.MapperSupport;
import com.roiland.platform.template.http.FileUtils;
import com.roiland.platform.template.http.HttpInvoker;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leon on 4/13/16.
 */
public class BenchMarkHttpInvoker {

    private static Map<String, Object> param1;

    private static String strParam1;

    private static Invoker invoker;

    static {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("host", "https://dev-openapi.roistar.net");
            map.put("content-type", "application/json");
            strParam1 = FileUtils.readResourceFile("param1");
            param1 = MapperSupport.readValue(strParam1, Map.class);
            String template1 = FileUtils.readResourceFile("template1");
            invoker = HttpInvoker.create(template1).globalParam(map).build(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void benchMark1() throws Exception {
        Object object = invoker.invoke(strParam1,null);
    }

    @Benchmark
    public void benchMark2() throws Exception {
        Object object = invoker.invoke(param1,null);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchMarkHttpInvoker.class.getSimpleName())
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(1)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
