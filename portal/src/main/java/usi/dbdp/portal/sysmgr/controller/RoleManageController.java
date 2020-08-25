package usi.dbdp.portal.sysmgr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.dto.OrgDto;
import usi.dbdp.portal.entity.ChangeLog;
import usi.dbdp.portal.sysmgr.service.LogService;
import usi.dbdp.portal.sysmgr.service.PortalRoleService;
import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleInfo;
import usi.dbdp.uic.dto.RoleMenuRel;
import usi.dbdp.uic.dto.RoleUser;
import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.Menu;
import usi.dbdp.uic.entity.Role;
import usi.dbdp.uic.entity.User;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 角色管理 
 * @author ma.guangming
 *
 */
@Controller
@RequestMapping("/roleManage")
public class RoleManageController {
	
	@Resource
	private  PortalRoleService  portalRoleService;
	@Resource
	private LogService logService;
	 
	/**
	 * 前往角色管理页面
	 * @return
	 */
	@RequestMapping(value="/menu_roleMain_toRoleMainPage.do", method = RequestMethod.GET)
	public String toMainPageMenu(HttpSession session,Model model){
		String userId = (String)session.getAttribute("userId");
		Long province = Long.parseLong(session.getAttribute("province").toString());
		String flag="";
		if(userId.equals(ConfigUtil.getValue("sysadmin"))){
			flag="true";//顶级管理员
		}else{
			flag="false";
		}
		model.addAttribute("flag", flag);
		model.addAttribute("appItems", portalRoleService.getDataPriByUserIdAndPrivilegeType(userId, 1));
		model.addAttribute("provinces", portalRoleService.getProvinces(province));
		model.addAttribute("resources", portalRoleService.getProvinces(province));
		return "portal/system/roleManage";
	}
	
	/**
	 * @author ma.guangming
	* 获取某应用下某角色名称(模糊查询)的角色列表
	* @param userId 登录帐号
	 * @param appCode 应用code
	 * @param roleName 角色名称
	 * @param roleType 角色类型（0：系统角色，1：应用角色）默认0 
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj , "roles" :roles } :pageObj是 分页对象 roles是角色列表
	 */
	@RequestMapping(value="/menu_roleMain_getRoles.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> getRolesByAppCodeAndRoleNameWithPage(String userId,String appCode,
			String roleName , Long province, PageObj pageObj) {
		int roleType ;
		if(userId.equals(ConfigUtil.getValue("sysadmin"))){
			roleType=2;
		}else{
			roleType=1;
		}
		return portalRoleService.getRolesByAppCodeAndRoleNameWithPage(appCode, roleName,province, roleType, pageObj);
	}
	
	/**
	 * 查询应用权限 
	 * @return
	 */
	@RequestMapping(value="/menu_roleMain_queryDataPri.do", method = RequestMethod.POST)
	@ResponseBody
	public List<DataPri> queryDataPri(HttpSession session){
		String userId = (String)session.getAttribute("userId");
		return portalRoleService.getDataPriByUserIdAndPrivilegeType(userId, 1);
	}
	
	/**
	 * 查询省份
	 * @return
	 */
	@RequestMapping(value="/menu_roleMain_queryProvinces.do", method = RequestMethod.POST)
	@ResponseBody
	public List<OrgDto> queryProvinces(HttpSession session){
		Long province = Long.parseLong(session.getAttribute("province").toString());
		return portalRoleService.getProvinces(province);
	}
	
