package usi.dbdp.uic.a2a.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.uic.a2a.dao.AuthenticationDao;
import usi.dbdp.uic.a2a.service.AuthenticationService;
import usi.dbdp.uic.dto.AuthMenu;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleGrantInfo;
import usi.dbdp.uic.dto.RoleMenuRel;
import usi.dbdp.uic.dto.UserRoleRel;
/**
 * 授权服务接口
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	@Resource
	private AuthenticationDao authenticationDao;
	
	/**
	 * 给角色赋予多个菜单
	 * @param roleMenuRel 角色菜单传输对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean grantMenus2Role(RoleMenuRel roleMenuRel) {
		return authenticationDao.grantMenus2Role(roleMenuRel);
	}
	/**
	 * 获取某用户在某应用的所有菜单
	 * @param appCode 应用code
	 * @param id 用户id
	 * @return 某用户在某应用的所有菜单
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AuthMenu> getAllMenusByUserIdInApp(String appCode,long id){
		return authenticationDao.getAllMenusByUserIdInApp(appCode, id);
	}	
	
	/**
	 * @author ma.guangming
	 * 依据应用code, 用户id, 角色code, 角色名称, 是否授权查询应用级角色列表
	 * @param pageObj 分页对象
	 * @param roleGrantInfo 角色授权dto:应用code, 用户id, 角色code, 角色名称
	 * @return {"pageObj":pageObj , "roleGrantInfos" :roleGrantInfos } :pageObj是 分页对象 roleGrantInfos是角色授权dto列表
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String , Object > getRoleGrantInfos(
			PageObj pageObj, RoleGrantInfo roleGrantInfo) {
		Map<String , Object> map=new HashMap<String , Object>();
		map.put("pageObj", pageObj);
		map.put("roleGrantInfos", authenticationDao.getRoleGrantInfos(pageObj, roleGrantInfo));
		return map;
	}
	/**
	 * 取消授权
	 * @param roleMenuRel 用户角色数据传输对象实体
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean delUserRolesRel(UserRoleRel userRoleRel) {
		return authenticationDao.delUserRolesRel(userRoleRel);
	}
	/**
	 * 删除角色的菜单权限 
	 * @param menuId 菜单id
	 * @return true 成功  false 失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean deleteMenuRole(long menuId){
		return authenticationDao.deleteMenuRole(menuId);
	}
	/**
	 * 给用户授权
	 * @param id  用户Id
	 * @param roleId  角色Id
	 * @return true 成功 false 失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean givePermissionByIdAndRoleId(long id, long roleId) {
		return authenticationDao.givePermissionByIdAndRoleId(id, roleId)>0;
	}
	/**
	 * 给用户取消授权
	 * @param id  用户Id
	 * @param roleId  角色Id
	 * @return true 成功 false 失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean delPermissionByIdAndRoleId(long id, long roleId) {
		return (authenticationDao.delPermissionByIdAndRoleId(id, roleId)>0);
	}
}
