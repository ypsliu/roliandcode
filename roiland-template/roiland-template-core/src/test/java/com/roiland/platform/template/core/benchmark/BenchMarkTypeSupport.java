package com.roiland.platform.template.core.benchmark;

import com.roiland.platform.template.core.support.TypeSupport;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author leon.chen
 * @since 2016/4/13
 */
public class BenchMarkTypeSupport {

    @Benchmark
     public void benchMark1(){
        String type = "HashMap<HashMap<String,Map<String,List<String>>>,HashMap<String,Map<String,List<String>>>>";
        TypeSupport.getJavaType(type);
    }

    @Benchmark
    public void benchMark2(){
        String type = "HashMap<HashMap<com.roiland.platform.template.core.support.Bean1,Map<com.roiland.platform.template.core.support.Bean1,List<com.roiland.platform.template.core.support.Bean1>>>,HashMap<com.roiland.platform.template.core.support.Bean1,Map<com.roiland.platform.template.core.support.Bean1,List<com.roiland.platform.template.core.support.Bean1>>>>";
        TypeSupport.getJavaType(type);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchMarkTypeSupport.class.getSimpleName())
                .warmupIterations(30)
                .measurementIterations(20)
                .threads(1)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
