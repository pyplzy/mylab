package usi.dbdp.portal.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
/**
 * 加密算法
 * @author xiang
 *
 */
public class SHA256Util {
	/**
	 * 利用java原生的摘要实现SHA256加密
	 * @param content
	 * @return
	 */
	public static String encryptSHA(String content) {
        String encodedCipherText = null;
        try {
        	 MessageDigest md = MessageDigest.getInstance("SHA-256");
             md.update(content.getBytes("utf-8"));
             encodedCipherText=Base64.encodeBase64String(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedCipherText;
    }
}