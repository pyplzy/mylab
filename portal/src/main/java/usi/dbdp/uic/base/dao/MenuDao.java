package usi.dbdp.uic.base.dao;

import java.util.List;

import usi.dbdp.uic.entity.Menu;

/**
 * @author zhqnie
 * @version 2015年9月6日 上午10:32:40
 * 说明
 */
public interface MenuDao {

	/**
	 * 给某应用增加一个菜单
	 * @param appCode
	 * @param menu
	 * @return
	 */
	public Long insertMenu(String appCode, Menu menu);

	/**
	 * 更新某应用菜单
	 * @param appCode
	 * @param menu
	 * @return
	 */
	public int updateMenu(String appCode, Menu menu);

	/**
	 * 某应用根据菜单id查菜单信息
	 * @param appCode
	 * @param menuId
	 * @return
	 */
	public List<Menu> queryMenu(String appCode, long menuId);

	/**
	 * 删除某应用菜单（逻辑删除，设置菜单状态为失效）
	 * @param appCode
	 * @param menuId
	 * @return
	 */
	public int deleteMenuById(String appCode, long menuId);


	/**
	 * 获取菜单的直接子菜单列表
	 * @param pMenuId
	 * @return
	 */
	public List<Menu> getDirectMenusBypMenuId(long pMenuId);

	/**
	 * @author ma.guangming
	 * 获取某应用所有激活的菜单
	 * @param appCode
	 * @return 菜单列表
	 */
	public List<Menu> getAllMenusByAppCode(String appCode);

	/**
	 * 根据菜单id删除角色菜单关联表
	 * @param menuId 菜单id
	 * @return
	 */
	public int deleteMenuRoleRel(long menuId);

}