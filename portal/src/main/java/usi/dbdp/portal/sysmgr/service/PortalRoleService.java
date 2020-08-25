package usi.dbdp.portal.sysmgr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.dto.OptDto;
import usi.dbdp.portal.dto.OrgDto;
import usi.dbdp.portal.sysmgr.dao.RoleManageDao;
import usi.dbdp.uic.a2a.service.AuthenticationService;
import usi.dbdp.uic.base.service.DataPriService;
import usi.dbdp.uic.base.service.MenuService;
import usi.dbdp.uic.base.service.RoleService;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleInfo;
import usi.dbdp.uic.dto.RoleMenuRel;
import usi.dbdp.uic.dto.RoleUser;
import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.Menu;
import usi.dbdp.uic.entity.Role;
import usi.dbdp.uic.entity.User;

import com.alibaba.fastjson.JSONObject;
@Service
public class PortalRoleService {
	
	@Resource
	private RoleService roleService;
	 
	@Resource
	private DataPriService dataPriService;
	
	@Resource
	private  MenuService menuService;
	
	@Resource
	private  AuthenticationService authenticationService;
	
	@Resource
	private RoleManageDao roleManageDao;
	
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
	* 获取某应用下某角色名称(模糊查询)的角色列表
	 * @param appCode 应用code
	 * @param roleName 角色名称
	 * @param roleType 角色类型（0：系统角色，1：应用角色）默认0 
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj , "roles" :roles } :pageObj是 分页对象 roles是角色列表
	 */
	public Map<String , Object> getRolesByAppCodeAndRoleNameWithPage(String appCode,
			String roleName ,Long province,int roleType ,  PageObj pageObj) {
		return roleService.getRolesByAppCodeAndRoleNameWithPage(appCode, roleName,province, roleType, pageObj);
	}
	
	/**
	 * 给某应用增加一个角色
	 * @param appCode 应用code
	 * @param role 角色对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Transactional(rollbackFor=Exception.class) 
	public boolean addRole(Role role){
		return roleService.addRole( role);
	}
	
	/**
	 * 更新某应用角色
	 * @param appCode 应用code
	 * @param role 角色对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Transactional(rollbackFor=Exception.class) 
	public boolean updateRole(Role role){
		return roleService.updateRole( role);
	}
	
	/**
	 * 删除某应用角色（逻辑删除，设置角色状态为失效） 并将角色与菜单之间的关系解除、角色与用户之间的关系解除
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return true表示操作成功，false表示操作失败
	 */
	@Transactional(rollbackFor=Exception.class) 
	public boolean deleteRoleById(String appCode,long roleId){
		return roleService.deleteRoleById(appCode, roleId);
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
	 * @author ma.guangming
	 * 获取某应用下某角色拥有的所有菜单
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 菜单列表
	 */
	public List<Menu> getMenusByRoleIdAndAppCode(String appCode,long roleId){
		return roleService.getMenusByRoleIdAndAppCode(appCode, roleId);
	}
	
	/**
	 * 角色与菜单之间的关系删除
	 * @param roleId 角色id
	 */
	@Transactional(rollbackFor=Exception.class) 
	public boolean deleteRoleMenus(long roleId){
		return roleService.deleteRoleMenus(roleId);
	}
	
	/**
	 * 给角色赋予多个菜单
	 * @param roleMenuRel 角色菜单传输对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Transactional(rollbackFor=Exception.class) 
	public boolean grantMenus2Role(RoleMenuRel roleMenuRel) {
		return authenticationService.grantMenus2Role(roleMenuRel);
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
	 * @return 未添加进该角色的员工
	 */
	public Map<String , Object> 	getAllOtherUsersWithoutRoleByPage2
		(Long adminOrgId,Long orgId  , String adminId , long roleId,String userId , String userName , String orgName  , PageObj pageObj){
		return roleService.getAllOtherUsersWithoutRoleByPage2(adminOrgId, orgId, adminId, roleId, userId, userName, orgName, pageObj);
	}
	
	/**
	 * @author ma.guangming
	 * 批量添加角色下的成员
	 * @param roleUser
	 */
	@Transactional(rollbackFor=Exception.class) 
	public boolean batchAddUsersIntoRole(final RoleUser roleUser){
		return roleService.batchAddUsersIntoRole(roleUser);
	}
	
	/**
	 * @author ma.guangming
	 * 批量删除角色下的成员
	 * @param roleUser
	 */
	@Transactional(rollbackFor=Exception.class) 
	public boolean batchDeleteUsersFromRole(final RoleUser roleUser){
		return roleService.batchDeleteUsersFromRole(roleUser);
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用code
	 * @param roleCode 角色编码
	 * @return 角色对象Role
	 */
	public RoleInfo queryRoleByCode(String appCode, String roleCode) {
		return roleService.queryRoleByCode(appCode, roleCode);
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 角色对象Role
	 */
	public Role queryRoleById(String appCode, long roleId) {
		return roleService.queryRoleById(appCode, roleId);
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public RoleInfo queryRoleById2(String appCode, long roleId){
		return roleService.queryRoleById2(appCode, roleId);
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
	public Map<String , Object> getUsersByAppCodeAndRoleIdAndUserIdAndUserNameWithPage(
			String appCode, User user , long roleId, PageObj pageObj) {
			return roleService.
					getUsersByAppCodeAndRoleIdAndUserIdAndUserNameWithPage2(appCode, user, roleId, pageObj);
	}

	/**
	 * 获取省机构下拉列表 
	 * @param province 当前登录人省份orgId 0表示集团
	 * @return
	 */
	public List<OrgDto> getProvinces(Long province) {
		return roleManageDao.getProvinces(province);
	}

	public Map<String, Object> qryOptsByRoleId(long roleId, long resourceId,String optName,String appCode,PageObj pageObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<OptDto> optList= roleManageDao.qryOptsByRoleId(roleId,resourceId,optName,appCode,pageObj);
		map.put("pageObj", pageObj);
		map.put("optList",optList);
		return map;
	}

	@Transactional(rollbackFor=Exception.class) 
	public void delRoleOpt(long roleId, long optId) {
		roleManageDao.delRoleOpt(roleId,optId);
		
	}
	@Transactional(rollbackFor=Exception.class) 
	public void giveRoleOpt(long roleId, long optId) {
		roleManageDao.giveRoleOpt(roleId,optId);
	}

	public JSONObject qryOptListById(long staffId) {
		List<OptDto> list = roleManageDao.qryOptListById(staffId);
		JSONObject obj = new JSONObject();
		if(list.size()>0){
			for(OptDto op:list){
				if("org_add".equals(op.getPrivilegeCode())){
					if(1==op.getIsGranted()){
						obj.put("org_add", "1");
					}else{
						obj.put("org_add", "0");
					}
				}
				if("org_edit".equals(op.getPrivilegeCode())){
					if(1==op.getIsGranted()){
						obj.put("org_edit", "1");
					}else{
						obj.put("org_edit", "0");
					}
				}
				if("org_delete".equals(op.getPrivilegeCode())){
					if(1==op.getIsGranted()){
						obj.put("org_delete", "1");
					}else{
						obj.put("org_delete", "0");
					}
				}
			}
		}else{
			obj.put("org_add", "0");
			obj.put("org_delete", "0");
			obj.put("org_edit", "0");
		}
		return obj;
	}
	
	
}
