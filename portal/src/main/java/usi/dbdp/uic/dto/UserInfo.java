package usi.dbdp.uic.dto;

import java.io.Serializable;


/**
 * 用户信息和机构信息DTO
 * @author ma.guangming
 *
 */
public class UserInfo implements Serializable  {
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
	/**删除标识（1：删除，0：未删除）*/
	private int delFlag;
	/**密码有效期*/
	private int duration;
	/**密码错误次数*/
	private int pwdErrCnt;
	/**机构名称*/
	private String orgName;
	/**机构名称序列*/
	private String orgNameSeq;
	/**创建时间*/
	private String createTime; 
	/**关系类型（1：行政归属(1)，2：业务归属(多)）*/
	private int type; 
	
	/**用户账号状态*/
	private String status;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 获取创建时间
	 * @return 创建时间
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * 设置机构名称
	 * @return createTime 机构名称
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取用户id
	 * @return 用户id
	 */
	public long getId() {
		return id;
	}
	/**
	 * 获取机构名称
	 * @return 机构名称
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * 设置机构名称
	 * @param orgName 机构名称
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * 设置用户id
	 * @param id 用户id
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
	 * 获取用户姓名
	 * @return 用户姓名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置用户姓名
	 * @param userName 用户姓名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * 获取删除标识（1：删除，0：未删除）
	 * @return 删除标识（1：删除，0：未删除）
	 */
	public int getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置删除标识（1：删除，0：未删除）
	 * @param state 删除标识（1：删除，0：未删除）
	 */
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
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
	 * 获取机构名称序列
	 * @return 机构名称序列
	 */
	public String getOrgNameSeq() {
		return orgNameSeq;
	}
	/**
	 * 设置机构名称序列
	 * @param orgNameSeq 机构名称序列
	 */
	public void setOrgNameSeq(String orgNameSeq) {
		this.orgNameSeq = orgNameSeq;
	}
	/**
	 * 获取登录号
	 * @param loginId 登录号
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
