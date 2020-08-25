package usi.dbdp.uic.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 用户角色数据传输对象实体
 * @author lmwang
 * 创建时间：2015-2-6 上午10:03:10
 */
public class UserRoleRel implements Serializable{
	
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	
	/**用户id*/
	private long id;
	
	/**角色id列表*/
	private List<Long> roleIds;

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
	 * 获取角色id列表
	 * @return 角色id列表
	 */
	public List<Long> getRoleIds() {
		return roleIds;
	}

	/**
	 * 设置角色id列表
	 * @param roleIds 角色id列表
	 */
	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}
	
}
