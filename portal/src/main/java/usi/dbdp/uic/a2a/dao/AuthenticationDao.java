package usi.dbdp.uic.a2a.dao;

import java.util.List;

import usi.dbdp.uic.dto.AuthMenu;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleGrantInfo;
import usi.dbdp.uic.dto.RoleMenuRel;
import usi.dbdp.uic.dto.UserRoleRel;

/**
 * @author zhqnie
 * @version 2015年9月6日 上午10:02:19
 * 说明
 */
public interface AuthenticationDao {


	/**
	 * 给角色赋予多个菜单
	 * @param roleMenuRel 角色菜单传输对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean grantMenus2Role(RoleMenuRel roleMenuRel);

	/**
	 * 获取某用户在某应用的所有菜单
	 * @param appCode 应用code
	 * @param id 用户id
	 * @return 某用户在某应用的所有菜单
	 */
	public List<AuthMenu> getAllMenusByUserIdInApp(String appCode,
			long id);



	/**
	 * @author ma.guangming
	 * 依据应用code, 用户id, 角色code, 角色名称, 是否授权查询应用级角色列表
	 * @param pageObj 分页对象
	 * @param roleGrantInfo 角色授权dto:应用code, 用户id, 角色code, 角色名称
	 * @return 角色授权dto列表
	 */
	public List<RoleGrantInfo> getRoleGrantInfos(
			PageObj pageObj, RoleGrantInfo roleGrantInfo);

	/**
	 * 取消授权
	 * @param roleMenuRel 用户角色数据传输对象实体
	 */
	public boolean delUserRolesRel(UserRoleRel userRoleRel);

	/**
	 * 删除角色的菜单权限 
	 * @param menuId 菜单id
	 * @return true 成功  false 失败
	 */
	public boolean deleteMenuRole(long menuId);

	/**
	 * 给用户取消授权
	 * @param id  用户Id
	 * @param roleId  角色Id
	 * @return 1 成功 0 失败
	 */
	public int delPermissionByIdAndRoleId(long id, long roleId);

	/**
	 * 给用户授权
	 * @param id  用户Id
	 * @param roleId  角色Id
	 * @return 1 成功 0 失败
	 */
	public int givePermissionByIdAndRoleId(long id, long roleId);

}