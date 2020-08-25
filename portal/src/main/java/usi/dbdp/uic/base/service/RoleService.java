package usi.dbdp.uic.base.service;

import java.util.List;
import java.util.Map;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleInfo;
import usi.dbdp.uic.dto.RoleUser;
import usi.dbdp.uic.entity.Menu;
import usi.dbdp.uic.entity.Role;
import usi.dbdp.uic.entity.User;

/**
 * 角色服务接口
 * @author lmwang
 * 创建时间：2015-2-6 上午11:30:43
 */
public interface RoleService {

	/**
	 * 给某应用增加一个角色
	 * @param appCode 应用code
	 * @param role 角色对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addRole(Role role);
	
	/**
	 * 更新某应用角色
	 * @param appCode 应用code
	 * @param role 角色对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateRole(Role role);
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 角色对象Role
	 */
	public Role queryRoleById(String appCode,long roleId);
	
	/**
	 * 删除某应用角色（逻辑删除，设置角色状态为失效） 并将角色与菜单之间的关系解除、角色与用户之间的关系解除
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean deleteRoleById(String appCode,long roleId);
	
	/**
	 * 激活某应用角色（将角色从失效状态改为生效）
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean activateRoleById(String appCode,long roleId);
	
	/**
	 * 获取某应用的所有角色
	 * @param appCode 应用code
	 * @return 角色列表
	 */
	public Map<String, Object> getAllRolesByAppCodePage(String appCode, PageObj pageObj);
	
	/**
	 * 获取某应用的所有角色
	 * @param appCode 应用code
	 * @return 角色列表
	 */
	public List<Role> getAllRolesByAppCode(String appCode);
	
	
	/**
	 * 获取某应用下特定角色名称(模糊查询)的角色列表（分页）
	 * @param appCode 应用code
	 * @param roleName 角色名称
	 * @param province 省份id
	 * @param roleType 角色类型（0：系统角色，1：应用角色）默认0 
	 * @return {"pageObj":pageObj , "roles" :roles } :pageObj是 分页对象 roles是角色列表
	 */
	public Map<String , Object> getRolesByAppCodeAndRoleNameWithPage(String appCode,String roleName ,Long province,int roleType  , PageObj pageObj);
	/**
	 * 获取某应用下某角色拥有的所有菜单
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 菜单列表
	 */
	public List<Menu> getMenusByRoleIdAndAppCode(String appCode,long roleId);
	
	/**
	 * 批量添加角色下的成员
	 * @param roleUser
	 */
	public boolean batchAddUsersIntoRole(final RoleUser roleUser);
	/**
	 * 批量删除角色下的成员
	 * @param roleUser
	 */
	public boolean batchDeleteUsersFromRole(final RoleUser roleUser);
	
	/**
	 * 根据应用code和角色类型查询角色列表
	 * @param appCode 应用编码
	 * @param roleType 角色类型0：系统角色，1：应用角色
	 * @return 角色列表
	 */
	public List<Role> queryByAppCodeAndRoleType(String appCode, int roleType);
	/**
	 * 角色与菜单之间的关系删除
	 * @param roleId 角色id
	 */
	public boolean deleteRoleMenus(long roleId);
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用编码
	 * @param roleCode 角色编码
	 * @return RoleInfo 角色dto
	 */
	public RoleInfo queryRoleByCode(String appCode, String roleCode);
	
	/**
	 * @author ma.guangming
	 * 依据登录帐号和用户姓名获取某应用下的某角色的用户列表（分页）
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @param user 用户对象  
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj,"userInfo4Sessions":userInfo4Sessions}
	 */
	public Map<String, Object> getUsersByAppCodeAndRoleIdAndUserIdAndUserNameWithPage2(String appCode, User user ,long roleId,
			PageObj pageObj);
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用编码
	 * @param roleId 角色主键id
	 * @return 角色dto:添加了应用名称字段
	 */
	public RoleInfo queryRoleById2(String appCode, long roleId);
	
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
	 * @return {"pageObj":pageObj,"userInfos":userInfos}
	 */
	public Map<String, Object> 	getAllOtherUsersWithoutRoleByPage2
		(Long adminOrgId,Long orgId  , String adminId , long roleId,String userId , String userName , String orgName  , PageObj pageObj);
	
}
