package usi.dbdp.portal.common.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.app.service.AppService;
import usi.dbdp.portal.app.service.WeatherService;
import usi.dbdp.portal.bulletin.service.BulletinService;
import usi.dbdp.portal.dto.BulletinDto;
import usi.dbdp.portal.dto.NewsDto;
import usi.dbdp.portal.news.service.NewsService;
import usi.dbdp.portal.todo.service.TodoService;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.Lunar;
import usi.dbdp.uic.dto.AppRegisterInfo;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.entity.AppRegister;

/**
 * 用户中心首页
 * @author zhang.dechang
 * 2017年1月13日 下午5:41:34
 */
@Controller
@RequestMapping("/homePage")
public class HomePageController {
	private Logger logger = LogManager.getLogger(getClass());
	
	@Resource
	private BulletinService bulletinService;
	@Resource
	private AppService appService;
	@Resource
	private WeatherService weatherService;
	@Resource
	private NewsService newsService;
	@Resource
	private TodoService todoService;
	
	/**
	 * 去往主界面
	 * @return
	 */
	@RequestMapping(value="/menu_main_main.do" ,method = RequestMethod.GET)
	public String toMain(Model model,HttpSession session){
		Long province = Long.parseLong(session.getAttribute("province").toString());
		logger.debug("province={}", province);
		
		PageObj noticePage =new PageObj(5, 1);	//最多查询出5条公告
		List<BulletinDto> noticeList=bulletinService.queryNoticeByPage(noticePage,province);
		List<NewsDto> newsList = newsService.getNewsForPortal(province);
		model.addAttribute("newsList", newsList);
		model.addAttribute("noticeList", noticeList);
		return "portal/home";
	}
	
	/**
	 * 查询首页应用
	 * @param userId 登录帐号
	 * @return
	 */
	@RequestMapping(value = "/menu_main_app-list.do", method = RequestMethod.POST)
	@ResponseBody
/*	public List<AppRegisterInfo> getAllAppsByUserId(HttpSession session){
		String userId = session.getAttribute("userId").toString();
		PageObj pageObj =new PageObj(12,1);
		List<AppRegisterInfo> appRegisterInfos=appService.getAllAppsByUserIdWithPage(pageObj , userId);
		return appRegisterInfos;
	}*/
	public List<AppRegisterInfo> getAllAppsByUserId(HttpSession session,PageObj pageObj){
		String userId = session.getAttribute("userId").toString();
		List<AppRegisterInfo> appRegisterInfos=appService.getAllAppsByUserIdWithPage(pageObj , userId);
		return appRegisterInfos;
	}
	/**查询首页我的待办
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/menu_main_todo-list.do", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> getAllTodosByUserId(HttpSession session){
		Long id = (Long)session.getAttribute("staffId");
		List<AppRegister> myapps = appService.queryAllAppList(id);
		List<Map<String, Object>> todoList = null;
		try {
			todoList = todoService.invokeInterface(myapps);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return todoList;
	}
	
	/**
	 * 点击"更多"按钮跳转到公告展现界面
	 * @return
	 */
	@RequestMapping(value = "menu_main_to-notices-more.do", method = RequestMethod.GET)
	public String toNoticesMore(){
		return "portal/noticesMore";
	}
	
	/**
	 * 分页查询公告
	 * @return
	 */
	@RequestMapping(value = "menu_main_notices-more.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object > queryNoticePublishedByPage(PageObj pageObj ,Model model ,HttpSession session ){
		Long province = Long.parseLong(session.getAttribute("province").toString());
		Map<String , Object > map =new HashMap<String , Object >();
		if(pageObj.getRows()==0){
			pageObj.setRows(2);
		}
		if(pageObj.getPage()==0){			
			pageObj.setPage(1);
		}
		List<BulletinDto> bulletinDtos=bulletinService.queryNoticePublishedByPage(pageObj,province);
		map.put("bulletinDtos",bulletinDtos);
		map.put("pageObj",pageObj);
		return map;
	}
	
	/**
	 * 去往更多应用界面
	 * @return List<AppRegister>
	 */
	@RequestMapping(value = "/menu_main_appMore.do", method = RequestMethod.GET)
	public String toAppMore(){
		return "portal/appMore";
	}
	
	/**
	 * 查询更多应用
	 * @return List<AppRegister>
	 */
	@RequestMapping(value = "/menu_main_appMore.do", method = RequestMethod.POST)
	@ResponseBody
	public List<AppRegister> queryAppMore(HttpSession session){
//		List<AppRegister> ars=appService.queryAppByState();
//		for(AppRegister ar :ars){
//			System.out.println(ar.getAppName()+"|||"+ar.getAppType());
//		}
		Long id=(Long)session.getAttribute("staffId");
		return appService.queryAllAppList(id);
	}
	
	/**
	 * 获取天气数据
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/menu_main_weather.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getWeatherByCity(HttpSession session){
		Map<String,Object> tmp = weatherService.getWeatherData(session);
		//日期 星期 农历
    	Calendar calendar = Calendar.getInstance();
    	Date today = new Date();
		calendar.setTime(today);
    	Lunar lunar = new Lunar(calendar);
    	//加入日期
    	tmp.put("day", CommonUtil.format(today, "MM月dd日"));
    	tmp.put("week",lunar.dayNames[lunar.weekDay-1==-1?0:lunar.weekDay-1]);
    	tmp.put("lunar",Lunar.CHINESE_NUMBER[lunar.month - 1] + "月" + Lunar.getChinaDayString(lunar.day));
		return tmp;
	}

}
