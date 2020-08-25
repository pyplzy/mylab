package usi.dbdp.portal.app.util;

import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.SHA256Util;

/**
 * Oracle 密码加密Util
 * @author xiang
 *
 */
public class OraclePasswdUtil {
	public static String DB_SQL_SWITCH = ConfigUtil.getValue("dbSqlSwitch");//加密开关
	public static String DB_SQL_KEY="";//密钥
	
	static{
		DB_SQL_SWITCH = ConfigUtil.getValue("dbSqlSwitch");
		if(DB_SQL_SWITCH.equals("ON")){
			DB_SQL_KEY = FilePasswdUtil.decrypt(ConfigUtil.getValue("dbSqlKey"));//密钥
		}
	}
	/**
	 * 获取加密的sql字段字符串
	 * @param name
	 * @return
	 */
	public static String getEncrypt(Object name){
		if(DB_SQL_SWITCH.equals("ON")){
			return "ENCRYPT("+name+",'"+DB_SQL_KEY+"')";
		}else{
			return name.toString();
		}
	}
	/**
	 * 获取加密的sql字段字符串
	 * @param name
	 * @return
	 */
	public static String getEncryptStr(Object name){
		if(DB_SQL_SWITCH.equals("ON")){
			return "ENCRYPT("+name+",'"+DB_SQL_KEY+"')";
		}else{
			return name.toString();
		}
	}
	/**
	 * 获取解密的sql字段字符串
	 * @param name
	 * @return
	 */
	public static String getDecryptStr(String name){
		if(DB_SQL_SWITCH.equals("ON")){
			return "DECRYPT("+name+",'"+DB_SQL_KEY+"')";
		}else{
			return name;
		}
	}
	/**
	 * 获取解密的sql字段字符串,赋予别名
	 * @param name
	 * @return
	 */
	public static String getDecryptAsStr(String name){
		if(DB_SQL_SWITCH.equals("ON")){
			return "DECRYPT("+name+",'"+DB_SQL_KEY+"') AS "+name+"";
		}else{
			return name;
		}
	}
}
