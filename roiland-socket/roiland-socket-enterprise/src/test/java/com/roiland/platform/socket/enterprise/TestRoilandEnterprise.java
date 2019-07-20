package com.roiland.platform.socket.enterprise;

import com.alibaba.fastjson.JSON;
import com.roiland.platform.socket.core.IDataCallback;
import com.roiland.platform.socket.core.IDataHandler;
import com.roiland.platform.socket.core.ProtocolBean;
import com.roiland.platform.socket.core.RoilandChannel;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * Created by jeffy.yang on 16-6-22.
 */
public class TestRoilandEnterprise {

    public static void main(String[] args) {
        NettyEnterpriseBootstrap.with(new IDataHandler() {
            @Override
            public void handle(RoilandChannel channel, ProtocolBean protocolBean) {

            }
        }).login("e22ee0050a044977be4fe90e68d13a93", "123456", new IDataCallback() {
            @Override
            public void callback(Channel channel, Map<String, String> params, Throwable throwable) {
                System.out.println(JSON.toJSONString(params));
            }
        }).build().connect("IDC.APP.CSM_DEV", "192.168.34.115", 7000);

    }
}
