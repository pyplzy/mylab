package usi.dbdp.portal.sysmgr.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import usi.dbdp.uic.a2a.service.AccountingService;
import usi.dbdp.uic.a2a.service.AuthenticationService;
import usi.dbdp.uic.base.service.AppRegisterService;
import usi.dbdp.uic.base.service.OrgService;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleGrantInfo;
import usi.dbdp.uic.dto.UserRoleRel;
import usi.dbdp.uic.entity.AppRegister;
import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.entity.User;

/**
 * 机构人员服务层所需的service
 * @author ma.guangming
 *
 */
@Service
public class PortalOrgService {
	
	@Resource
	private OrgService orgService;
	
	@Resource
	private AccountingService accountingService;
	
	@Resource
	private AppRegisterService appRegisterService;
	
	@Resource
	private AuthenticationService authenticationService;
	
	/**
	 * 增加一个机构
	 * @param org 机构对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addOrg(Org org){
		return orgService.addOrg(org);
	}
	
	/**
	 * 删除机构（逻辑删除，设置机构状态为失效） 是否需要级联删除用户？？？
	 * @param orgId 机构id
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean deleteOrgById(long orgId){
		return orgService.deleteOrgById(orgId);
	}
	
	
	/**
	 * 根据登录名获取用户信息
	 * @param userId 登录名
	 * @return 用户信息模型
	 */
	public User getUserByUserId(String userId){
		return accountingService.getUserByUserId(userId);
	}
	
	/**
	 * 更新用户
	 * @param user 用户信息模型
	 */
	public boolean updateUser( User user){
		return accountingService.updateUser(user);
	}
	
	
	/**
	 * 查询应用级应用
	 * @return List<AppRegister>
	 * @author  ma.guangming
	 */
	public List<AppRegister> getNomalAppsByState(){
		return appRegisterService.getNomalAppsByState();
	}
	
	/**
	 * @author ma.guangming
	 * 依据应用code, 用户id, 角色code, 角色名称, 是否授权查询应用级角色列表
	 * @param pageObj 分页对象
	 * @param roleGrantInfo 角色授权dto:应用code, 用户id, 角色code, 角色名称
	 * @return {"pageObj":pageObj , "roleGrantInfos" :roleGrantInfos } :pageObj是 分页对象 roleGrantInfos是角色授权dto列表
	 */
	public Map<String , Object > getRoleGrantInfosByIdAndAppCodeAndRoleCodeAndRoleNameAndIsGrantedWithPage(
			PageObj pageObj, RoleGrantInfo roleGrantInfo) {
		return authenticationService.
				getRoleGrantInfos
				(pageObj, roleGrantInfo);
	}
	
	/**
	 * 取消授权
	 * @param roleMenuRel 用户角色数据传输对象实体
	 */
	public boolean delUserRolesRel(UserRoleRel userRoleRel) {
		return authenticationService.delUserRolesRel(userRoleRel);
	}
	
	/**
	 * 查询某机构信息
	 * @param orgId 机构id
	 * @return 机构对象 Org
	 */
	public Org queryOrgById(long orgId) {
		return orgService.queryOrgById(orgId);
	}
	
	/**
	 * 更新一个机构
	 * @param org 机构对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateOrg(Org org){
		return orgService.updateOrg(org);
	}
	
	/**
	 * 增加用户
	 * @param user 用户信息模型 
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addUser(User user) {
		return accountingService.addUser(user);
	}
	
	
	
}
