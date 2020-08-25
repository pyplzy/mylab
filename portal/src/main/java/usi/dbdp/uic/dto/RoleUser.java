package usi.dbdp.uic.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 角色成员
 * @author ma.guangming
 *
 */
public class RoleUser implements Serializable  {
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	
	/**角色id*/
	private long roleId;
	/**成员列表*/
	private List<Long> ids ;
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
	 * 获取成员列表
	 * @return 成员列表
	 */
	public List<Long> getIds() {
		return ids;
	}
	/**
	 * 设置成员列表
	 * @param ids 成员列表
	 */
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	
}
