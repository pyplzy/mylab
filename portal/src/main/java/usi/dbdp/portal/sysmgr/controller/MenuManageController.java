package usi.dbdp.portal.sysmgr.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import usi.dbdp.portal.entity.ChangeLog;
import usi.dbdp.portal.sysmgr.service.LogService;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.uic.base.service.DataPriService;
import usi.dbdp.uic.base.service.MenuService;
import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.Menu;

/**
 * 菜单管理
 * @author nie.zhengqian
 * 创建时间：2015年4月16日 下午9:02:42
 */
@Controller
@RequestMapping("/menuManage")
public class MenuManageController {
	@Resource
	MenuService menuService;
	@Resource
	DataPriService dataPriService;
	@Resource
	private LogService logService;
	
	/**
	 * 前往菜单管理页面
	 * @return
	 */
	@RequestMapping(value="/menu_mainPage_toMainPage.do", method = RequestMethod.GET)
	public String toMainPageMenu(HttpSession session,Model model){
		String userId = (String)session.getAttribute("userId");
		model.addAttribute("appItems", dataPriService.getDataPrisByUserIdAndPrivilegeType(userId, 1));
		return "portal/system/menuManage";
	}
	
	/**
	 * 根据appCode查找菜单树
	 * @param appCode
	 * @return
	 */
	@RequestMapping(value="/menu_mainPage_getAllMenu.do", method = RequestMethod.POST)
	@ResponseBody
	public List<Menu> getAllMenusByAppCode(String appCode){
		return menuService.getAllMenusByAppCode(appCode);
	}
	
	/**
	 * 根据userId和数据类型查应用
	 * @param session
	 * @param privilegeType
	 * @return
	 */
	@RequestMapping(value="/menu_mainPage_getAllAppcodeList.do", method = RequestMethod.POST)
	@ResponseBody
	public List<DataPri> getAllAppcodeList(HttpSession session, int privilegeType){
		String userId = (String)session.getAttribute("userId");
		//查询登录帐号拥有的权限(userId, 权限类型)
		return dataPriService.getDataPrisByUserIdAndPrivilegeType(userId, privilegeType);
		
	}
	/**
	 * 删除子节点菜单
	 * @param appCode 应用编码
	 * @param menuId 菜单id
	 * @param session
	 * @return
	 */
	@RequestMapping(value="menu_mainPage_deleteLeafMenu.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean deleteLeafMenu(String appCode, int menuId,HttpSession session){
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("菜单表");
		changeLog.setOptType(LogService.OPT_DELETE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		changeLog.setOptContent("uic_menu:update uic_menu set del_flag = 1 where APP_CODE = ? and MENU_ID = ? :"+appCode+":"+menuId);
		logService.saveChangeLogInfo(changeLog);
		return menuService.deleteMenuById(appCode, menuId);
	}
	/**
	 * 修改菜单
	 * @param appCode 应用编码
	 * @param menu 菜单实体
	 * @param session
	 * @return
	 */
	@RequestMapping(value="menu_mainPage_updateMenu.do",method = RequestMethod.POST)
	@ResponseBody
	public boolean updateMenu(String appCode, Menu menu,HttpSession session){
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("菜单表");
		changeLog.setOptType(LogService.OPT_UPDATE);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		//原菜单长什么样子
		Menu originalMenu = menuService.queryMenuById(appCode, menu.getMenuId());
		try {
			changeLog.setOptContent("uic_menu:原纪录="+JacksonUtil.obj2json(originalMenu)+",新纪录="+JacksonUtil.obj2json(menu)+",{\"appCode\":\""+appCode+"\"}");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logService.saveChangeLogInfo(changeLog);
		
		return menuService.updateMenu(appCode, menu);
	}
	
	/**
	 * 新增菜单
	 * @param appCode 应用编码
	 * @param menu 菜单实体
	 * @param session
	 * @return
	 */
	@RequestMapping(value="menu_mainPage_insertMenu.do",method = RequestMethod.POST)
	@ResponseBody
	public boolean insertMenu(String appCode, Menu menu,HttpSession session){
		//记录日志
		ChangeLog changeLog = new ChangeLog();
		changeLog.setUserId((String) session.getAttribute("userId"));
		changeLog.setOptObj("菜单表");
		changeLog.setOptType(LogService.OPT_INSERT);
		changeLog.setOptIp((String) session.getAttribute("ip"));
		try {
			changeLog.setOptContent("uic_menu:"+JacksonUtil.obj2json(menu)+",{\"appCode\":\""+appCode+"\"}");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logService.saveChangeLogInfo(changeLog);
		
		return menuService.addMenu(appCode, menu);
	}
	
	

}
