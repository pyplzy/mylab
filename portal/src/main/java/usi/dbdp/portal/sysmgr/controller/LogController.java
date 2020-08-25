package usi.dbdp.portal.sysmgr.controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.dto.ChangeLogInfo;
import usi.dbdp.portal.entity.LoginLog;
import usi.dbdp.portal.sysmgr.service.LogService;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.uic.dto.PageObj;

/**
 * 登录日志和信息变更日志查询
 * @author lmwang
 * 创建时间：2015-4-16 下午9:09:22
 */
@Controller
@RequestMapping("/log")
public class LogController {
	
	@Resource
	private LogService logService;

	/**
	 * 到日志查询页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/menu_to_toMenu.do", method = RequestMethod.GET)
	public String toMenu(Model model){
		//日期输入框默认今天
		model.addAttribute("today", CommonUtil.format(new Date(), "yyyy-MM-dd"));
		model.addAttribute("todayStart", CommonUtil.format(new Date(), "yyyy-MM-dd")+" 00:00:00");
		model.addAttribute("todayEnd", CommonUtil.format(new Date(), "yyyy-MM-dd")+" 23:59:59");
		return "portal/system/logQuery";
	}
	
	/**
	 * 查询登录日志
	 * @param loginLog 登录日志
	 * @param pageObj
	 * @return 
	 */
	@RequestMapping(value="/menu_to_getLoginLogs.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object > getLoginLog(LoginLog loginLog ,PageObj pageObj){
		return logService.getUicLoginLogInfo(loginLog, pageObj);
	}
	/**
	 * 查询信息变更日志
	 * @param changeLogInfo 信息变更日志dto
	 * @param pageObj 分页对象
	 * @return 
	 */
	@RequestMapping(value="/menu_to_getChgLogs.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object > getChangeLog(ChangeLogInfo changeLogInfo, PageObj pageObj){
		return logService.getUicChangeLogInfo(changeLogInfo, pageObj);
	}
}
