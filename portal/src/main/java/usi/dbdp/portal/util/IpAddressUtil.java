package usi.dbdp.portal.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取请求Ip
 * 
 * @author lmwang 2013-9-26 下午3:59:39
 */
public class IpAddressUtil {

	/**
	 * 根据请求获取ip
	 * 
	 * @param request
	 * @return ip地址
	 */
	public static String getReqIp(HttpServletRequest request) {
		// 取nginx加的真实ip
		String ip = request.getHeader("x-forwarded-for");// head里面的参数不区分大小写
		// 取不到的话取
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			// 渠道的话，考虑多级代理
		} else {
			String[] ipArr = ip.split(",");
			// 取第一个非unknown
			for (String tmp : ipArr) {
				if (!"unknown".equals(tmp)) {
					ip = tmp;
					break;
				}
			}
		}
		return ip;
	}
}
