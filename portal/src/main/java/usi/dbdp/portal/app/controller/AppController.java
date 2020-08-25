package usi.dbdp.portal.app.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.app.service.AppService;
import usi.dbdp.uic.entity.AppRegister;

/**
 * 应用注册
 * @author ma.guangming
 *
 */
@Controller
@RequestMapping("/app")
public class AppController {
	@Resource
	private AppService appService;
	/**
	 * 查询所有的应用
	 * @return List<AppRegister>
	 */
	@RequestMapping(value = "/menu_appReg_appReg.do", method = RequestMethod.GET)
	public String queryAppByState(Model model){
		model.addAttribute("apps", appService.queryAppByState()) ;
		return "portal/appManage/appReg";
	}
	/**
	 * 激活某应用（将应用信息状态从失效状态改为生效）
	 * @param appCode 应用code
	 * @return true表示操作成功，false表示操作失败
	 */
	@RequestMapping(value = "/menu_appReg_actApp.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean activateAppByAppCode(String appCode){
		return appService.activateAppByAppCode(appCode);
	}
	/**
	 * 删除某应用（逻辑删除，设置应用信息状态为失效）
	 * @param appCode 应用code
	 * @return true表示操作成功，false表示操作失败
	 */
	@RequestMapping(value = "/menu_appReg_delApp.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean deleteAppByAppCode(String appCode){
		return appService.deleteAppByAppCode(appCode);
	}
	
	/**
	 * 判断应用code 是否可用
	 * @param appCode 应用code
	 * @return true表示操作可用，false表示重复不可用
	 */
	@RequestMapping(value = "/menu_appReg_judgeApp.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean judgeAppByAppCode(String appCode){
		return appService.judgeAppByAppCode(appCode);
	}
	
	/**
	 * 增加一个应用
	 * @param appRegister 应用信息对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@RequestMapping(value = "/menu_appReg_addApp.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean addApp(AppRegister appRegister){
		return appService.addApp(appRegister);
	}
	
	/**
	 * 查询应用概览
	 * @return List<AppRegister>
	 */
	@RequestMapping(value = "/menu_appOver_appOverview.do", method = RequestMethod.GET)
	public String queryAppOverView(HttpSession session , Model model){
//		List<AppRegister> ars=appService.queryAppByState();
//		for(AppRegister ar :ars){
//			System.out.println(ar.getAppName()+"|||"+ar.getAppType());
//		}
		Long id=(Long)session.getAttribute("staffId");
		model.addAttribute("apps", appService.queryAllAppList(id)) ;
		return "portal/appManage/appOverview";
	}
	
	/**
	 * 根据应用appCode查应用信息
	 * @param appCode 应用code
	 * @return 应用信息对象
	 */
	@RequestMapping(value = "/menu_appReg_queryApp.do", method = RequestMethod.POST)
	@ResponseBody
	public AppRegister queryAppByAppCode(String appCode){
		return appService.queryAppByAppCode(appCode);
	}
	
	/**
	 * 更新某应用信息
	 * @param appRegister 应用信息对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@RequestMapping(value = "/menu_appReg_updApp.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateApp( AppRegister appRegister){
		return appService.updateApp(appRegister);
	}
	
	
}
