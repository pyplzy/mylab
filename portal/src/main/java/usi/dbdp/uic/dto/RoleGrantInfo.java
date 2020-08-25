package usi.dbdp.uic.dto;

import java.io.Serializable;

/**
 * 角色授权信息dto
 * @author ma.guangming
 *
 */
public class RoleGrantInfo implements Serializable  {
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	private long roleId; //角色ID
	private String appCode;//所属应用
	private String roleName; //角色名称
	private String roleCode; //角色编码
	private String roleDesc; //角色说明
	private Integer isGranted; //是否授予 0已授予 1未授予
	private long id; //员工ID
	private int roleType; //角色类型
	private long orgId;//省份
	private String orgName;//省份名称
	
	/**
	 * 获取省份名称
	 * @return orgName 省份名称
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
	 * 获取省份id
	 * @return orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * 设置省份id
	 * @param orgId 省份id
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * 获取角色类型
	 * @return 角色类型
	 */
	public int getRoleType() {
		return roleType;
	}
	/**
	 * 设置角色类型
	 * @param roleType 角色类型
	 */
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
	/**
	 * 获取角色ID
	 * @return 角色ID
	 */
	public long getRoleId() {
		return roleId;
	}
	/**
	 * 设置角色ID
	 * @param roleId 角色ID
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	/**
	 * 获取应用code
	 * @return 应用code
	 */
	public String getAppCode() {
		return appCode;
	}
	/**
	 * 设置应用code
	 * @param appCode 应用code
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
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
	 * 获取角色code
	 * @return 角色code
	 */
	public String getRoleCode() {
		return roleCode;
	}
	/**
	 * 设置角色code
	 * @param roleCode 角色code
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	/**
	 * 获取角色说明
	 * @return 角色说明
	 */
	public String getRoleDesc() {
		return roleDesc;
	}
	/**
	 * 设置角色说明
	 * @param roleDesc 角色说明
	 */
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	/**
	 * 获取是否授权 0已授予 1未授予
	 * @return 是否授权 0已授予 1未授予
	 */
	public Integer getIsGranted() {
		return isGranted;
	}
	/**
	 * 设置是否授权 0已授予 1未授予
	 * @param isGranted 是否授权 0已授予 1未授予
	 */
	public void setIsGranted(Integer isGranted) {
		this.isGranted = isGranted;
	}
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
}
