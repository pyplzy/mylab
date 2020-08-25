package usi.dbdp.portal.sysmgr.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.addressbook.service.AddressBookSyncService;
import usi.dbdp.portal.dto.UserDto;
import usi.dbdp.portal.entity.ChangeLog;
import usi.dbdp.portal.sysmgr.service.LogService;
import usi.dbdp.portal.sysmgr.service.PeopleOrgDataService;
import usi.dbdp.portal.sysmgr.service.PortalRoleService;
import usi.dbdp.portal.task.service.UserOrgIncService;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.uic.a2a.service.AccountingService;
import usi.dbdp.uic.a2a.service.AuthenticationService;
import usi.dbdp.uic.base.service.AppRegisterService;
import usi.dbdp.uic.base.service.OrgService;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleGrantInfo;
import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.entity.AppRegister;
import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.entity.User;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 机构人员管理
 * @author nie.zhengqian
 * 创建时间：2015年4月22日 上午11:33:45
 */
@Controller
@RequestMapping("/peopleManage")
public class PeopleManageController {
	@Resource
	private OrgService orgService;
	@Resource
	private AccountingService accountingService;
	@Resource
	private AppRegisterService appRegisterService;
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private LogService logService;	
	@Resource
	private AddressBookSyncService addressBookSyncService;
	@Resource
	private  PortalRoleService  portalRoleService;
	@Resource
	private  PeopleOrgDataService  peopleOrgDataService;
	@Resource
	UserOrgIncService userOrgIncService;
	
	
	/**
	 * 前往管理页面
	 * @return
	 */
	@RequestMapping(value="/menu_to_toMain.do", method = RequestMethod.GET)
	public String toManagePage(HttpSession session,Model model){
		Long province = Long.parseLong(session.getAttribute("province").toString());
		long staffId = Long.parseLong(session.getAttribute("staffId").toString());
		model.addAttribute("provinces", portalRoleService.getProvinces(province));
		
		model.addAttribute("optList", portalRoleService.qryOptListById(staffId));
		return "portal/system/peopleManage";
	}
	
	/**
	 * 前往地市管理页面
	 * @return
	 */
	@RequestMapping(value="/menu_to_toMainMenuForCity.do", method = RequestMethod.GET)
	public String toMainMenuForCity(HttpSession session,Model model){
		Long province = Long.parseLong(session.getAttribute("province").toString());
		model.addAttribute("provinces", portalRoleService.getProvinces(province));
		return "portal/system/peopleManageForCity";
	}
	
