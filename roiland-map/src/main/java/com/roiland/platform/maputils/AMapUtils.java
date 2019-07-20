package com.roiland.platform.maputils;

import com.roiland.platform.maputils.bean.GPSBean;

/**
 * AMAP工具类
 * @author kevin.gong
 * @version  2015-11-24 下午3:31:07
 * @since JDK1.7
 */
public class AMapUtils {

	    /**
	     * 本地公式纠偏
	     * @param gpsBean 传入经纬度对象
	     * @return 经纬度对象是否已经被纠偏(true:纠偏成功;false:失败)
	     */
	    public static boolean gpsCoor(GPSBean gpsBean){
	    	return MapHelper.wgs2gcj(gpsBean);
	    }
	    
}
