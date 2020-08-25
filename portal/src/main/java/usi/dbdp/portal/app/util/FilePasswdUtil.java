package usi.dbdp.portal.app.util;

import org.jasypt.util.text.BasicTextEncryptor;

public class FilePasswdUtil {
	private static String KEYSTR;
	/**
	 * 初始化key信息
	 */
	static{
		KEYSTR=System.getProperty("passwdKey");
	}
	/**
     * AES 加密操作
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content) {
    	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    	textEncryptor.setPassword(KEYSTR);
        return textEncryptor.encrypt(content);
    }

    /**
     * AES 解密操作
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content) {
    	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();     
        textEncryptor.setPassword(KEYSTR);
		return textEncryptor.decrypt(content);
    }
    
    public static void main(String[] args) {
    	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();     
        textEncryptor.setPassword(KEYSTR);    
        String newPassword = textEncryptor.encrypt("Chicken@!#2008");    
        System.out.println(newPassword);    
//        解密    
        BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();     
        textEncryptor2.setPassword("password");     
        String oldPassword = textEncryptor2.decrypt(newPassword);       
        System.out.println(oldPassword);    
        System.out.println("--------------------------"); 
	}
}
