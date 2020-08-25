package usi.dbdp.portal.dto;


/**
 * 
 * @author nie.zhengqian
 * 创建时间：2015年4月23日 下午1:53:49
 */

public class UserDto {
	
	/**用户ID*/
	private long id;
	
	/**登录账号*/
	private String userId;	
	
	/**用户姓名*/
	private String userName;
	
	/**性别（0：未知，1：男，2：女）*/
	private int gender;
	
	/**手机号码*/
	private String mobileNo;
	
	/**用户类型（1：普通用户，2：测试用户，3：运维用户）*/
	private int userType; 	
	
	
	/**删除标识（1：删除，0：未删除）*/
	private int delFlag;
	
	
	/**创建时间 String*/
	private String createDate;
	
	/**机构id*/
	private long orgId;
	
	/**机构名称*/
	private String orgName;
	
	/**登录账号*/
	private String loginId;
	
	
	/**
	 * 获取 机构id
	 * @return orgId机构id
	 */
	public long getOrgId() {
		return orgId;
	}

	/**
	 * 设置机构id
	 * @param orgId机构id
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
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


	public int getDelFlag() {
		return delFlag;
	}


	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	
	
	
}
