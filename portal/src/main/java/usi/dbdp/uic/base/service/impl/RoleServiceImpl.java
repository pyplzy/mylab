package usi.dbdp.uic.base.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.uic.base.dao.RoleDao;
import usi.dbdp.uic.base.service.RoleService;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleInfo;
import usi.dbdp.uic.dto.RoleUser;
import usi.dbdp.uic.dto.UserInfo4Session;
import usi.dbdp.uic.entity.Menu;
import usi.dbdp.uic.entity.Role;
import usi.dbdp.uic.entity.User;

/**
 * 角色服务
 * @author nie.zhengqian
 * 创建时间：2015年3月3日 下午6:25:47
 */
@Service
public class RoleServiceImpl implements RoleService{
	@Resource
	private RoleDao roleDao;
	
	/**
	 * 给某应用增加一个角色
	 * @param appCode 应用code
	 * @param role 角色对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean addRole(Role role) {
		return (roleDao.addRole( role)>0);
	} 
	
	/**
	 * 更新某应用角色
	 * @param appCode 应用code
	 * @param role 角色对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateRole( Role role) {
		return (roleDao.updateRole(role)>0);
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 角色对象Role
	 */
	@Override
	@Transactional(readOnly=true)
	public Role queryRoleById(String appCode, long roleId) {
		List<Role> role = roleDao.queryRoleById(appCode, roleId);
		if(role.size()>0){
			return role.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 删除某应用角色（逻辑删除，设置角色状态为失效） 并将角色与菜单之间的关系解除、角色与用户之间的关系解除
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean deleteRoleById(String appCode, long roleId) {
		int tt = roleDao.deleteRoleById(appCode, roleId);
		//如果删除成功
		if(tt>0){
			//解除role menu关联关系
			roleDao.deleteRoleMenus(roleId);
			//接触role user关联关系
			roleDao.deleteRoleUsers(roleId);
			//解除role opt关联关系
			roleDao.deleteRoleOpts(roleId);
		}
		return tt>0;
	}

	/**
	 * 激活某应用角色（将角色从失效状态改为生效）
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean activateRoleById(String appCode, long roleId) {
		return (roleDao.activateRoleById(appCode, roleId)>0);
	}

	/**
	 * 获取某应用的所有角色 分页
	 * @param appCode 应用code
	 * @return 角色列表
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getAllRolesByAppCodePage(String appCode, PageObj pageObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Role> role = roleDao.getAllRolesByAppCodePage(appCode, pageObj);
		map.put("total", pageObj.getTotal());
		map.put("rows",role);
		return map;
	}
	
	/**
	 * 获取某应用的所有角色
	 * @param appCode 应用code
	 * @return 角色列表
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Role> getAllRolesByAppCode(String appCode) {
		return roleDao.getAllRolesByAppCode(appCode);
	}


	/**
	 * @author ma.guangming
	* 获取某应用下某角色名称(模糊查询)的角色列表
	 * @param appCode 应用code
	 * @param roleName 角色名称
	 * @param roleType 角色类型（0：系统角色，1：应用角色）默认0 
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj , "roles" :roles } :pageObj是 分页对象 roles是角色列表
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String , Object> getRolesByAppCodeAndRoleNameWithPage(String appCode,
			String roleName ,Long province,int roleType ,  PageObj pageObj) {
		Map<String , Object> map=new HashMap<String , Object>();
		List<Role> roles=roleDao.getRolesByAppCodeAndRoleNameWithPage(appCode, roleName ,province, roleType  , pageObj);
		map.put("pageObj", pageObj);
		map.put("roles", roles );
		return map;
	}
	/**
	 * @author ma.guangming
	 * 获取某应用下某角色拥有的所有菜单
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 菜单列表
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Menu> getMenusByRoleIdAndAppCode(String appCode,long roleId){
		return roleDao.getMenusByRoleIdAndAppCode(appCode, roleId);
	}
	
	
	/**
	 * @author ma.guangming
	 * 批量添加角色下的成员
	 * @param roleUser
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean batchAddUsersIntoRole(final RoleUser roleUser){
		boolean flag=false;
		try{
			roleDao.batchAddUsersIntoRole(roleUser);
			flag = true ;
		}catch(DataAccessException e){
			e.printStackTrace();
			throw e;
		}
		return flag;
	}
	/**
	 * @author ma.guangming
	 * 批量删除角色下的成员
	 * @param roleUser
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean batchDeleteUsersFromRole(RoleUser roleUser) {
		boolean flag=false;
		try{
			roleDao.batchDeleteUsersFromRole(roleUser);
			flag = true ;
		}catch(DataAccessException e){
			e.printStackTrace();
			throw e;
		}
		return flag;
	}
	/**
	 * 根据应用code和角色类型查询用户列表
	 * @param appCode应用code
	 * @param roleType角色类型
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Role> queryByAppCodeAndRoleType(String appCode, int roleType) {
		return roleDao.queryByAppCodeAndRoleType(appCode,roleType);
	}
	/**
	 * 角色与菜单之间的关系删除
	 * @param roleId 角色id
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean deleteRoleMenus(long roleId){
		return roleDao.deleteRoleMenus(roleId);
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用编码
	 * @param roleCode 角色编码
	 * @return RoleInfo 角色dto
	 */
	public RoleInfo queryRoleByCode(String appCode, String roleCode){
		List<RoleInfo> role = roleDao.queryRoleByCode(appCode, roleCode);
		if(role.size()>0){
			return role.get(0);
		}else{
			return null;
		}
	}
	
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
			PageObj pageObj){
		Map<String, Object> map = new HashMap<String, Object>();
		List<UserInfo4Session> userInfo4Sessions= roleDao.getUsersByAppCodeAndRoleIdAndUserIdAndUserNameWithPage2(appCode, user, roleId, pageObj);
		map.put("pageObj", pageObj);
		map.put("userInfo4Sessions",userInfo4Sessions);
		return map;
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode
	 * @param roleId
	 * @return 角色dto:添加了应用名称字段
	 */
	public RoleInfo queryRoleById2(String appCode, long roleId){
		List<RoleInfo> role = roleDao.queryRoleById2(appCode, roleId);
		if(role.size()>0){
			return role.get(0);
		}else{
			return null;
		}
	}
	
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
		(Long adminOrgId,Long orgId  , String adminId , long roleId,String userId , String userName , String orgName  , PageObj pageObj){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageObj", pageObj);
		map.put("userInfos",roleDao.getAllOtherUsersWithoutRoleByPage2(adminOrgId, orgId, adminId, roleId, userId, userName, orgName, pageObj));
		return map;
	}

	
	
}
