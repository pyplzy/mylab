package usi.dbdp.uic.base.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.uic.base.dao.MenuDao;
import usi.dbdp.uic.base.service.MenuService;
import usi.dbdp.uic.entity.Menu;

/**
 * 菜单服务
 * @author nie.zhengqian
 * 创建时间：2015年3月3日 下午6:18:17
 */
@Service
public class MenuServiceImpl implements MenuService {
	@Resource
	private MenuDao menuDao;
	
	/**
	 * 给某应用增加一个菜单
	 * @param appCode 应用code
	 * @param menu 菜单对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean addMenu(String appCode, Menu menu) {
		return menuDao.insertMenu(appCode, menu)>0;
	}
	/**
	 * 更新某应用菜单
	 * @param appCode 应用code
	 * @param menu 菜单对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateMenu(String appCode, Menu menu) {
		return (menuDao.updateMenu(appCode, menu)>0);
	}
	/**
	 * 某应用根据菜单id查菜单信息
	 * @param appCode 应用code
	 * @param menuId 菜单id
	 * @return 菜单对象
	 */
	@Override
	@Transactional(readOnly=true)
	public Menu queryMenuById(String appCode, long menuId) {
		List<Menu> menu = menuDao.queryMenu(appCode, menuId);
		if(menu.size()>0){
			return menu.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 删除某应用菜单（逻辑删除，设置菜单状态为失效）,先角色和菜单关联表解除（物理删除）
	 * @param appCode 应用code
	 * @param menuId 菜单id
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean deleteMenuById(String appCode, long menuId) {
		
		if(menuDao.deleteMenuById(appCode, menuId)>0){
			//删除角色菜单关联表
			menuDao.deleteMenuRoleRel(menuId);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取菜单的直接子菜单列表
	 * @param pMenuId 父菜单id
	 * @return 直接子菜单列表
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Menu> getDirectMenusBypMenuId(long pMenuId) {
		return menuDao.getDirectMenusBypMenuId(pMenuId);
	}
	/**
	 * @author ma.guangming
	 * 获取某应用所有激活的菜单
	 * @param appCode
	 * @return 菜单列表
	 */
	public List<Menu> getAllMenusByAppCode(String appCode){
		return menuDao.getAllMenusByAppCode(appCode);
	}
}
