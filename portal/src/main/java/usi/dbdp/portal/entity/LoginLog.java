package usi.dbdp.portal.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 登录日志
 * @author ma.guangming
 *
 */
public class LoginLog implements Serializable   {
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	private long loginId;//登录ID
	private String userId;//登录帐号
	private String loginIp;//登录IP
	private String sessionId;//会话ID
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date loginTime;//登录时间
	private Date logoutTime;//登出时间
	private int isSuccess;//登录结果（0：失败，1：成功）
	/**
	 * 获取登录id
	 * @return 登录id
	 */
	public long getLoginId() {
		return loginId;
	}
	/**
	 * 设置登录id
	 * @param loginId 登录id
	 */
	public void setLoginId(long loginId) {
		this.loginId = loginId;
	}
	/**
	 * 获取登录帐号
	 * @return 登录帐号
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置登录帐号
	 * @param userId 登录帐号
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取登录ip
	 * @return 登录ip
	 */
	public String getLoginIp() {
		return loginIp;
	}
	/**
	 * 设置登录ip
	 * @param loginIp 登录ip
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	/**
	 * 获取会话id
	 * @return 会话id
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * 设置会话id
	 * @param sessionId 会话id
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * 获取登录时间
	 * @return 登录时间
	 */
	public Date getLoginTime() {
		return loginTime;
	}
	/**
	 * 设置登录时间
	 * @param loginTime 登录时间
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	/**
	 * 获取登录时间
	 * @return 登录时间
	 */
	public Date getLogoutTime() {
		return logoutTime;
	}
	/**
	 * 设置登出时间
	 * @param logoutTime 登出时间
	 */
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}
	/**
	 * 获取登录结果
	 * @return 登录结果
	 */
	public int getIsSuccess() {
		return isSuccess;
	}
	/**
	 * 设置登录结果
	 * @param isSuccess 登录结果
	 */
	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}
}
