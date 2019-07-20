# Roiland-template

## core用法
```java
    ResourceBean template = TemplateSupport.parseTemplate(template);
    BindingSupport.builder().preCompile(template);
    ResourceBean resource = BindingSupport.builder().bind(param,template);
```

## http用法
```java
    try(Invoker invoker = HttpInvoker.create(template).globalParam(globalParam).build(true)){
        Object object = invoker.invoke(param);
    }
```

## socket用法
```java
    try(Invoker invoker = SocketInvoker.create(template).globalParam(globalParam).build(true)){
        invoker.invoke(param);
    }
```

## 模板绑定Benchmark
```
    Benchmark                             Mode  Cnt       Score       Error  Units
    BenchMarkTemplateBinding.benchMark6  thrpt   10  204644.200 ± 23098.722  ops/s //模板经过预编译
    BenchMarkTemplateBinding.benchMark8  thrpt   10   17935.064 ±  5509.751  ops/s //未预编译，解释执行

```