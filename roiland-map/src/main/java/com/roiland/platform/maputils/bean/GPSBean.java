package com.roiland.platform.maputils.bean;

/**
 * 存储GPS坐标的实体类，用来存储GPS的经纬度信息。
 * User:    berton.wang
 * Date:    13-9-17
 * Version: 1.0
 * History:
 *          1.0      2013/09/17     初始做成
 */
public class GPSBean {
    /**
     * 经度
     */
    private double lng;
    @Override
	public String toString() {
		return "\"off_lat\":\"" + lat + "\", \"off_lng\":\"" + lng + "\"";
	}

	/**
     * 纬度
     */
    private double lat;

    public GPSBean(double lng,double lat) {
        this.lat = lat;
        this.lng = lng;
    }
	public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
	public void setLng(double lng) {
		this.lng = lng;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
}
