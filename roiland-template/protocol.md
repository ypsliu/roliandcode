## 模板协议

*协议共同部:* `resource`,`method`,`variables`
*协议可选部:* `struct`
*先不考虑format*

`resource`: 表示资源名(http表示`URI`，dubbo表示`class全限定名`)
`method`: 表示方法名
`variables`:参数或属性部分

### 压测参数模板
```
    {
        "thread" : 200,
        "count" : 2000,
        "machine" 4
    }
```

### http（http协议共同部分，http协议header部分，roiland鉴权协议秘钥部分,http资源部分）
```
    {
        "host" : "https://dev-openapi.roistar.net",
        "content-type" : "application/json"
    }
    {
        "resource" : "/usr/info",
        "method" : "GET",
        "return" : "Map<String,Object>",
        "variables" : [
            {"name" : "#headers", "type" : "Map<String,String>", "value" : {"access_token" : "${access_token}", "version" : "0.0.1"}},
            {"name" : "#secretKey", "type" : "String", "value" : "${secretKey}"},
            {"name" : "timestamp", "type" : "Timestamp", "value" : "${timestamp}"},
            {"name" : "field", "type" : "Array<String>", "value" : ["fields1", "fields2", "fields3"]},
            {"name" : "field1", "type" : "Map<String,Object>", "value" : {"var1" : "${fields1}", "var2" : "fields2"}},
            {"name" : "field2", "type" : "Boolean", "value" : true},
            {"name" : "field3", "type" : "Integer", "value" : "${field3}"}
        ]
    }
```

### dubbo（dubbo协议共同部分，dubbo资源部分）
```
    暂未实现
    {
        "registry" : "zookeeper://192.168.35.232:2181",
        "version" : "1.0.0",
        "application" : "roiland-open-rest",
        "group" : "dev",
        "jar" : "http://maven.roistar.net/service/local/repositories/snapshot/content/com/roiland/platform/roiland-open-api/1.0.3-SNAPSHOT/roiland-open-api-1.0.3-20160327.093144-33.jar",
    }
    {
        "resource" : "com.roiland.platform.open.api.IVehService",
        "method" : "getVehInfo",
        "return" : "Map<String,Object>"
        "variables" : [
            {"name" : "bid", "type" : "String", "value" : "${bid}"},
            {"name" : "cnum", "type" : "String", "value" : "LVAM0009000919"},
            {"name" : "timestamp", "type" : "String", "value" : "${#string.leftPad(index,10,'0')}"},
            {"name" : "fields", "type" : "Array<String>", "value" : ["fields1","fields2","fields3"]},
            {"name" : "fields1", "type" : "Map<String,xx.xxx.xxBean>", "value" : {"key" : {"var1" : "${var1}", "var2" : "${var2}"}}},
            {"name" : "fields2", "type" : "List<Integer>", "value" : "${fields2}"},
            {"name" : "fields3", "type" : "List<xx.xxx.xxBean>", "value" : "${fields3}"},
            {"name" : "fields4", "type" : "xx.xxx.xxBean", "value" : "${fields4}"}
        ],
        "struct" : [
            {
                "bean" : "xx.xxx.xxBean",
                "fields" : [
                    {"name" : "var1", "type" : "String"},
                    {"name" : "var2", "type" : "xx.xxx.xxBean11"}
                ]
            },
            {
                "bean" : "xx.xxx.xxEnum",
                "fields" : [
                    {"name" : "FOO", "type" : "Enum"},
                    {"name" : "BAR", "type" : "Enum"}
                ]
            }
        ]
    }
```

### socket
```
    {
        "host" : "192.168.35.78",
        "port" : "7000"
    }
    {
        "resource" : "",
        "method" : "",
        "return" : "String",
        "variables" : [
            //是否有回执,默认false
            {"name" : "#ack", "type" : "Boolean", "value" : "${ack}"},
            //sasl socket鉴权
            {"name" : "#username", "type" : "String", "value" : "${username}"},
            {"name" : "#password", "type" : "String", "value" : "${password}"},
            {"name" : "uuid", "type" : "String", "value" : "${uuid}"},
            {"name" : "group", "type" : "String", "value" : "${group}"},
            {"name" : "source", "type" : "String", "value" : "${source}"},
            {"name" : "target", "type" : "String", "value" : "${target}"},
            {"name" : "type", "type" : "Integer", "value" : "${type}"},
            {"name" : "body", "type" : "String", "value" : "${body}"}
        ]
    }
```

## 数据模板绑定
```
    {
        "bid" : "1232asdf9a0949090fd901930f9",
        "index" : 5,
        "var1" : "v1",
        "var2" : "v2",
        "fields2" : [4,5,6],
        "fields3" : [{"var1" : "v1", "var2" : "v2"},{"var1" : "v3", "var2" : "v4"}],
        "fields4" : {"var1" : "v1", "var2" : "v2"}
    }
```
## 绑定结果
    略
    

