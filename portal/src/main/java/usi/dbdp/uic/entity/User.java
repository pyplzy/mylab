package usi.dbdp.uic.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息实体
 * @author lmwang
 * 创建时间：2015-2-5 下午2:16:02
 */
public class User implements Serializable{

	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;

	/**用户ID*/
	private long id;
	
	/**工号*/
	private String userId;	
	
	/**登录号**/
	private String loginId;
	
	/**用户姓名*/
	private String userName;
	
	/**性别（0：未知，1：男，2：女）*/
	private int gender;
	
	/**手机号码*/
	private String mobileNo;
	
	/**用户类型（1：普通用户，2：测试用户，3：运维用户）*/
	private int userType; 	
	
	/**机构ID*/
	private long orgId;
	
	/**删除标识（1：已删除，0：未删除）*/
	private int delFlag;
	
	/**密码*/
	private String password;
	
	/**密码有效期*/
	private int duration;
	
	/**密码最后修改时间*/
	private Date pwdLastModTime;
	
	/**密码最后错误时间*/
	private Date lstErrPwdTime;
	
	/**密码错误次数*/
	private int pwdErrCnt;
	
	/**创建时间*/
	private Date createTime;
	
	/**机构名称*/
	private String orgName;
	
	/**
	 * 获取机构名称
	 * @return 机构名称
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * 设置机构名称
	 * @param userId 机构名称
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * 获取用户ID
	 * @return 用户ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置用户ID
	 * @param id 用户ID
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取工号
	 * @return 工号
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置工号
	 * @param userId 工号
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}	
	
	/**
	 * 获取登录号
	 * @return 登录号
	 */
	public String getLoginId() {
		return loginId;
	}
	
	/**
	 * 设置登录号
	 * @param loginId 登录号
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}	
	
	/**
	 * 获取用户名
	 * @return 用户名
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置用户名
	 * @param userName 用户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 获取手机号码
	 * @return 手机号码
	 */
	public String getMobileNo() {
		return mobileNo;
	}
	
	/**
	 * 设置手机号码
	 * @param mobileNo 手机号码
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	/**
	 * 获取用户类型（1：普通用户，2：测试用户，3：运维用户）
	 * @return 用户类型（1：普通用户，2：测试用户，3：运维用户）
	 */
	public int getUserType() {
		return userType;
	}
	
	/**
	 * 设置用户类型（1：普通用户，2：测试用户，3：运维用户）
	 * @param userType 用户类型（1：普通用户，2：测试用户，3：运维用户）
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

	/**
	 * 获取性别（0：未知，1：男，2：女）
	 * @return 性别（0：未知，1：男，2：女）
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * 设置性别（0：未知，1：男，2：女）
	 * @param gender 性别（0：未知，1：男，2：女）
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * 获取机构id
	 * @return 机构id
	 */
	public long getOrgId() {
		return orgId;
	}

	/**
	 * 设置机构id
	 * @param orgId 机构id
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	/**
	 * 获取用户状态（1：启用，0：停用）
	 * @return 用户状态（1：启用，0：停用）
	 */
	public int getDelFlag() {
		return delFlag;
	}

	/**
	 * 设置用户状态（1：启用，0：停用）
	 * @param state 用户状态（1：启用，0：停用）
	 */
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 获取用户密码
	 * @return 用户密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置用户密码
	 * @param password 用户密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取密码有效期
	 * @return 密码有效期
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * 设置密码有效期
	 * @param duration 密码有效期
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * 获取密码最后修改时间
	 * @return 密码最后修改时间
	 */
	public Date getPwdLastModTime() {
		return pwdLastModTime;
	}

	/**
	 * 设置密码最后修改时间
	 * @param pwdLastModTime 密码最后修改时间
	 */
	public void setPwdLastModTime(Date pwdLastModTime) {
		this.pwdLastModTime = pwdLastModTime;
	}

	/**
	 * 获取密码最后错误时间
	 * @return 密码最后错误时间
	 */
	public Date getLstErrPwdTime() {
		return lstErrPwdTime;
	}

	/**
	 * 设置密码最后错误时间
	 * @param lstErrPwdTime 密码最后错误时间
	 */
	public void setLstErrPwdTime(Date lstErrPwdTime) {
		this.lstErrPwdTime = lstErrPwdTime;
	}

	/**
	 * 获取密码错误次数
	 * @return 密码错误次数
	 */
	public int getPwdErrCnt() {
		return pwdErrCnt;
	}

	/**
	 * 设置密码错误次数
	 * @param pwdErrCnt 密码错误次数
	 */
	public void setPwdErrCnt(int pwdErrCnt) {
		this.pwdErrCnt = pwdErrCnt;
	}

	/**
	 * 获取创建时间
	 * @return 密码创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	
	/**
	 * 设置创建时间
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
