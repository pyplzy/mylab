package usi.dbdp.portal.sysmgr.controller;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.addressbook.service.AddressBookSyncService;
import usi.dbdp.portal.task.service.UserOrgIncService;
import usi.dbdp.portal.util.EncryptUtil;
import usi.dbdp.uic.a2a.service.AccountingService;
import usi.dbdp.uic.base.service.OrgService;
import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.entity.User;

/**
 * 系统管理-个人信息修改
 * @author nie.zhengqian
 * 创建时间：2015年4月16日 下午2:19:34
 */
@Controller
@RequestMapping("/personMsg")
public class PersonMsgChangeController {
	@Resource
	private AccountingService accountingService;
	@Resource
	private AddressBookSyncService addressBookSyncService;
	@Resource
	private OrgService orgService;
	@Resource
	UserOrgIncService userOrgIncService;
	/**
	 * 到菜单页面
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/menu_toPage_toPage.do", method = RequestMethod.GET)
	public String toPersonPage(HttpSession session, Model model){
		String userId = (String)session.getAttribute("userId");//工号
		String loginId = (String)session.getAttribute("loginId");//登录号
		User user = accountingService.getUserByUserId(userId);
		model.addAttribute("user", user);
		model.addAttribute("loginId", loginId);
		return "portal/system/personalData"; 
	}
	
	/**
	 * 修改密码
	 * @param session
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return
	 */
	@RequestMapping(value="/menu_toPage_updatePwd.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean passWordEdit(HttpSession session,String oldPassword, String newPassword){
		String loginId = (String)session.getAttribute("loginId");
		//用户数据更新，插入用户增量表
		User userForInc=userOrgIncService.getUserByUserIdForInc((Long) session.getAttribute("staffId"));
		userOrgIncService.insertIncUser(userForInc,"U");
		return accountingService.updateUserPasswordByLoginId(loginId, oldPassword, newPassword);
	}
	
	/**
	 * 更新个人信息
	 * @param session
	 * @param gender 性别
	 * @param phoneNum 手机号码
	 * @param userName 姓名
	 * @param loginId 登录号
	 * @return
	 */
	@RequestMapping(value="/menu_toPage_updateUserMsg.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean savaUserMsg(HttpSession session,Integer gender, String phoneNum, String userName,String loginId){
		String userId = (String)session.getAttribute("userId");
		User user = accountingService.getUserByUserId(userId);
		user.setUserName(userName);
		user.setGender(gender);
		user.setMobileNo(phoneNum);
		user.setLoginId(loginId);
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
		String oldLoginId = (String)session.getAttribute("loginId");
		//如果修改了登录号，则要重置密码
		if(!oldLoginId.equals(loginId)) {
			accountingService.updateUserPasswordByLoginId(oldLoginId, user.getPassword(), EncryptUtil.doEncrypt(loginId+"000000"));
			user.setPassword(EncryptUtil.doEncrypt(loginId+"000000"));//最后一步更新的时候涉及到了密码，所以此处，应用使用重置后的密码
		}
		boolean flag=accountingService.updateUser(user);
		if(flag){
			//用户数据更新，插入用户增量表
			User userForInc=userOrgIncService.getUserByUserIdForInc(user.getId());
			userOrgIncService.insertIncUser(userForInc,"U");
		}
		return flag;
	}
	
	/**
	 * 检测loginId（登录号）是否可以使用
	 * @param loginId 登录号
	 * @param pw
	 */
	@RequestMapping(value="/menu_toPage_checkLoginIdCanUse.do", method = RequestMethod.POST)
	public void checkLoginIdCanUse(String loginId,PrintWriter pw) {
		if(accountingService.checkLoginIdInUse(loginId)) {
			pw.write("success");
		}else {
			pw.write("fail");
		}
	}
}
