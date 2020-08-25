package usi.dbdp.uic.entity;

import java.io.Serializable;
/**
 * 数据权限
 * @author ma.guangming
 *
 */
public class DataPri  implements Serializable  {
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	/**  登录帐号*/
	private String userId;
	/**权限值（appcode、orgid等）*/
	private String privilegeValue;
	/**权限类型（1：应用数据权限，2：机构数据权限）*/
	private int privilegeType;
	/**备注*/
	private String privilegeMemo;
	/**
	 * 获取登录帐号
	 * @return 登录帐号
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置登录帐号
	 * @param userId 登录帐号
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取 权限值（appcode、orgid等）
	 * @return 权限值（appcode、orgid等）
	 */
	public String getPrivilegeValue() {
		return privilegeValue;
	}
	/**
	 * 设置 权限值（appcode、orgid等）
	 * @param privilegeValue 权限值（appcode、orgid等）
	 */
	public void setPrivilegeValue(String privilegeValue) {
		this.privilegeValue = privilegeValue;
	}
	/**
	 * 获取 权限类型（1：应用数据权限，2：机构数据权限）
	 * @return 权限类型（1：应用数据权限，2：机构数据权限）
	 */
	public int getPrivilegeType() {
		return privilegeType;
	}
	/**
	 * 设置 权限类型（1：应用数据权限，2：机构数据权限）
	 * @param privilegeType 权限类型（1：应用数据权限，2：机构数据权限）
	 */
	public void setPrivilegeType(int privilegeType) {
		this.privilegeType = privilegeType;
	}
	/**
	 * 获取备注
	 * @return 备注
	 */
	public String getPrivilegeMemo() {
		return privilegeMemo;
	}
	/**
	 * 设置备注
	 * @param privilegeMemo 备注
	 */
	public void setPrivilegeMemo(String privilegeMemo) {
		this.privilegeMemo = privilegeMemo;
	}
}
