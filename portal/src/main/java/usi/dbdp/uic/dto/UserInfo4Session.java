package usi.dbdp.uic.dto;

import java.io.Serializable;

/**
 * 用的id、userid、userName、gender、mobileNo、userType、orgId
 * orgName、orgIdSeq、orgNameSeq、shardingId、province、city、county
 * @author lmwang
 * 创建时间：2015-4-10 上午8:28:17
 */
public class UserInfo4Session implements Serializable  {

	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	
	/**用户ID*/
	private long id;
	/**工号*/
	private String userId;	
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
	/**机构名称*/
	private String orgName;
	/**机构id序列*/
	private String orgIdSeq;
	/**机构名称序列*/
	private String orgNameSeq;
	
	/**用户分片id，用于数据分片，根据所属机构序列的第二个和静态表关联获得*/
	private int shardingId;
	
	/**省*/
	private long province;
	/**市*/
	private long city;
	/**县*/
	private long county;
	
	/**根机构id*/
	private long rootOgrId;
	/**登录号**/
	private String loginId;
	/**删除标识1删除0未删除**/
	private int delFlag;
	
	/**
	 * 获取用户id
	 * @return 用户id
	 */
	public long getId() {
		return id;
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
	 * 获取机构id序列
	 * @return 机构id序列
	 */
	public String getOrgIdSeq() {
		return orgIdSeq;
	}
	/**
	 * 设置机构id序列
	 * @param orgIdSeq 机构id序列
	 */
	public void setOrgIdSeq(String orgIdSeq) {
		this.orgIdSeq = orgIdSeq;
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
	 * 获取分片id
	 * @return 分片id
	 */
	public int getShardingId() {
		return shardingId;
	}
	/**
	 * 设置分片id
	 * @param shardingId 分片id
	 */	
	public void setShardingId(int shardingId) {
		this.shardingId = shardingId;
	}
	/**
	 * 获取省机构Id
	 * @return 省机构Id
	 */
	public long getProvince() {
		return province;
	}
	/**
	 * 设置省机构Id
	 * @param province 省机构Id
	 */	
	public void setProvince(long province) {
		this.province = province;
	}
	/**
	 * 获取市机构Id
	 * @return 市机构Id
	 */
	public long getCity() {
		return city;
	}
	/**
	 * 设置市机构Id
	 * @param city 市机构Id
	 */	
	public void setCity(long city) {
		this.city = city;
	}
	/**
	 * 获取县机构Id
	 * @return 县机构Id
	 */
	public long getCounty() {
		return county;
	}
	/**
	 * 设置县机构Id
	 * @param county 县机构Id
	 */	
	public void setCounty(long county) {
		this.county = county;
	}
	/**
	 * 获取根机构Id
	 * @return 根机构Id
	 */
	public long getRootOgrId() {
		return rootOgrId;
	}
	/**
	 * 设置根机构Id
	 * @param rootOgrId 根机构Id
	 */	
	public void setRootOgrId(long rootOgrId) {
		this.rootOgrId = rootOgrId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	
}
