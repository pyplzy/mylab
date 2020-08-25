package usi.dbdp.uic.base.service;

import java.util.List;

import usi.dbdp.uic.entity.Menu;

/**
 * 菜单服务接口
 * @author lmwang
 * 创建时间：2015-2-6 上午11:55:58
 */
public interface MenuService {

	/**
	 * 给某应用增加一个菜单
	 * @param appCode 应用code
	 * @param menu 菜单对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addMenu(String appCode,Menu menu);
	
	/**
	 * 更新某应用菜单
	 * @param appCode 应用code
	 * @param menu 菜单对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateMenu(String appCode,Menu menu);
	
	/**
	 * 某应用根据菜单id查菜单信息
	 * @param appCode 应用code
	 * @param menuId 菜单id
	 * @return 菜单对象
	 */
	public Menu queryMenuById(String appCode,long menuId);
	
	/**
	 * 删除某应用菜单（逻辑删除，设置菜单状态为失效）
	 * @param appCode 应用code
	 * @param menuId 菜单id
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean deleteMenuById(String appCode,long menuId);
	
	
	/**
	 * 获取菜单的直接子菜单列表
	 * @param pMenuId 父菜单id
	 * @return 直接子菜单列表
	 */
	public List<Menu> getDirectMenusBypMenuId(long pMenuId);
	/**
	 * 获取某应用所有激活的菜单
	 * @param appCode
	 * @return 菜单列表
	 */
	public List<Menu> getAllMenusByAppCode(String appCode);
	
}
