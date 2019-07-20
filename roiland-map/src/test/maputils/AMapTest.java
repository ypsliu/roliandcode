package maputils;


import java.io.IOException;

import maputils.bean.amap.GpsResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.roiland.platform.maputils.MapHelper;
import com.roiland.platform.maputils.bean.GPSBean;

/**
 * AMAP工具类
 * @author kevin.gong
 * @version  2015-11-24 下午3:31:07
 * @since JDK1.7
 */
public class AMapTest {

	    private static final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	    private static HttpClientParams httpClientParams = new HttpClientParams();
	    private static final String ROILAND_KEY = "a682480529195c70fab2c87b6fabfc74";
	    
	    static {
	        httpClientParams.setParameter("http.protocol.content-charset", "UTF-8");
	        httpClientParams.setParameter(
	                        HttpMethodParams.USER_AGENT,
	                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.10) Gecko/2009042316 Firefox/3.0.10 (.NET CLR 3.5.30729)");
	        httpClientParams.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
	        // 设置连接超时时间(单位毫秒)
	        httpClientParams.setConnectionManagerTimeout(10000);
	        // 设置读数据超时时间(单位毫秒)
	        httpClientParams.setSoTimeout(10000);
	    }
	    
	    /**
	     * 本地公式纠偏
	     * @param gpsBean 传入经纬度对象
	     * @return 经纬度对象是否已经被纠偏(true:纠偏成功;false:失败)
	     */
	    public static boolean gpsCoorLocation(GPSBean gpsBean){
	    	return MapHelper.wgs2gcj(gpsBean);
	    }
	    
	    /**
	     * 通过网站纠偏
	     * @param gpsBean 传入经纬度对象
	     * @return 经纬度对象是否已经被纠偏(true:纠偏成功;false:失败)
	     */
	    public static boolean gpsCoorOnline(GPSBean gpsBean){
	    	boolean result = false;
	    	if(gpsBean != null){
	    		double lat = gpsBean.getLat();
	    		double lng = gpsBean.getLng();
	    		StringBuilder url = new StringBuilder("http://restapi.amap.com/v3/assistant/coordinate/convert?key=");
	    		url.append(ROILAND_KEY);
	    		url.append("&locations=");
	    		url.append(lng);
	    		url.append(",");
	    		url.append(lat);
	    		url.append("&coordsys=gps");
	    		HttpClient client = new HttpClient(httpClientParams, connectionManager);
	            GetMethod method = new GetMethod(url.toString());
	            try {
	    			client.executeMethod(method);
	    			String json = method.getResponseBodyAsString();
	    			if(MapHelper.isNotEmpty(json)){
	    				GpsResult gpsResult = MapHelper.gson.fromJson(json, GpsResult.class);
	    				if(gpsResult!=null && "1".equals(gpsResult.getStatus()) && MapHelper.isNotEmpty(gpsResult.getLocations())){
	    					String loc = gpsResult.getLocations();
	    					String[] locArr = loc.split(",");
	    					if(locArr.length >= 2){
	    						String lngS = locArr[0];
	    						String latS = locArr[1];
	    						if(MapHelper.isNotEmpty(lngS) && MapHelper.isNotEmpty(latS)){
	    							gpsBean.setLng(Double.valueOf(lngS));
	    							gpsBean.setLat(Double.valueOf(latS));
	    							result = true;
	    						}
	    					}
	    				}
	    			} 
	    		} catch (HttpException e) {
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	return result;
	    }
}
