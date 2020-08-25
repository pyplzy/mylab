package usi.dbdp.portal.entity;

/**
 * 通讯录实体类
 * @author lxci
 * 创建时间：2015年3月24日 下午13:00:00
 */

public class PtlAddressBook {

	private Integer addressBookId; // 通讯录主键
	private String name; // 姓名
	private String title; // 职务
	private String administrativeLevel; // 行政级别
	private String deparment; // 部门
	private String email; // 邮箱
	private String gender; // 性别（0：未知，1：男，2：女）
	private String company; // 公司
	private String mobileNo; // 手机号码
	private String fixedLineTel; // 固定电话
	private String userCata; // 类型（1：企业内部、2：合作伙伴、3：客户）
	private String headImg; // 头像
	private String userId; // 用户ID

	public Integer getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(Integer addressBookId) {
		this.addressBookId = addressBookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAdministrativeLevel() {
		return administrativeLevel;
	}

	public void setAdministrativeLevel(String administrativeLevel) {
		this.administrativeLevel = administrativeLevel;
	}

	public String getDeparment() {
		return deparment;
	}

	public void setDeparment(String deparment) {
		this.deparment = deparment;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getFixedLineTel() {
		return fixedLineTel;
	}

	public void setFixedLineTel(String fixedLineTel) {
		this.fixedLineTel = fixedLineTel;
	}

	public String getUserCata() {
		return userCata;
	}

	public void setUserCata(String userCata) {
		this.userCata = userCata;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
