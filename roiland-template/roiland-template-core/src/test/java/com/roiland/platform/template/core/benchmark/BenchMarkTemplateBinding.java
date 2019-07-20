package com.roiland.platform.template.core.benchmark;

import com.roiland.platform.template.core.FileUtils;
import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.support.MapperSupport;
import com.roiland.platform.template.core.support.TemplateSupport;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/13
 */
public class BenchMarkTemplateBinding {

    private static ResourceBean resourceBean;

    private static ResourceBean resourceBean3;

    private static Map<String, Object> param1;

    private static String strParam1;

    private static Map<String, Object> param3;

    private static String strParam3;

    private static TemplateSupport templateSupport = new TemplateSupport();

    static {
        try {
            strParam1 = FileUtils.readResourceFile("param2");
            param1 = MapperSupport.readValue(strParam1, Map.class);
            String template1 = FileUtils.readResourceFile("template2");
            resourceBean = templateSupport.parseTemplate(template1);
            templateSupport.preCompile(resourceBean);
            strParam3 = FileUtils.readResourceFile("param3");
            param3 = MapperSupport.readValue(strParam3, Map.class);
            String template3 = FileUtils.readResourceFile("template3");
            resourceBean3 = templateSupport.parseTemplate(template3);
            templateSupport.preCompile(resourceBean3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void benchMark1() throws IOException {
        templateSupport.bind(strParam1, resourceBean);
    }

    @Benchmark
    public void benchMark2() throws IOException {
        templateSupport.bind(param1, resourceBean);
    }

    @Benchmark
    public void benchMark3() throws IOException {
        templateSupport.bind(strParam3, resourceBean3);
    }

    @Benchmark
    public void benchMark4() throws IOException {
        templateSupport.bind(param3, resourceBean3);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchMarkTemplateBinding.class.getSimpleName())
                .warmupIterations(15)
                .measurementIterations(10)
                .threads(1)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
