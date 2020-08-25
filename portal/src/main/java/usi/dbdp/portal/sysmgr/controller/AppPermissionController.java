package usi.dbdp.portal.sysmgr.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.sysmgr.service.PortalDataPriService;
import usi.dbdp.uic.entity.AppRegister;
import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.User;

/**
 * 应用权限管理
 * @author ma.guangming
 *
 */
@Controller
@RequestMapping("/appPermission")
public class AppPermissionController {
	
	@Resource
	private PortalDataPriService portalDataPriService;
	
	/**
	 *去往应用权限管理主界面 
	 * @return
	 */
	@RequestMapping(value="/menu_toApp_toAppPermission.do", method = RequestMethod.GET)
	public String toAppPermissionPage(){
		return "portal/system/appPermission"; 
	}
	
	/**
	 * @author ma.guangming
	 * 根据登录帐号或用户姓名查询管理员角色的用户
	 * @param value 查询字符串 
	 * @return 用户列表
	 */
	@RequestMapping(value="/menu_toApp_getAdmins.do", method = RequestMethod.POST)
	@ResponseBody
	public List<User> getAdminsByUserIdOrUserNameFromRole(HttpSession session,String value){
		Long province =Long.parseLong(session.getAttribute("province").toString()) ;
		String userId=session.getAttribute("userId").toString();
		return portalDataPriService.getAdminsByUserIdOrUserNameFromRole(value,province,userId);
	}
	
	/**
	 * 查询应用级应用
	 * @return List<AppRegister>
	 * @author  ma.guangming
	 */
	@RequestMapping(value="/menu_toApp_getNormalApps.do", method = RequestMethod.POST)
	@ResponseBody
	public List<AppRegister> getNomalAppsByState(){
		return portalDataPriService.getNomalAppsByState();
	}
	
	/**
	 * @author ma.guangming
	 * 查询登录帐号拥有的应用权限
	 * @param userId 登录帐号
	 * @return 数据权限列表
	 */
	@RequestMapping(value="/menu_toApp_getDataPris.do", method = RequestMethod.POST)
	@ResponseBody
	public List<DataPri> getDataPriByUserIdAndPrivilegeType(String userId ){
		return portalDataPriService.getDataPriByUserIdAndPrivilegeType(userId, 1);
	}
	
	/**
	  * 批量保存数据权限
	 * @param dataPris 数据权限列表
	 * @return true 添加成功 false 添加失败
	 */
	@RequestMapping(value="/menu_toApp_addDataPris.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean batchAddDataPris(String str , String userId){
		if(str==null || "".equals(str)){
			try{
				portalDataPriService.delDataPri(userId);
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}
		try{
			portalDataPriService.delDataPri(userId);
			List<DataPri>  dataPris=new ArrayList<DataPri>();
			//userId:privilegeValue;userId:privilegeValue;userId:privilegeValue;
			String[] strs=str.split(";");
			for(String s : strs){
				DataPri dp=new DataPri();
				String[] data= s.split(":");
				dp.setUserId(data[0]);
				dp.setPrivilegeValue(data[1]);
				dataPris.add(dp);
			}
			portalDataPriService.batchAddDataPris(dataPris);
		}catch(Exception e){
			e.printStackTrace();
		}
		return true ;
	}
	
	
}
