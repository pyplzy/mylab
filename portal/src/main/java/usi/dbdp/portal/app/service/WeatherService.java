package usi.dbdp.portal.app.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.HttpclientUtil;
import usi.dbdp.portal.util.JacksonUtil;

/**
 * 天气服务
 * @author lmwang
 * 创建时间：2015-4-24 上午10:29:23
 */
@Service
public class WeatherService {

	/**
	 * 通过http+json的接口方式，调用天气数据
	 * @param session 存放城市编码和调用接口的合法用户
	 * @return 天气json数据，形如：
	 * {  "city": "合肥", 
		“PM25”:{
				“AQI_num”:”44”,
				“AQI_level”:”优”,
				“concentration”:”23微克/立方米”,
				“other”:”击败了全国69%的城市，目前城市排名115”
				},
		“weather”:{
				“imageCode”:”1”,
				“imageExplain”:”多云”,
				“temperature”:”28~15℃”
				},
		“prompt”:”空气很好，可以外出活动，呼吸新鲜空气”,
		“other”:”数据来源：中国环境监测总站 最后更新：2015-04-23 17:00”
		}
	 */
	public Map<String,Object> getWeatherData(HttpSession session) {
		String leaglWeatherUser = ConfigUtil.getValue("leaglWeatherUser");
		String city = String.valueOf((Long)session.getAttribute("city"));
		String cityPinyin = "beijing";//默认是北京
		
		//天气数据返回结果
		Map<String,Object> weatherData = null;
		//接口调用结果
		String result = "";
//				"{\"status\":\"fail\",\"msg\":\"目标服务器拒绝链接\"}";
				
//				"{" +"    \"city\": \"合肥\"," + 
//						"    \"PM25\": {" + 
//						"        \"AQI_num\": \"44\"," + 
//						"        \"AQI_level\": \"优\"," + 
//						"        \"concentration\": \"23微克/立方米\"," + 
//						"        \"other\": \"击败了全国69%的城市，目前城市排名115\"" + 
//						"    }," + 
//						"    \"weather\": {" + 
//						"        \"imageCode\": \"2\"," + 
//						"        \"imageExplain\": \"多云\"," + 
//						"        \"temperature\": \"28~15℃\"" + 
//						"    }," + 
//						"    \"prompt\": \"空气很好，可以外出活动，呼吸新鲜空气\"," + 
//						"    \"other\": \"数据来源：中国环境监测总站最后更新：2015-04-23 17: 00\"" + 
//						"}";

		
		//是直属部门，
		if("0".equals(city)) {
			String province = String.valueOf((Long)session.getAttribute("province"));
			//集团直属，没有省id，就让他是北京
			if("0".equals(province)) {
				city = "110000";
			//取省前两位加0100，算省会城市
			}else {
				city = province.substring(0,2)+"0100";
			}
			cityPinyin = ConfigUtil.getValue(city);
		}else {
			cityPinyin = ConfigUtil.getValue(city);
		}

		try {
			result = HttpclientUtil.sendRequestByGet(ConfigUtil.getValue("weatherUrl")+"?city="+cityPinyin+"&userID="+leaglWeatherUser);
			weatherData = JacksonUtil.json2map(result);
		}catch (Exception e) {
			weatherData = new HashMap<String,Object>();
			weatherData.put("status", "fail");
			weatherData.put("msg", "接口异常");
			e.printStackTrace();
		}
		return weatherData;
	}
	
}