	/**
	 * 给某应用增加一个角色
	 * @param appCode 应用code
	 * @param role 角色对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@RequestMapping(value="/menu_roleMain_addRole.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean addRole(HttpSession session,Role role){
		String userId = (String)session.getAttribute("userId");
		if(userId.equals(ConfigUtil.getValue("sysadmin"))){
		}else{
			role.setRoleType(1);
		}
		
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId(userId);
		changeLog.setOptObj("角色表");
		changeLog.setOptType(LogService.OPT_INSERT);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		try {
			changeLog.setOptContent("uic_role:"+JacksonUtil.obj2json(role)+",{\"appCode\":\""+role.getAppCode()+"\"}");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logService.saveChangeLogInfo(changeLog);
		
		return portalRoleService.addRole(role);
	}
	
	/**
	 * 判断roleCode是否可用
	 * @param appCode 应用code
	 * @param roleCode 角色code
	 * @return 
	 */
	@RequestMapping(value="/menu_roleMain_queryRole.do", method = RequestMethod.POST)
	@ResponseBody
	public String queryRoleByCode(String appCode, String  roleCode) {
		return portalRoleService.queryRoleByCode(appCode, roleCode)==null?"false":"true";
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 角色对象Role
	 */
	@RequestMapping(value="/menu_roleMain_queryRoleById.do", method = RequestMethod.POST)
	@ResponseBody
	public RoleInfo queryRoleById(String appCode, long roleId) {
		return portalRoleService.queryRoleById2(appCode, roleId);
	}
	
	/**
	 * 更新某应用角色
	 * @param appCode 应用code
	 * @param role 角色对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@RequestMapping(value="/menu_roleMain_updRole.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateRole(HttpSession session , Role role){
		String userId = (String)session.getAttribute("userId");
		if(userId.equals(ConfigUtil.getValue("sysadmin"))){
		}else{
			role.setRoleType(1);
		}
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId(userId);
		changeLog.setOptObj("角色表");
		changeLog.setOptType(LogService.OPT_UPDATE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		//原角色长什么样子
		Role originalRole = portalRoleService.queryRoleById(role.getAppCode(), role.getRoleId());
		try {
			changeLog.setOptContent("uic_role:原纪录="+JacksonUtil.obj2json(originalRole)+",新纪录="+JacksonUtil.obj2json(role)+",{\"appCode\":\""+role.getAppCode()+"\"}");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logService.saveChangeLogInfo(changeLog);
		
		return portalRoleService.updateRole(role);
	}
	
	/**
	 * 删除某应用角色（逻辑删除，设置角色状态为失效） 并将角色与菜单之间的关系解除、角色与用户之间的关系解除
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return true表示操作成功，false表示操作失败
	 */
	@RequestMapping(value="/menu_roleMain_delRole.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteRoleById(String appCode,long roleId,HttpSession session){
		
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("角色表");
		changeLog.setOptType(LogService.OPT_DELETE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		changeLog.setOptContent("uic_role:UPDATE UIC_ROLE SET DEL_FLAG = 1 WHERE ROLE_ID = ? AND APP_CODE = ? :"+roleId+":"+appCode);
		logService.saveChangeLogInfo(changeLog);
		
		return portalRoleService.deleteRoleById(appCode, roleId)?"succ":"fail";
	}

	/**
	 * @author ma.guangming
	 * 获取某应用下某角色拥有的所有菜单
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 菜单列表
	 */
	@RequestMapping(value="/menu_roleMain_getMenus.do", method = RequestMethod.POST)
	@ResponseBody
	public List<Menu> getMenusByRoleIdAndAppCode(String appCode,long roleId){
		return portalRoleService.getMenusByRoleIdAndAppCode(appCode, roleId);
	}
	
	/**
	 * 给角色赋予多个菜单或者去除菜单
	 * @param str 角色id;菜单id1:菜单id2...  也可能是角色id;这种情况表示删除角色的全部菜单
	 * @return true表示操作成功，false表示操作失败
	 */
	@RequestMapping(value="menu_roleMain_saveMenus.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean grantMenus2Role(String  str) {
		if(str==null || "".equals(str) ){
			return false;
		}
		String[] strs=str.split(";");
		long roleId=Long.parseLong(strs[0]);
		//数组如果长度为1，说明是删除角色的所有菜单
		if(strs.length==1){
			try{
				portalRoleService.deleteRoleMenus(roleId);
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}
		RoleMenuRel rm=new RoleMenuRel();
		rm.setRoleId(roleId);
		String[] menuIds=strs[1].split(":");
		ArrayList<Long> list=new ArrayList<Long>();
		for(String menuId : menuIds){
			list.add(Long.parseLong(menuId));
		}
		rm.setMenuIds(list);
		try{
			portalRoleService.deleteRoleMenus(roleId);
			portalRoleService.grantMenus2Role(rm);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true ;
	}
	
	/**
	 * @author ma.guangming
	 * 依据登录帐号和用户姓名获取某应用下的某角色的用户列表（分页）
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @param user 用户对象  
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj , "users" :users } :pageObj是 分页对象 users是人员列表
	 */
	@RequestMapping(value="/menu_roleMain_getUsers.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> getUsersByAppCodeAndRoleIdAndUserIdAndUserNameWithPage(
			String appCode, User user , long roleId,
			PageObj pageObj) {
		return portalRoleService.getUsersByAppCodeAndRoleIdAndUserIdAndUserNameWithPage(appCode, user, roleId, pageObj);
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
	@RequestMapping(value="/menu_roleMain_getUsers2.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> 	getAllOtherUsersWithoutRoleByPage2
		(Long adminOrgId,Long orgId  , String adminId , long roleId,String userId , String userName , String orgName  , PageObj pageObj){
		return portalRoleService.getAllOtherUsersWithoutRoleByPage2(adminOrgId, orgId, adminId, roleId, userId, "", "", pageObj);
	}
	
	/**
	 * @author ma.guangming
	 * 批量添加角色下的成员
	 * @param roleUser
	 */
	@RequestMapping(value="/menu_roleMain_saveUsers2.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean batchAddUsersIntoRole( String str){
		if(str==null || "".equals(str)){
			return false;
		}
		String[] strs=str.split(";");
		long roleId=Long.parseLong(strs[0]);
		if(strs[1]==null || "".equals(strs[1])){
			return false;
		}
		RoleUser roleUser=new RoleUser();
		roleUser.setRoleId(roleId);
		String[] ids=strs[1].split(":");
		ArrayList<Long> list=new ArrayList<Long>();
		for(String id : ids){
			list.add(Long.parseLong(id));
		}
		roleUser.setIds(list);
		try{
			portalRoleService.batchAddUsersIntoRole(roleUser);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true ;
	}
	

	/**
	 * @author ma.guangming
	 * 批量删除角色下的成员
	 * @param roleUser
	 */
	@RequestMapping(value="/menu_roleMain_delUsers2.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean batchDeleteUsersFromRole(String str){
		if(str==null || "".equals(str)){
			return false;
		}
		String[] strs=str.split(";");
		long roleId=Long.parseLong(strs[0]);
		if(strs[1]==null || "".equals(strs[1])){
			return false;
		}
		RoleUser roleUser=new RoleUser();
		roleUser.setRoleId(roleId);
		String[] ids=strs[1].split(":");
		ArrayList<Long> list=new ArrayList<Long>();
		for(String id : ids){
			list.add(Long.parseLong(id));
		}
		roleUser.setIds(list);
		try{
			portalRoleService.batchDeleteUsersFromRole(roleUser);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true ;
	}
	/**
	 * 根据角色查操作权限
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/menu_roleMain_qryOptsByRoleId.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> qryOptsByRoleId(long roleId,long resourceId ,String optName,String appCode,PageObj pageObj){
		return portalRoleService.qryOptsByRoleId(roleId,resourceId,optName,appCode,pageObj);
	}
	
	/**
	 * 根据角色查操作权限
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/menu_roleMain_delOrGiveRoleOpt.do", method = RequestMethod.POST)
	@ResponseBody
	public String delOrGiveRoleOpt(long roleId,long optId ,int type){
		try {
			if(type==1){  //取消
				portalRoleService.delRoleOpt(roleId,optId);
			}
			if(type==2){  //添加
				portalRoleService.giveRoleOpt(roleId,optId);			
			}
			return "succ";
		} catch (Exception e) {
			return "fail";
		}
	}
}
