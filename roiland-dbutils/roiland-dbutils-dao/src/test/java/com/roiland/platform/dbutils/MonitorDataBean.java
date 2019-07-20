package com.roiland.platform.dbutils;

import com.roiland.platform.dbutils.annotation.Column;
import com.roiland.platform.dbutils.annotation.Table;

/**
 * 
 */
@Table(name = "t_monitor_data", schema="dubbo-monitor")
public class MonitorDataBean {

        // 自增主键
        @Column(name = "id")
        private java.math.BigInteger id;
        // 采集时间
        @Column(name = "collect_time")
        private java.util.Date collectTime;
        // 应用名
        @Column(name = "application")
        private String application;
        // 服务名
        @Column(name = "service")
        private String service;
        // 方法名
        @Column(name = "method")
        private String method;
        // 组名
        @Column(name = "group")
        private String group;
        // 版本
        @Column(name = "version")
        private String version;
        // 服务消费者地址
        @Column(name = "client")
        private String client;
        // 服务提供者地址
        @Column(name = "server")
        private String server;
        // 总成功次数
        @Column(name = "total_success")
        private Integer totalSuccess;
        // 总失败次数
        @Column(name = "total_failure")
        private Integer totalFailure;
        // 未在dubbo相关文档找到说明，疑似数据长度
        @Column(name = "total_input")
        private Integer totalInput;
        // 未在dubbo相关文档找到说明，疑似数据长度
        @Column(name = "total_output")
        private Integer totalOutput;
        // 总执行时间
        @Column(name = "total_elapsed")
        private Integer totalElapsed;
        // 并发执行条数
        @Column(name = "concurrent")
        private Integer concurrent;
        // 未在dubbo相关文档找到说明，疑似数据长度
        @Column(name = "max_input")
        private Integer maxInput;
        // 未在dubbo相关文档找到说明，疑似数据长度
        @Column(name = "max_output")
        private Integer maxOutput;
        // 最大执行时间
        @Column(name = "max_elapsed")
        private Integer maxElapsed;
        // 最大并发条数
        @Column(name = "max_concurrent")
        private Integer maxConcurrent;
        // 数据插入时间
        @Column(name = "create_time")
        private java.util.Date createTime;

        /**
          * 获取自增主键
          * @return id
          */
        public java.math.BigInteger getId() { return id; }

        /**
          * 设置自增主键
          * @param id
          */
        public void setId(java.math.BigInteger id) { this.id = id; }

        /**
          * 获取采集时间
          * @return collectTime
          */
        public java.util.Date getCollectTime() { return collectTime; }

        /**
          * 设置采集时间
          * @param collectTime
          */
        public void setCollectTime(java.util.Date collectTime) { this.collectTime = collectTime; }

        /**
          * 获取应用名
          * @return application
          */
        public String getApplication() { return application; }

        /**
          * 设置应用名
          * @param application
          */
        public void setApplication(String application) { this.application = application; }

        /**
          * 获取服务名
          * @return service
          */
        public String getService() { return service; }

        /**
          * 设置服务名
          * @param service
          */
        public void setService(String service) { this.service = service; }

        /**
          * 获取方法名
          * @return method
          */
        public String getMethod() { return method; }

        /**
          * 设置方法名
          * @param method
          */
        public void setMethod(String method) { this.method = method; }

        /**
          * 获取组名
          * @return group
          */
        public String getGroup() { return group; }

        /**
          * 设置组名
          * @param group
          */
        public void setGroup(String group) { this.group = group; }

        /**
          * 获取版本
          * @return version
          */
        public String getVersion() { return version; }

        /**
          * 设置版本
          * @param version
          */
        public void setVersion(String version) { this.version = version; }

        /**
          * 获取服务消费者地址
          * @return client
          */
        public String getClient() { return client; }

        /**
          * 设置服务消费者地址
          * @param client
          */
        public void setClient(String client) { this.client = client; }

        /**
          * 获取服务提供者地址
          * @return server
          */
        public String getServer() { return server; }

        /**
          * 设置服务提供者地址
          * @param server
          */
        public void setServer(String server) { this.server = server; }

        /**
          * 获取总成功次数
          * @return totalSuccess
          */
        public Integer getTotalSuccess() { return totalSuccess; }

        /**
          * 设置总成功次数
          * @param totalSuccess
          */
        public void setTotalSuccess(Integer totalSuccess) { this.totalSuccess = totalSuccess; }

        /**
          * 获取总失败次数
          * @return totalFailure
          */
        public Integer getTotalFailure() { return totalFailure; }

        /**
          * 设置总失败次数
          * @param totalFailure
          */
        public void setTotalFailure(Integer totalFailure) { this.totalFailure = totalFailure; }

        /**
          * 获取未在dubbo相关文档找到说明，疑似数据长度
          * @return totalInput
          */
        public Integer getTotalInput() { return totalInput; }

        /**
          * 设置未在dubbo相关文档找到说明，疑似数据长度
          * @param totalInput
          */
        public void setTotalInput(Integer totalInput) { this.totalInput = totalInput; }

        /**
          * 获取未在dubbo相关文档找到说明，疑似数据长度
          * @return totalOutput
          */
        public Integer getTotalOutput() { return totalOutput; }

        /**
          * 设置未在dubbo相关文档找到说明，疑似数据长度
          * @param totalOutput
          */
        public void setTotalOutput(Integer totalOutput) { this.totalOutput = totalOutput; }

        /**
          * 获取总执行时间
          * @return totalElapsed
          */
        public Integer getTotalElapsed() { return totalElapsed; }

        /**
          * 设置总执行时间
          * @param totalElapsed
          */
        public void setTotalElapsed(Integer totalElapsed) { this.totalElapsed = totalElapsed; }

        /**
          * 获取并发执行条数
          * @return concurrent
          */
        public Integer getConcurrent() { return concurrent; }

        /**
          * 设置并发执行条数
          * @param concurrent
          */
        public void setConcurrent(Integer concurrent) { this.concurrent = concurrent; }

        /**
          * 获取未在dubbo相关文档找到说明，疑似数据长度
          * @return maxInput
          */
        public Integer getMaxInput() { return maxInput; }

        /**
          * 设置未在dubbo相关文档找到说明，疑似数据长度
          * @param maxInput
          */
        public void setMaxInput(Integer maxInput) { this.maxInput = maxInput; }

        /**
          * 获取未在dubbo相关文档找到说明，疑似数据长度
          * @return maxOutput
          */
        public Integer getMaxOutput() { return maxOutput; }

        /**
          * 设置未在dubbo相关文档找到说明，疑似数据长度
          * @param maxOutput
          */
        public void setMaxOutput(Integer maxOutput) { this.maxOutput = maxOutput; }

        /**
          * 获取最大执行时间
          * @return maxElapsed
          */
        public Integer getMaxElapsed() { return maxElapsed; }

        /**
          * 设置最大执行时间
          * @param maxElapsed
          */
        public void setMaxElapsed(Integer maxElapsed) { this.maxElapsed = maxElapsed; }

        /**
          * 获取最大并发条数
          * @return maxConcurrent
          */
        public Integer getMaxConcurrent() { return maxConcurrent; }

        /**
          * 设置最大并发条数
          * @param maxConcurrent
          */
        public void setMaxConcurrent(Integer maxConcurrent) { this.maxConcurrent = maxConcurrent; }

        /**
          * 获取数据插入时间
          * @return createTime
          */
        public java.util.Date getCreateTime() { return createTime; }

        /**
          * 设置数据插入时间
          * @param createTime
          */
        public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }

}
