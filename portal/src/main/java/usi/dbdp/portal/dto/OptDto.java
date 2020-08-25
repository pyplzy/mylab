package usi.dbdp.portal.dto;
/**
 * @author zhqnie
 * @version 2016年12月29日 下午3:56:49
 * 说明
 */
public class OptDto {
	
	private long privilegeId;
	private long resourceId;
	private String privilegeCode;
	private String privilegeName;
	private int delFlag;
	private String resourceName;
	private int isGranted;  //是否赋予权限 1是 0否
	
	public int getIsGranted() {
		return isGranted;
	}
	public void setIsGranted(int isGranted) {
		this.isGranted = isGranted;
	}
	public long getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(long privilegeId) {
		this.privilegeId = privilegeId;
	}
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public String getPrivilegeCode() {
		return privilegeCode;
	}
	public void setPrivilegeCode(String privilegeCode) {
		this.privilegeCode = privilegeCode;
	}
	public String getPrivilegeName() {
		return privilegeName;
	}
	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
}
