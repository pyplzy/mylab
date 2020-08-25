package usi.dbdp.uic.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 角色菜单数据传输对象实体
 * @author lmwang
 * 创建时间：2015-2-6 上午9:46:28
 */
public class RoleMenuRel implements Serializable{
	
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;

	/**角色id*/
	private long roleId;
	
	/**菜单id列表*/
	private List<Long> menuIds;

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
	 * 获取菜单id列表
	 * @return 菜单id列表
	 */
	public List<Long> getMenuIds() {
		return menuIds;
	}

	/**
	 * 设置菜单id列表
	 * @param menuIds 菜单id列表
	 */
	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}
	
}
