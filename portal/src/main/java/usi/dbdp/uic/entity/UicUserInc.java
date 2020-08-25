package usi.dbdp.uic.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: UicUserInc 
 * @Description: 用户增量表实体
 * @author johnDong
 * @date 2017年2月9日
 */
public class UicUserInc implements Serializable{
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
	
	/**增量类型 (A、D、U)*/
	private String incType;
	
	/**操作当前时间*/
	private Date occurTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getPwdLastModTime() {
		return pwdLastModTime;
	}

	public void setPwdLastModTime(Date pwdLastModTime) {
		this.pwdLastModTime = pwdLastModTime;
	}

	public Date getLstErrPwdTime() {
		return lstErrPwdTime;
	}

	public void setLstErrPwdTime(Date lstErrPwdTime) {
		this.lstErrPwdTime = lstErrPwdTime;
	}

	public int getPwdErrCnt() {
		return pwdErrCnt;
	}

	public void setPwdErrCnt(int pwdErrCnt) {
		this.pwdErrCnt = pwdErrCnt;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getIncType() {
		return incType;
	}

	public void setIncType(String incType) {
		this.incType = incType;
	}

	public Date getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}
	
}
