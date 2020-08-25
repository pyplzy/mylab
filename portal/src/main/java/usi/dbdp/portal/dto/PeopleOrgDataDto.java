package usi.dbdp.portal.dto;

public class PeopleOrgDataDto {
	private String userName;
	private String orgName;
	private String userId;
	private String isAdd;
	private String orgId;
	private String manageOrgName;
	private String manageOrgId;

	public String getManageOrgName() {
		return manageOrgName;
	}
	public void setManageOrgName(String manageOrgName) {
		this.manageOrgName = manageOrgName;
	}
	public String getManageOrgId() {
		return manageOrgId;
	}
	public void setManageOrgId(String manageOrgId) {
		this.manageOrgId = manageOrgId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIsAdd() {
		return isAdd;
	}
	public void setIsAdd(String  isAdd) {
		this.isAdd = isAdd;
	}


}
