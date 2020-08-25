package usi.dbdp.uic.base.dao;

import java.util.List;

import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleInfo;
import usi.dbdp.uic.dto.RoleUser;
import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.dto.UserInfo4Session;
import usi.dbdp.uic.entity.Menu;
import usi.dbdp.uic.entity.Role;
import usi.dbdp.uic.entity.User;

/**
 * @author zhqnie
 * @version 2015年9月6日 上午10:33:06
 * 说明
 */
public interface RoleDao {

	/**
	 * 给某应用增加一个角色
	 * @param appCode
	 * @param role
	 * @return
	 */
	public int addRole(Role role);

	/**
	 * 更新某应用角色
	 * @param appCode
	 * @param role
	 * @return
	 */
	public int updateRole(Role role);

	/**
	 * 查询某应用某角色信息
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public List<Role> queryRoleById(String appCode, long roleId);

	/**
	 * 角色与菜单之间的关系删除
	 * @param roleId
	 */
	public boolean deleteRoleMenus(long roleId);

	/**
	 *角色与用户之间的关系删除
	 * @param roleId 角色id
	 */
	public void deleteRoleUsers(long roleId);

	/**
	 *删除某应用角色（逻辑删除，设置角色状态为失效）
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public int deleteRoleById(String appCode, long roleId);

	/**
	 * 激活某应用角色（将角色从失效状态改为生效）
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public int activateRoleById(String appCode, long roleId);

	/**
	 * 获取某应用的所有角色 分页
	 * @param appCode
	 * @return
	 */
	public List<Role> getAllRolesByAppCodePage(String appCode,
			PageObj pageObj);

	/**
	 * 获取某应用的所有角色
	 * @param appCode
	 * @return
	 */
	public List<Role> getAllRolesByAppCode(String appCode);



	/**
	 * @author ma.guangming
	 * 获取某应用下某角色名称(模糊查询)的角色列表
	 * @param appCode 应用code
	 * @param roleName 角色名称
	 * @param roleType 角色类型（0：系统角色，1：应用角色）默认0 
	 * @param pageObj 分页对象
	 * @return 角色列表
	 */
	public List<Role> getRolesByAppCodeAndRoleNameWithPage(
			String appCode, String roleName,Long province, int roleType, PageObj pageObj);

	/**
	 * @author ma.guangming
	 * 获取某应用下某角色拥有的所有菜单
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 菜单列表
	 */
	public List<Menu> getMenusByRoleIdAndAppCode(String appCode,
			long roleId);


	

	/**
	 * @author ma.guangming
	 * 批量添加角色下的成员
	 * @param roleUser
	 */
	public boolean batchAddUsersIntoRole(RoleUser roleUser);

	/**
	 * @author ma.guangming
	 * 批量删除角色下的成员
	 * @param roleUser
	 */
	public boolean batchDeleteUsersFromRole(RoleUser roleUser);

	/**
	 * 根据应用code和角色类型查询用户列表
	 * @param appCode应用code
	 * @param roleType角色类型
	 * @return
	 */
	public List<Role> queryByAppCodeAndRoleType(String appCode,
			int roleType);

	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用编码
	 * @param roleCode 角色编码
	 * @return
	 */
	public List<RoleInfo> queryRoleByCode(String appCode,
			String roleCode);

	/**
	 * @author ma.guangming
	 * 依据登录帐号和用户姓名获取某应用下的某角色的用户列表（分页）
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @param user 用户对象  
	 * @param pageObj 分页对象
	 * @return 某应用下的某角色的用户列表
	 */
	public List<UserInfo4Session> getUsersByAppCodeAndRoleIdAndUserIdAndUserNameWithPage2(
			String appCode, User user, long roleId, PageObj pageObj);

	/**
	 * 查询某应用某角色信息
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public List<RoleInfo> queryRoleById2(String appCode, long roleId);

	/**
	 * @author ma.guangming
	 * 根据角色ID查询出的未添加进该角色的员工,用于选择添加
	 * @param adminOrgId 当前登录人的机构id
	 * @param orgId 查询条件的机构id
	 * @param adminId 当前登录人的账号
	 * @param roleId 角色id
	 * @param userId 被查询员工的的登录帐号
	 * @param userName 被查询员工的用户名称
	 * @param orgName 被查询员工的机构名称
	 * @param pageObj 分页对象
	 * @return 未添加进该角色的员工
	 */
	public List<UserInfo> getAllOtherUsersWithoutRoleByPage2(
			Long adminOrgId, Long orgId, String adminId, long roleId,
			String userId, String userName, String orgName, PageObj pageObj);


	/**
	 * 角色与操作权限之间的关系删除
	 * @param roleId
	 */
	public void deleteRoleOpts(long roleId);

}