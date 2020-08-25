package usi.dbdp.uic.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户的菜单
 * @author fan.fan
 * 创建时间：2014-4-2 下午8:26:55
 */
public class AuthMenu implements Serializable  {
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	/**菜单id*/
	private long menuId;
	/**菜单名称*/
	private String menuName;
	/**菜单动作*/
	private String menuAction;
	/**父菜单id*/
	private long parentId;
	/**仅菜单框架使用，其他应用可以忽略*/
	private String target = "c_iframe";
	/**是否叶子节点（1：是，0：否）*/
	private int isLeaf;
	
	/**子菜单列表*/
	private List<AuthMenu> children = new ArrayList<AuthMenu>();
	/**
	 * 获取菜单id
	 * @return 菜单id
	 */
	public long getMenuId() {
		return menuId;
	}
	/**
	 * 设置菜单id
	 * @param menuId 菜单id
	 */
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
	/**
	 * 获取菜单名称
	 * @return 菜单名称
	 */
	public String getMenuName() {
		return menuName;
	}
	/**
	 * 设置菜单名称
	 * @param menuName 菜单名称
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	/**
	 * 获取菜单动作
	 * @return 菜单动作
	 */
	public String getMenuAction() {
		return menuAction;
	}
	/**
	 * 设置菜单动作
	 * @param menuAction 菜单动作
	 */
	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}
	/**
	 * 获取父菜单id
	 * @return 父菜单id
	 */
	public long getParentId() {
		return parentId;
	}
	/**
	 * 设置父菜单id
	 * @param parentId 父菜单id
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 仅菜单框架使用，其他应用可以忽略
	 * @return target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * 仅菜单框架使用，其他应用可以忽略
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * 获取子菜单列表
	 * @return 子菜单列表
	 */
	public List<AuthMenu> getChildren() {
		return children;
	}
	/**
	 * 设置子菜单列表
	 * @param children 子菜单列表
	 */
	public void setChildren(List<AuthMenu> children) {
		this.children = children;
	}
	/**
	 * 获取是否叶子节点（1：是，0：否）
	 * @return 是否叶子节点（1：是，0：否）
	 */
	public int getIsLeaf() {
		return isLeaf;
	}

	/**
	 * 设置机构是否叶子节点（1：是，0：否）
	 * @param isLeaf 是否叶子节点（1：是，0：否）
	 */
	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}
}
