package usi.dbdp.uic.dto;

import java.io.Serializable;

/**
 * 角色dto
 *
 */
public class RoleInfo implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	/** 角色ID*/
	private long roleId;
	
	/** 所属应用*/
	private String appCode;
	
	/** 省份*/
	private String orgName;
	
	/** 角色编码*/
	private String roleCode;
	
	/** 角色名称*/
	private String roleName;
	
	/** 角色说明*/
	private String roleMemo;
	
	/**删除标识（1：删除，0：未删除）*/
	private int delFlag;
	
	/**角色类型（0：系统角色，1：应用角色）默认0*/
	private int roleType;
	
	/**应用名称*/
	private String appName;
	
	/**获取角色*/
	public long getRoleId() {
		return roleId;
	}

	/**设置角色id*/
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**获取应用编码*/
	public String getAppCode() {
		return appCode;
	}

	/**设置应用编码*/
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * 获取省份名称
	 * @return
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * 设置省份名称
	 * @param orgName
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**获取角色编码*/
	public String getRoleCode() {
		return roleCode;
	}

	/**设置角色编码*/
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	/**获取角色名称*/
	public String getRoleName() {
		return roleName;
	}

	/**设置角色名称*/
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**获取角色说明*/
	public String getRoleMemo() {
		return roleMemo;
	}

	/**设置角色说明*/
	public void setRoleMemo(String roleMemo) {
		this.roleMemo = roleMemo;
	}

	/**删除标识（1：删除，0：未删除）*/
	public int getDelFlag() {
		return delFlag;
	}

	/**删除标识（1：删除，0：未删除）*/
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	/**设置角色类型：角色类型（0：系统角色，1：应用角色）默认0 */
	public int getRoleType() {
		return roleType;
	}

	/**获取角色类型：角色类型（0：系统角色，1：应用角色）默认0*/
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	/**获取应用名称*/
	public String getAppName() {
		return appName;
	}
	
	/**设置角色名称*/
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	
	
	
	
}