	/**
	 * 加载机构树
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value="/menu_to_getOrgTree.do", method = RequestMethod.POST)
	@ResponseBody
	public List<Org> getOrgTree(long orgId){
		return orgService.getDirectSubOrgsById(orgId);
	}
	
	/**
	 * 判断机构是否可删除
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value="/menu_to_jugeCandel.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String ,Object> jugeCandel(long orgId ,String userId, String userName,PageObj pageObj){
		String value="";
		 Map<String ,Object> map = new HashMap<String ,Object>();
		if(orgService.getDirectSubOrgsById(orgId).size()>0){
			value = "yes";
		}
		map = orgService.getUsersByOrgIdWithPage(orgId, userId, userName, pageObj);
		map.put("hasChildOrg", value);
		return map;
	}
	
	/**
	 * 根据树orgId查用户 分页
	 * @param orgId
	 * @param pageObj
	 * @return
	 */
	@RequestMapping(value="/menu_to_getUserList.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getUsersByOrgIdWithPage(long orgId, String userId, String userName,PageObj pageObj){
		return orgService.getUsersByOrgIdWithPage(orgId, userId, userName, pageObj);
		
	}
	/**
	 * 根据登录人orgId查用户 分页
	 * @param orgId
	 * @param pageObj
	 * @return
	 */
	@RequestMapping(value="/menu_to_getAllUserList.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllUsersByOrgIdWithPage(long orgId, String userId, String userName,PageObj pageObj){
		return orgService.getAllUsersByOrgIdWithPage(orgId, userId, userName, pageObj);
	}
	/**
	 * 查用户基本信息
	 * @param userId 
	 * @return
	 */
	@RequestMapping(value="/menu_to_serchUserMsg.do", method = RequestMethod.POST)
	@ResponseBody
	public UserDto serchUserMsg(String userId){
		User user = accountingService.getUserByUserId(userId);
		UserDto userDto = new UserDto();
		userDto.setUserId(user.getUserId());
		userDto.setLoginId(user.getLoginId());
		userDto.setOrgId(user.getOrgId());
		userDto.setOrgName(user.getOrgName());
		userDto.setUserName(user.getUserName());
		userDto.setMobileNo(user.getMobileNo());
		userDto.setGender(user.getGender());
		userDto.setDelFlag(user.getDelFlag());
		userDto.setUserType(user.getUserType());
		userDto.setCreateDate(CommonUtil.format(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		return userDto;
	}
	
	/**
	 * 修改个人信息
	 * @param userDto
	 * @return
	 */
	@RequestMapping(value="/menu_to_updateBaseMsg.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateBaseMsg(UserDto userDto,HttpSession session){
		boolean flag = false;
		try {
			User user = accountingService.getUserByLoginId(userDto.getLoginId());
			user.setUserName(userDto.getUserName());
			user.setCreateTime(CommonUtil.parse(userDto.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			user.setGender(userDto.getGender());
			user.setMobileNo(userDto.getMobileNo());
			user.setDelFlag(userDto.getDelFlag());
			user.setUserType(userDto.getUserType());
			user.setOrgId(userDto.getOrgId());
			
			//同步到通讯录
			addressBookSyncService.sync2AddressBook(user);
			
			//更新通讯录公司和部门字段
			Org org = orgService.queryOrgById(user.getOrgId());
			String company = "";
			String department = "";
			//切成数组
			String [] orgNameArr = org.getOrgNameSeq().split("->");
			//数组长度
			int lenOfOrgNameArr = orgNameArr.length;
			//直接挂在集团下面
			if(lenOfOrgNameArr==1) {
				company = orgNameArr[0];
			//取省公司
			}else if(lenOfOrgNameArr>1){
				company = orgNameArr[1];
				StringBuilder tmpDepartment = new StringBuilder("");
				//拼接部门
				for(int i=2;i<lenOfOrgNameArr;i++) {
					if(i==lenOfOrgNameArr-1) {
						tmpDepartment.append(orgNameArr[i]);
					}else {
						tmpDepartment.append(orgNameArr[i]).append("->");
					}
				}
				department = tmpDepartment.toString();
			}
			addressBookSyncService.updateABcompanyAndDepartment(company, department, user.getUserId());
			flag = accountingService.updateUser(user);
			insertChanglOg(user,(String) session.getAttribute("userId"),(String) session.getAttribute("ip"));
			//修改人员信息，数据插入人员增量表
			User userForInc=userOrgIncService.getUserByUserIdForInc(user.getId());
			userOrgIncService.insertIncUser(userForInc,"U");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public void insertChanglOg(User user,String userId,String ip){
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId(userId);
		changeLog.setOptObj("用户表");
		changeLog.setOptType(LogService.OPT_UPDATE);
		changeLog.setOptIp(ip);
		//原记录长什么样子
		User newUser = accountingService.getUserByLoginId(user.getLoginId());
		try {
			changeLog.setOptContent("uic_user:原记录="+JacksonUtil.obj2json(user)+",新记录="+JacksonUtil.obj2json(newUser));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logService.saveChangeLogInfo(changeLog);
		
	}
	
	/**
	 * 重置密码
	 * @param loginId
	 * @return
	 */
	@RequestMapping(value="/menu_to_updatePassWord.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean updatePassWord(String loginId,String newPassword,HttpSession session){
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("用户表");
		changeLog.setOptType(LogService.OPT_UPDATE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		changeLog.setOptContent("重置密码:update uic_user set PASSWORD =? where login_id = ? "+newPassword+":"+loginId);
		logService.saveChangeLogInfo(changeLog);
		User userForInc=userOrgIncService.getUserByUserIdForInc((Long) session.getAttribute("staffId"));
		userOrgIncService.insertIncUser(userForInc,"U");
		return accountingService.resetPasswordByLoginId(loginId,newPassword);
	}
	
	/**
	 * 个人所属机构修改判断
	 * @param loginId orgIdChangeSeq
	 * @return yes 允许修改 no不允许
	 */
	@RequestMapping(value="/menu_to_jugeOrgChange.do", method = RequestMethod.POST)
	@ResponseBody
	public String jugeOrgChange(HttpSession session,String loginId,String orgIdChangeSeq){
		String userId = session.getAttribute("userId").toString();
		Org org = orgService.getOrgByLoginId(loginId);
		String seq = "";
		String flag = "no";
		//原始机构序列 机构级别  级别为1  允许修改到任意机构   级别为2限制在省内  级别为3限制在市内
		if(org.getOrgGrade()>=3){
			seq = cutOrgIdSeq(org.getOrgIdSeq(), 3);
			if(orgIdChangeSeq.indexOf(seq)>=0){
				flag = "yes";
			}
		}
		if(org.getOrgGrade()==2){
			seq = cutOrgIdSeq(org.getOrgIdSeq(), 2);
			if(orgIdChangeSeq.indexOf(seq)>=0){
				flag = "yes";
			}
		}
		if(org.getOrgGrade()==1){
			flag = "yes";
		}
		if(ConfigUtil.getValue("sysadmin").equals(userId)){
			flag = "yes";
		}
		return flag;
	}
	
	/**
	 * 截取前len位机构序列
	 * @param orgIdSeq
	 * @param len
	 * @return
	 */
	public static String cutOrgIdSeq(String orgIdSeq,int len){
		char arr[] = orgIdSeq.toCharArray();
		int num=0;
		String ss = "";
		for(int i=0; i<arr.length;i++){
			if(arr[i]=='.'){
				num++;
			}
			if(num==len){
				ss = orgIdSeq.substring(0, i+1);
				break;
			}
		}
		return ss;
	}
	
	/**
	 * 查询所有激活的应用
	 * @return
	 */
	@RequestMapping(value="/menu_to_getAllAppCode.do", method = RequestMethod.POST)
	@ResponseBody
	public List<AppRegister> getAllAppCode(){
		return appRegisterService.queryAppByState();
	}
	
	/**
	 * 查询能该账号查看的激活的应用
	 * @return
	 */
	@RequestMapping(value="/menu_to_getAppCodes.do", method = RequestMethod.POST)
	@ResponseBody
	public List<AppRegister> getAppCodes(HttpSession session){
		Long province = Long.parseLong(session.getAttribute("province").toString());
		return appRegisterService.getAppCodes(province);
	}
	
	/**
	 * 查询role 分页  + 查询条件 + appCode + id
	 * @param appCode
	 * @return
	 */
	@RequestMapping(value="/menu_to_getAllRoleByApp.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllRoleByApp(HttpSession session,RoleGrantInfo roleGrantInfo, PageObj pageObj){
		String userId = (String) session.getAttribute("userId");
		if(ConfigUtil.getValue("sysadmin").equals(userId)){
			roleGrantInfo.setRoleType(2);
		}else{
			roleGrantInfo.setRoleType(1);
		}
		return authenticationService.getRoleGrantInfos(pageObj, roleGrantInfo);
	}
	
	/**
	 *取消授予角色
	 * @param id
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/menu_to_delPermission.do", method = RequestMethod.POST )
	@ResponseBody
	public boolean savePermission(long id, long roleId){
		return authenticationService.delPermissionByIdAndRoleId(id, roleId);
	}
	/**
	 *授予角色
	 * @param id
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/menu_to_givePermission.do", method = RequestMethod.POST )
	@ResponseBody
	public boolean givePermission(long id, long roleId){
		return authenticationService.givePermissionByIdAndRoleId(id, roleId);
	}
	/**
	 * 检测工号是否可用
	 * @param userId 工号
	 * @return
	 */
	@RequestMapping(value="/menu_to_checkUserIdInUse.do", method = RequestMethod.POST )
	@ResponseBody
	public boolean checkUserIdInUse(String userId){
		return accountingService.checkUserIdInUse(userId);
	}
	
	/**
	 * 新增机构人员
	 * @return
	 */
	@RequestMapping(value="/menu_to_isnertUserMsg.do", method = RequestMethod.POST )
	@ResponseBody
	public boolean isnertUserMsg(User user,HttpSession session){
		user.setCreateTime(new Date());
		user.setDuration(90);
		user.setLstErrPwdTime(new Date());
		user.setPwdErrCnt(0);
		//初始密码userId+"000000"  md5加密
		user.setPassword(CommonUtil.getMd5(user.getUserId()+"000000"));
		
		//同步到通讯录
		addressBookSyncService.sync2AddressBook(user);
		
		//更新通讯录公司和部门字段
		Org org = orgService.queryOrgById(user.getOrgId());
		String company = "";
		String department = "";
		//切成数组
		String [] orgNameArr = org.getOrgNameSeq().split("->");
		//数组长度
		int lenOfOrgNameArr = orgNameArr.length;
		//直接挂在集团下面
		if(lenOfOrgNameArr==1) {
			company = orgNameArr[0];
		//取省公司
		}else if(lenOfOrgNameArr>1){
			company = orgNameArr[1];
			StringBuilder tmpDepartment = new StringBuilder("");
			//拼接部门
			for(int i=2;i<lenOfOrgNameArr;i++) {
				if(i==lenOfOrgNameArr-1) {
					tmpDepartment.append(orgNameArr[i]);
				}else {
					tmpDepartment.append(orgNameArr[i]).append("->");
				}
			}
			department = tmpDepartment.toString();
		}
		addressBookSyncService.updateABcompanyAndDepartment(company, department, user.getUserId());
		
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("用户表");
		changeLog.setOptType(LogService.OPT_INSERT);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		try {
			changeLog.setOptContent("uic_user:"+JacksonUtil.obj2json(user));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logService.saveChangeLogInfo(changeLog);
		return	accountingService.addUser(user);
	}
	
	/**
	 * 删除机构人员    逻辑删除
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/menu_to_removeOrgUser.do", method = RequestMethod.POST )
	@ResponseBody
	public boolean removeOrgUser(UserInfo user,HttpSession session){
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("用户表");
		changeLog.setOptType(LogService.OPT_DELETE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		changeLog.setOptContent("uic_user:update uic_user set del_flag=0   where id = ? :"+user.getId());
		logService.saveChangeLogInfo(changeLog);
		//用户数据删除，插入用户增量表
		User userForInc=userOrgIncService.getUserByUserIdForInc(user.getId());
		userOrgIncService.insertIncUser(userForInc,"D");
		return accountingService.deleteUserByUserId(user);
	}
	/**
	 * 
	 * @Title: updateUserStatus
	 * @Author: zhang.rui
	 * @Date: 2018年8月3日上午9:49:09
	 * @Description: 更新用户状态（锁定：2/解锁：0）
	 * @param loginId 
	 * @param status
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/menu_to_updateUserStatus.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateUserStatus(String loginId,String status,HttpSession session){
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("用户表");
		changeLog.setOptType(LogService.OPT_UPDATE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		changeLog.setOptContent("更新用户状态:update uic_user set status =? where login_id = ? "+status+":"+loginId);
		logService.saveChangeLogInfo(changeLog);
		return accountingService.updateUserStatus(loginId, status); 
	}
	
	/**
	 * 新增机构
	 * @param org
	 * @return
	 */
	@RequestMapping(value="/menu_to_insertOrg.do", method = RequestMethod.POST )
	@ResponseBody
	public boolean insertOrg(Org org,HttpSession session){
		
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("机构表");
		changeLog.setOptType(LogService.OPT_INSERT);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		try {
			changeLog.setOptContent("uic_org:"+JacksonUtil.obj2json(org));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logService.saveChangeLogInfo(changeLog);
		return orgService.addOrg(org);
	}
	/**
	 * 修改机构
	 * @param org
	 * @return
	 */
	@RequestMapping(value="/menu_to_updateOrg.do", method = RequestMethod.POST )
	@ResponseBody
	public boolean updateOrg(Org org,HttpSession session){
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("机构表");
		changeLog.setOptType(LogService.OPT_UPDATE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		//原机构长什么样子
		Org originalOrg = orgService.queryOrgById(org.getOrgId());
		try {
			changeLog.setOptContent("uic_org:原记录="+JacksonUtil.obj2json(originalOrg)+",新记录="+JacksonUtil.obj2json(org));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logService.saveChangeLogInfo(changeLog);
		//机构数据新增，插入机构增量表
		Org orgForInc=this.searchLoginOrg(org.getOrgId());
		userOrgIncService.insertIncOrg(orgForInc, "U");
		
		String orgNameSeq = org.getOrgNameSeq();
		//修改后的机构序列名称
		StringBuilder sb = new StringBuilder();
		String[] str = orgNameSeq.split("->");
		for(int i=0;i<str.length-1;i++){
			sb.append(str[i]+"->");
		}
		if(org.getpOrgId()<=0){
			org.setpOrgId(originalOrg.getpOrgId());
		}
		org.setOrgNameSeq(sb.toString()+org.getOrgName());
		//判断更新子机构序列
		if(!org.getOrgNameSeq().equals(originalOrg.getOrgNameSeq())){
			orgService.updateChildOrgSeq(originalOrg.getOrgIdSeq(),org.getOrgNameSeq(),originalOrg.getOrgNameSeq());
		}
		return orgService.updateOrg(org);
	}
	
	/**
	 * 删除机构
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value="/menu_to_deleteOrgMenu.do", method = RequestMethod.POST )
	@ResponseBody
	public boolean deleteOrgMenu(long orgId,HttpSession session){
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("机构表");
		changeLog.setOptType(LogService.OPT_DELETE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		changeLog.setOptContent("uic_org:update uic_org set del_flag = 0 where org_id = ?:"+orgId);
		logService.saveChangeLogInfo(changeLog);
		//机构数据新增，插入机构增量表
		Org orgForInc=this.searchLoginOrg(orgId);
		userOrgIncService.insertIncOrg(orgForInc, "D");
		
		return orgService.deleteOrgById(orgId);
	}
	
	/**
	 * 查询当前机构信息
	 * @return
	 */
	@RequestMapping(value="/menu_to_searchLoginOrg.do", method = RequestMethod.POST )
	@ResponseBody
	public Org searchLoginOrg(long orgId){
		return orgService.queryOrgById(orgId);
	}
	/**
	 * 查询用户配置的机构权限
	 * @return
	 */
	@RequestMapping(value="/menu_to_searchRootOrg.do", method = RequestMethod.POST )
	@ResponseBody
	public Org searchRootOrg(long orgId,String userId){
		long priOrgId = peopleOrgDataService.searchPriOrgId(userId);
		if(priOrgId>0){
			return orgService.queryOrgById(priOrgId);
		}else{
			return orgService.queryOrgById(orgId);
		}
	}
	/**
	 * 检查orgCode是否重复
	 * @return
	 */
	@RequestMapping(value="/menu_to_checkOrgCode.do", method = RequestMethod.POST )
	@ResponseBody
	public String checkOrgCode(String orgCode){
		return peopleOrgDataService.checkOrgCode(orgCode);
	}
	
	/**
	 * 添加业务归属人员
	 * @return
	 */
	@RequestMapping(value="/menu_to_addUserOrgType2.do", method = RequestMethod.POST )
	@ResponseBody
	public String addUserOrgType2(long orgId,long staffId){
		try {
			if(peopleOrgDataService.searchCount(orgId,staffId)>0){
				return "use";
			}
			peopleOrgDataService.addUserOrgType2(orgId,staffId);
			return "succ";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	
	/**
	 * 检查orgCode是否重复
	 * @return
	 */
	@RequestMapping(value="/menu_to_getUsersByOrgId.do", method = RequestMethod.POST )
	@ResponseBody
	public Map<String , Object> getUsersByOrgId(Long adminOrgId,Long orgId  , String adminId , String userId , PageObj pageObj){
		return peopleOrgDataService.getUsersByOrgId(adminOrgId,orgId,adminId,userId,pageObj);
	}
	
	
}
