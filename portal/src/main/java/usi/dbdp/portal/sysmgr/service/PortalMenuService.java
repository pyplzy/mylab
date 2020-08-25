package usi.dbdp.portal.sysmgr.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import usi.dbdp.uic.a2a.service.AuthenticationService;
import usi.dbdp.uic.base.service.DataPriService;
import usi.dbdp.uic.base.service.MenuService;
import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.Menu;

/**
 * 菜单服务层需要的service
 * @author ma.guangming
 *
 */
@Service
public class PortalMenuService {
	
	@Resource
	private DataPriService dataPriService;
	@Resource
	private MenuService  menuService;
	@Resource
	private AuthenticationService authenticationService;
	/**
	 * 查询登录帐号拥有的权限
	 * @param userId 登录帐号
	 * @param privilegeType 权限类型（1：应用数据权限，2：机构数据权限）
	 * @return 数据权限列表
	 */
	public List<DataPri> getDataPriByUserIdAndPrivilegeType(String userId,int privilegeType){
		return dataPriService.getDataPrisByUserIdAndPrivilegeType(userId, privilegeType);
	}
	
	/**
	 * @author ma.guangming
	 * 获取某应用所有激活的菜单
	 * @param appCode
	 * @return 菜单列表
	 */
	public List<Menu> getAllMenusByAppCode(String appCode){
		return menuService.getAllMenusByAppCode(appCode);
	}
	
	/**
	 * 删除角色的菜单权限 
	 * @param menuId 菜单id
	 * @return true 成功  false 失败
	 */
	public boolean deleteMenuRole(long menuId){
		return authenticationService.deleteMenuRole(menuId);
	}
	
	/**
	 * 删除某应用菜单（逻辑删除，设置菜单状态为失效）
	 * @param appCode
	 * @param menuId
	 * @return
	 */
	public boolean deleteMenuById(String appCode, long menuId){
		return menuService.deleteMenuById(appCode, menuId);
	}
	
	/**
	 * 给某应用增加一个菜单
	 * @param appCode 应用code
	 * @param menu 菜单对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addMenu(String appCode, Menu menu){
		return menuService.addMenu(appCode, menu);
	}
	
}
