package usi.dbdp.portal.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

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
     * 将字符串按格式，格式化成日期（采用阿帕奇的工具类，性能好，线程安全）
     * @param stringValue 字符串日期
     * @param formatPattern 要格式化成的日期
     * @return
     */
	public static final Date parse(String stringValue, String formatPattern) {
		if(stringValue==null || formatPattern==null) {
			return null;
		}
		Date finalDate = null;
		try {
			finalDate = DateUtils.parseDate(stringValue, new String [] {formatPattern});
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return finalDate;
	}
	
    /**
     * 格式化日期（采用阿帕奇的工具类，性能好，线程安全）
     * @param date 日期
     * @param formatPattern 要格式化的形式
     * @return
     */
    public static final String format(Date date, String formatPattern) {
		if (date == null) {
			return "";
		}
		return DateFormatUtils.format(date, formatPattern);
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
