package com.roiland.platform.maputils;

import com.roiland.platform.maputils.bean.GPSBean;

public class BaiduMapUtils {
	    
	    /**
	     * 本地公式纠偏
	     * @param gpsBean 传入经纬度对象
	     * @return 经纬度对象是否已经被纠偏(true:纠偏成功;false:失败)
	     */
	    public static boolean gpsCoor(GPSBean gpsBean){
	    	boolean result = false;
	    	if(MapHelper.wgs2gcj(gpsBean)){
	    		if(MapHelper.gcj2bd(gpsBean)){
	    			result = true;
	    		}
	    	}
	    	return result;
	    }
	    
}
