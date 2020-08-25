package usi.dbdp.portal.common.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import usi.dbdp.portal.dto.AuthInfo;
import usi.dbdp.portal.entity.LoginLog;
import usi.dbdp.portal.sysmgr.service.LogService;
import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.IpAddressUtil;
import usi.dbdp.uic.a2a.service.AccountingService;
import usi.dbdp.uic.a2a.service.AuthenticationService;
import usi.dbdp.uic.dto.AuthMenu;
import usi.dbdp.uic.dto.UserInfo4Session;

/**
 * 用于展示菜单
 * @author lmwang
 * 创建时间：2015-3-25 上午9:15:13
 */
@Controller
public class HomeController {
	
	@Resource
	private AccountingService accountingService;
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private LogService logService;
	
	/**
	 * 取得用户信息，放入session，并获取菜单，在框架展示
	 * @param userId 用户登录id
	 * @param session session里存放一些常用信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	private String home(HttpServletRequest request, HttpServletResponse response,Model model) {
		
		//获取session
		HttpSession session = request.getSession(false);
		//如果没有初始化过session信息，说明是初次访问，获取session信息，记录登录日志
		if(null==request.getSession().getAttribute("loginId")) {
			//登录id，稍后从cas中获取（-1没有实际意义，防止空指针）
			String loginId ="-1";
			//通过下面对象获取登录id
			Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
			if (null != assertion) {
				AttributePrincipal principal = assertion.getPrincipal();
				if (null != principal) {
					loginId = principal.getName();
				}
			}
			//获取user信息
			UserInfo4Session userInfo4Session = accountingService.getUserInfo4SessionByLoginId(loginId);
			session.setAttribute("loginId", loginId);
			session.setAttribute("staffId", userInfo4Session.getId());
			//session核心属性，不能删除
			session.setAttribute("userId", userInfo4Session.getUserId());
			session.setAttribute("userName", userInfo4Session.getUserName());
			session.setAttribute("orgId", userInfo4Session.getOrgId());
			session.setAttribute("orgName", userInfo4Session.getOrgName());	
			session.setAttribute("province", userInfo4Session.getProvince());//省id
			session.setAttribute("city", userInfo4Session.getCity());//城市id，用于天气获取城市名称拼音和或获取区域编码
			session.setAttribute("ip", IpAddressUtil.getReqIp(request));//设置ip用于日志
			//记录登录日志（由于与cas交互存在二次重定向，为了避免登录日志重复登录，判断session信息）
			LoginLog loginLog = new LoginLog();
			loginLog.setUserId(session.getAttribute("userId").toString());
			loginLog.setLoginIp((String) session.getAttribute("ip"));
			loginLog.setSessionId(session.getId());
			loginLog.setIsSuccess(LogService.LOGIN_SUCCESS);
			logService.saveLoginLogInfo(loginLog);
		}
		
		//获取所有的菜单
		List<AuthMenu> menusLst = authenticationService.getAllMenusByUserIdInApp(ConfigUtil.getValue("appCode"), (Long)session.getAttribute("staffId"));
		AuthInfo authInfoOnlyMenus = new AuthInfo();
		//存放叶子菜单的map（放在session里，做菜单权限校验）
		Map<String,String> leafMenusMap = new HashMap<String,String>();
		//组织成父子结构方便框架展示，并把叶子菜单组织好，以方便放到session里，做菜单权限校验
		authInfoOnlyMenus.setAuthMenus(convertMenusFormat(menusLst,leafMenusMap));
		//叶子菜单放到session里
		session.setAttribute(ConfigUtil.getValue("menuKey"), leafMenusMap);
		//菜单
		model.addAttribute("menus", authInfoOnlyMenus);
		//从配置文件获取默认的一级菜单个数
		model.addAttribute("lv1MenuNum", ConfigUtil.getValue("lv1MenuNum"));
		
		return "portal/index";
	}
	
	/**
	 * 安全退出，销毁session
	 * @param session
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session,String casUrl) {
		String userId=session.getAttribute("userId").toString();
		try {
			session.invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logService.saveLogoutInfo(userId);//更新日志
		return "redirect:"+casUrl;
	}
	/**
	 * 菜单封装成父子结构（并把叶子菜单放到map里）
	 * @param menusLst 菜单列表
	 * @return 菜单列表（父子结构）
	 */
	private static List<AuthMenu> convertMenusFormat(List<AuthMenu> menusLst,Map<String,String> leafMenusMap){
		AuthMenu pMenu = new AuthMenu();
		pMenu.setMenuId(0);
		pMenu.setParentId(-1);
		menusLst.add(pMenu);
		for(AuthMenu menu: menusLst) {
			//如果是叶子菜单，放到map里
			if(menu.getIsLeaf()==1) {
				leafMenusMap.put(menu.getMenuAction(), "1");
			}
			for(AuthMenu tmpMenu: menusLst) {
				if(tmpMenu.getParentId() == menu.getMenuId()) {
					menu.getChildren().add(tmpMenu);
				}
			}
		}
		return pMenu.getChildren();
	}

}
