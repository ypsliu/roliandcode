package com.roiland.platform.lang;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class IPUtils {

    private static final Log LOGGER = LogFactory.getLog(IPUtils.class);

    private static Pattern pattern = Pattern.compile("^[1-9]\\d{0,2}\\.[1-9]\\d{0,2}\\.[1-9]\\d{0,2}\\.[1-9]\\d{0,2}$");

    public static String ip() {
        String address = null;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e1) {
            LOGGER.warn("获取IP地址失败，请确认/etc/hosts是否配置");
        }

        if (StringUtils.isEmpty(address)) {
            try {
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        String temp = ips.nextElement().getHostAddress();
                        if (isIp(temp) && !"127.0.0.1".equals(temp)) {
                            return temp;
                        }
                    }
                }
            } catch (SocketException e) {
                LOGGER.warn("尝试从网卡获取地址信息失败");
            }
        }
        return address;
    }

    /**
     * 简单IP地址判断。
     * ① 地址为4组数据组成
     * ② 每组数据不超过3位
     * ③ 以1~9开头
     * 
     * @param ip
     * @return
     */
    public static boolean isIp(final String ip) {
        return pattern.matcher(ip).matches();
    }
}
