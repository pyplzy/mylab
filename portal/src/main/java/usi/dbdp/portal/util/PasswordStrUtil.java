package usi.dbdp.portal.util;

import javax.servlet.http.HttpSession;

public class PasswordStrUtil {
	private static String password=ConfigUtil.getValue("userPassword");
	//获取配置文件的中配置的值
	public static String getJointStr(String loginId){	
		//String logId=(String) session.getAttribute("userId");
		String commands=loginId+password;
		
		System.out.println("---------------->>>>>>>>>>>>>>>>>>>"+commands);
		return SHA256Util.encryptSHA(commands);
	}
	

	
}
