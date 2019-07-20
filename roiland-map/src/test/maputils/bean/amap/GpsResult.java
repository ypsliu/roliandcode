package maputils.bean.amap;

/**
 * GPS结果
 * @author kevin.gong
 * @version 1.0.0 2015-11-24 下午3:33:34
 * @since JDK1.7
 */
public class GpsResult {

	/**
	 * 状态 - "1":成功
	 */
	private String status;
	/**
	 * 文字信息
	 */
	private String info;
	/**
	 * 信息码
	 */
	private String infocode;
	/**
	 * 经纬度(格式如:"100.331376,30.217404")
	 */
	private String locations;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfocode() {
		return infocode;
	}

	public void setInfocode(String infocode) {
		this.infocode = infocode;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}
}
