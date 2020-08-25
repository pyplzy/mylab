package usi.dbdp.portal.sysmgr.controller;

 import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

 import usi.dbdp.portal.sysmgr.service.PeopleOrgDataService;
import usi.dbdp.uic.dto.PageObj;

/**
 * 
 * @author 
 *机构数据权限
 */
@Controller
@RequestMapping("/peopleOrgData")
public class PeopleOrgDataController {
	@Resource
	private PeopleOrgDataService peopleOrgDataService;
	
	@RequestMapping(value="/menu_toOrg_toMenuOrgData.do", method = RequestMethod.GET)
	public String toManagePage(HttpSession session,Model model){
		return "portal/system/peopleOrgData";
	}
	@RequestMapping(value="/menu_toOrg_getUsersForNow.do")
	@ResponseBody
	public Map<String, Object>  getUsersForNowOrg(Long orgId,String userName, HttpSession session ,PageObj pageObj){
		Long orgIdLong=null;
		if(orgId==null){
			 orgIdLong=(Long) session.getAttribute("orgId");
		}else{
			orgIdLong=orgId;
		}
		
		return peopleOrgDataService.getUsersForNowOrg(orgIdLong, userName,pageObj);
	}	
	
	@RequestMapping(value="/menu_toOrg_deleteUserOrg.do")
	@ResponseBody
	public String   deleteUserOrg(String userId,Long orgId){
		return peopleOrgDataService.deleteUserOrg(userId,orgId);
	}	
	@RequestMapping(value="/menu_toOrg_editUserOrg.do")
	@ResponseBody
	public String   editUserOrg(String userId,Long orgId,int type){
		try {
			peopleOrgDataService.editUserOrg(userId,orgId,type);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "failure";
		}				
	}	
}
