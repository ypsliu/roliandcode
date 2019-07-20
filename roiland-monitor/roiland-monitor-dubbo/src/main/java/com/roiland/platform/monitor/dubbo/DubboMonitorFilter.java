package com.roiland.platform.monitor.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.monitor.MonitorService;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.support.RpcUtils;
import com.roiland.platform.monitor.basic.extract.Timer;
import com.roiland.platform.monitor.basic.transform.InfoBean;

@Activate(group = {Constants.PROVIDER})
public class DubboMonitorFilter implements Filter {

    // 调用过程拦截
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        final URL url = invoker.getUrl();
        final String application = url.getParameter(Constants.APPLICATION_KEY);                            // 应用名称
        final String service = invoker.getInterface().getName();                                                        // 获取服务名称
        final String method = RpcUtils.getMethodName(invocation);                                                // 获取方法名
        final String group = url.getParameter(MonitorService.GROUP);
        final String version = url.getParameter(MonitorService.VERSION);
        final String client = RpcContext.getContext().getRemoteHost();
        final String server = url.getParameter(MonitorService.PROVIDER, url.getAddress());

        int success = 0;
        int failure = 0;
        int input = 0;
        int output = 0;

        Timer.Context context = Timer.getInstance().start(new InfoBean(InfoBean.TYPE.DUBBO, application, service, method, group, version, client, server));
        try {
            Result result = invoker.invoke(invocation);                        // 让调用链往下执行
            success = 1;
            if (invocation.getAttachment(Constants.INPUT_KEY) != null) {
                input = Integer.valueOf(invocation.getAttachment(Constants.INPUT_KEY));
            }
            if (result != null && result.getAttachment(Constants.OUTPUT_KEY) != null) {
                output = Integer.valueOf(result.getAttachment(Constants.OUTPUT_KEY));
            }
            return result;
        } catch (RpcException e) {
            failure = 1;
            throw e;
        } finally {
            context.stop(success, failure, input, output);
        }
    }
}