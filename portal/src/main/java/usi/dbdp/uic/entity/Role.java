package usi.dbdp.uic.entity;

import java.io.Serializable;

/**
 * 角色实体
 * @author lmwang
 * 创建时间：2015-2-5 下午5:41:34
 */
public class Role implements Serializable {

	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	
	/** 角色ID*/
	private long roleId;
	
	/** 所属应用*/
	private String appCode;
	
	/** 省份id*/
	private long province;
	
	/** 省份名称*/
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
	/**
	 * 获取角色id
	 * @return 角色id
	 */
	public long getRoleId() {
		return roleId;
	}
	/**
	 * 设置角色id
	 * @param roleId 角色id
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取所属应用code
	 * @return 所属应用code
	 */
	public String getAppCode() {
		return appCode;
	}

	/**
	 * 获取省份id
	 * @return 省份id
	 */
	public long getProvince() {
		return province;
	}
	/**
	 *  设置省份id
	 * @param 省份id
	 */
	public void setProvince(long province) {
		this.province = province;
	}
	/**
	 * 设置所属应用code
	 * @param appCode 所属应用code
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * 获取角色编码
	 * @return 角色编码
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * 设置角色编码
	 * @param roleCode 角色编码
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	/**
	 * 获取角色名称
	 * @return 角色名称
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * 设置角色名称
	 * @param roleName 角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 获取角色备注
	 * @return 角色备注
	 */
	public String getRoleMemo() {
		return roleMemo;
	}

	/**
	 * 设置角色备注
	 * @param roleMemo 角色备注
	 */
	public void setRoleMemo(String roleMemo) {
		this.roleMemo = roleMemo;
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
	 * @param delFlag 删除标识（1：删除，0：未删除）
	 */
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	
	
	/**
	 * 获取角色类型（0：系统角色，1：应用角色）默认0
	 * @return 角色类型（0：系统角色，1：应用角色）默认0
	 */
	public int getRoleType() {
		return roleType;
	}

	/**
	 * 设置角色类型（0：系统角色，1：应用角色）默认0
	 * @param roleType 角色类型（0：系统角色，1：应用角色）默认0
	 */
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
	
}
