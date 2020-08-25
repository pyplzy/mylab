package usi.dbdp.uic.util;


/**
 * 常用工具类
 * @author fan.fan
 * @date 2014-3-31 上午11:18:45
 */
public class CommonUtil {
	
	/**
     * 验证一个字符串是否有值(既不是null,也不是空字符串)
     * @param value
     * @return
     */
    public static final boolean hasValue(String value) {
    	return value != null && value.trim().length() > 0;
    }
    /**
	 * 调用加密工具类进行加密，加密工具类可被使用此框架的系统覆盖
	 * @param s
	 * @return 加密后字符串
	 */
    public static final String getMd5(String s) {
    	return EncryptUtil.doEncrypt(s);
    }
    
  
}
