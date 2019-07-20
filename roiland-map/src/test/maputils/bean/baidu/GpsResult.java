package maputils.bean.baidu;

import java.util.List;

/**
 * GPS结果
 * @author kevin.gong
 * @version 1.0.0 2015-11-24 下午3:30:22
 * @since JDK1.7
 */
public class GpsResult {

	/**
	 * 状态
	 */
	private int status;
	
	/**
	 * 经纬度信息
	 */
	private List<GpsBean> result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<GpsBean> getResult() {
		return result;
	}

	public void setResult(List<GpsBean> result) {
		this.result = result;
	}
}
