package com.roiland.platform.maputils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roiland.platform.maputils.bean.GPSBean;

public class MapHelper {

	public static final Gson gson = new GsonBuilder()
    .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
    .serializeNulls()
    .disableHtmlEscaping()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式
    .setPrettyPrinting() //对json结果格式化.
    .create();
	
	//火星坐标系转换使用
    private static final double pi = 3.14159265358979324;
    
    //百度坐标系转换使用
    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    
    // Krasovsky 1940
    //
    // a = 6378245.0, 1/f = 298.3
    // b = a * (1 - f)
    // ee = (a^2 - b^2) / a^2;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;
	
	public static boolean isNotEmpty(String str){
		if(str == null || "".equals(str)){
			return false;
		} else {
			return true;
		}
	}
	
	public static final boolean outOfChina(GPSBean gpsBean) {
        double lng = gpsBean.getLng();
        double lat = gpsBean.getLat();
        return outOfChina(lng, lat);
    }
	
	private static final boolean outOfChina(double lng, double lat) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }
	
	/**
     * 地球坐标系转换为火星坐标系
     *
     * @param gpsBean 地球坐标系
     * @return 火星坐标系
     */
    public static boolean wgs2gcj(GPSBean gpsBean) {
    	boolean result = false;
        if (gpsBean != null && !outOfChina(gpsBean)) {
            double wgsLat = gpsBean.getLat();
            double wgsLon = gpsBean.getLng();
            double gcjLat, gcjLng;

            double dLat = transformLat(wgsLon - 105.0, wgsLat - 35.0);
            double dLng = transformLng(wgsLon - 105.0, wgsLat - 35.0);
            double radLat = wgsLat / 180.0 * pi;
            double magic = Math.sin(radLat);
            magic = 1 - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
            dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
            gcjLat = wgsLat + dLat;
            gcjLng = wgsLon + dLng;
            gpsBean.setLat(gcjLat);
            gpsBean.setLng(gcjLng);
            result = true;
        }
        return result;
    }
    
    /**
     * 火星坐标系转换为百度坐标系
     *
     * @param gpsBean 火星坐标系
     * @return 百度坐标系
     */
    public static boolean gcj2bd(GPSBean gpsBean) {
    	boolean result = false;
        if (gpsBean != null && !outOfChina(gpsBean)) {
            double x = gpsBean.getLng();
            double y = gpsBean.getLat();
            double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);

            double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);

            double lng = z * Math.cos(theta) + 0.0065;

            double lat = z * Math.sin(theta) + 0.006;
            gpsBean.setLat(lat);
            gpsBean.setLng(lng);
            result = true;
        }
        return result;
    }
    
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLng(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
}
