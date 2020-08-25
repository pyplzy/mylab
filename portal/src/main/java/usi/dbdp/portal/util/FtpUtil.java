package usi.dbdp.portal.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * ftp工具类（理论上方法要逐级抛异常出去，为了简单，所以没抛异常）（开了防火墙不行）
 * @author lmwang
 * 创建时间：2015-4-24 下午2:32:03
 */
public class FtpUtil {

	private static final Logger LOGGER = Logger.getLogger(FtpUtil.class);
	//ftp主机ip
	private static String ftpHostIp = ConfigUtil.getValue("ftpHostIp");
	//ftp端口
	private static String ftpPort = ConfigUtil.getValue("ftpPort");
	//ftp用户名
	private static String userName = ConfigUtil.getValue("userName");
	//ftp密码
	private static String password = ConfigUtil.getValue("password");

	//默认超时时间（单位毫秒）
	private static final int DEFAUL_TTIMEOUT = 120000;
	//连接超时时间（单位毫秒）
	private static final int CONNECT_TIMEOUT = 120000;
	//数据超时时间（单位毫秒）
	private static final int DATA_TIMEOUT = 120000;
	
	/**
	 * 获取ftp客户端连接
	 * @return
	 */
	private static FTPClient getFtpClient() {
		FTPClient  ftpClient = null;
		int reply; 
		try {
			ftpClient = new FTPClient();

			ftpClient.setControlEncoding("UTF-8");
			ftpClient.connect(ftpHostIp);
			ftpClient.setDefaultPort(Integer.parseInt(ftpPort));
			ftpClient.login(userName, password);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setDefaultTimeout(DEFAUL_TTIMEOUT);
			ftpClient.setConnectTimeout(CONNECT_TIMEOUT);
			ftpClient.setDataTimeout(DATA_TIMEOUT);
			
			reply = ftpClient.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				LOGGER.info("FTP服务器拒绝连接");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("登录ftp服务器【" + ftpHostIp + "】失败,连接超时！");
		}
		return ftpClient;
	}
	/**
	 * 创建目录
	 * @param ftpClient ftp客户端连接
	 * @param dir 目录名称
	 * @return
	 */
	private static boolean makeDirectory(FTPClient  ftpClient,String dir) {
		boolean flag = true;  
        try {  
            flag = ftpClient.makeDirectory(dir);  
            if (flag) {  
                LOGGER.info("make Directory " +dir +" succeed");
            } else {  
                LOGGER.info("make Directory " +dir+ " false");
            }  
        } catch (Exception e) { 
        	flag = false;
            e.printStackTrace();  
        }  
        return flag;  
	}
	
	 /** 
     * 进入到服务器的某个目录下 
     * @param ftpClient ftp客户端连接
     * @param directory 目录
     */  
    private static boolean changeWorkingDirectory(FTPClient ftpClient,String directory) {  
    	boolean flag = true;
        try {  
        	flag = ftpClient.changeWorkingDirectory(directory);  
        } catch (IOException e) { 
        	flag = false;
            e.printStackTrace();  
        }  
        return flag;
    } 
    
    /**
     * 上传
     * @param fis 源文件输入流
     * @param dir 路径
     * @param newFileName 新文件名
     * @return
     */
	public static boolean upload(InputStream fis,String dir,String newFileName) {
		//获取ftp客户端连接
		FTPClient  ftpClient = getFtpClient();
		boolean flag = true;
		try {
			//主动模式，预防卡死
			ftpClient.enterLocalPassiveMode();
			//切换到家目录
			ftpClient.changeWorkingDirectory("~");
			//目录不存在
			if(!changeWorkingDirectory(ftpClient,dir)) {
				//创建目录
				makeDirectory(ftpClient,dir);
				//再切换目录
				changeWorkingDirectory(ftpClient,dir);
			}

			flag = ftpClient.storeFile(newFileName, fis);
			if (flag) {
				LOGGER.info("upload File succeed");
			} else {
				LOGGER.info("upload File false");
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 下载
	 * @param os 输出流
	 * @param fileName 带路径的文件名
	 * @return
	 */
	public static boolean download(OutputStream os,String fileName) {
		//获取ftp客户端连接
		FTPClient  ftpClient = getFtpClient();
		boolean flag = true;
		try {
			//主动模式，预防卡死
			ftpClient.enterLocalPassiveMode();
			//切换到家目录
			ftpClient.changeWorkingDirectory("~");
			flag = ftpClient.retrieveFile(fileName, os);
			if (flag) {
				LOGGER.info("file download successfully");
			} else {
				LOGGER.info("file download not successfully");
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} 
		return flag;
	}

	/**
	 * 删除文件
	 * @param fileName 文件名含路径（相对路径）
	 * @return 
	 */
	public static boolean deleteFile(String fileName) {
		//获取ftp客户端连接
		FTPClient  ftpClient = getFtpClient();
		boolean flag = true;
		try {
			//切换到家目录
			ftpClient.changeWorkingDirectory("~");
			flag = ftpClient.deleteFile(fileName);
			if (flag) {
				LOGGER.info("file delete successfully");
			} else {
				LOGGER.info("file is not delete successfully");
			}
		} catch (IOException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}    
	
}
