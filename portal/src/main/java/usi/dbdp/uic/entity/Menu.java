package usi.dbdp.uic.entity;

import java.io.Serializable;

/**
 * 菜单实体
 * @author lmwang
 * 创建时间：2015-2-5 下午5:29:47
 */
public class Menu implements Serializable {

	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	
	/** 菜单ID*/
	private long menuId;
	
	/** 所属应用*/
	private String appCode;
	
	/** 菜单名称*/
	private String menuName;
	
	/** 菜单级别（1：一级，2：二级，...）*/
	private int menuLevel;
	
	/** 菜单动作*/
	private String menuAction;
	
	/** 父菜单ID*/
	private long pMenuId;
	
	/**显示顺序*/
	private int displayOrder;
	
	/**删除标识（1：删除，0：未删除）*/
	private int delFlag;
	
	/**菜单备注*/
	private String menuMemo;
	
	/**菜单序列*/
	private String menuSeq;
	
	/**是否叶子节点（1：是，0：否）*/
	private int isLeaf;
	
	/**父亲节点（true：是，false：否）*/
	private String isParent;
	
	/**
	 * 获取父亲节点（true：是，false：否）
	 * @return 父亲节点（true：是，false：否）
	 */
	public String getIsParent() {
		return isParent;
	}
	/**
	 * 设置父亲节点（true：是，false：否）
	 * @param isParent 是否父亲节点（true：是，false：否）
	 */
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

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
	 * 获取所属应用code
	 * @return 所属应用code
	 */
	public String getAppCode() {
		return appCode;
	}

	/**
	 * 设置所属应用code
	 * @param appCode 所属应用code
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
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
	 * 获取菜单级别（1：一级，2：二级，...）
	 * @return 菜单级别（1：一级，2：二级，...）
	 */
	public int getMenuLevel() {
		return menuLevel;
	}

	/**
	 * 设置菜单级别（1：一级，2：二级，...）
	 * @param menuLevel 菜单级别（1：一级，2：二级，...）
	 */
	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
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
	public long getpMenuId() {
		return pMenuId;
	}

	/**
	 * 设置父菜单id
	 * @param pMenuId 父菜单id
	 */
	public void setpMenuId(long pMenuId) {
		this.pMenuId = pMenuId;
	}

	/**
	 * 获取显示顺序
	 * @return 显示顺序
	 */
	public int getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * 设置显示顺序
	 * @param displayOrder 显示顺序
	 */
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
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
	 * @param delFlag删除标识（1：删除，0：未删除）
	 */
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 获取菜单备注
	 * @return 菜单备注
	 */
	public String getMenuMemo() {
		return menuMemo;
	}

	/**
	 * 设置菜单备注
	 * @param menuMemo 菜单备注
	 */
	public void setMenuMemo(String menuMemo) {
		this.menuMemo = menuMemo;
	}

	/**
	 * 获取菜单序列
	 * @return 菜单序列
	 */
	public String getMenuSeq() {
		return menuSeq;
	}

	/**
	 * 设置菜单序列
	 * @param menuSeq 菜单序列
	 */
	public void setMenuSeq(String menuSeq) {
		this.menuSeq = menuSeq;
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
