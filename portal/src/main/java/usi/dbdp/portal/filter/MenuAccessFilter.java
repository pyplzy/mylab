package usi.dbdp.portal.filter;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.GlobalApplicationContextHolder;
import usi.dbdp.portal.util.IpAddressUtil;
import usi.dbdp.uic.a2a.service.AccountingService;
import usi.dbdp.uic.a2a.service.AuthenticationService;
import usi.dbdp.uic.dto.AuthMenu;
import usi.dbdp.uic.dto.UserInfo4Session;

/**
 * 菜单访问过滤器（如果当前访问是get请求，且以*Menu.do结尾[约定]，说明是菜单，此时与session中的登录人拥有的叶子菜单做比较）。注：此为粗粒度，细粒度应该某菜单里涉及的url都不让访问
 * 备注：约定所有菜单都以*Menu.do结尾
 * @author lmwang
 * 创建时间：2014-12-15 下午2:26:17
 */
public class MenuAccessFilter implements Filter{
	
	//日志
	private static final Logger LOGGER = Logger.getLogger(MenuAccessFilter.class);
	
	private ServletContext servletContext;
	
	private static final AccountingService ACCOUNTING_SERVICE = GlobalApplicationContextHolder.getApplicationContext().getBean(AccountingService.class);
	private static final AuthenticationService AUTHENTICATION_SERVICE = GlobalApplicationContextHolder.getApplicationContext().getBean(AuthenticationService.class);

	private static final String MENU_FILTER = "menuFilter";
	/**
	 * 初始化时，设置context，后来会用来取全局变量
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.servletContext = filterConfig.getServletContext();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		//取session中的userId
		String userId = (String)request.getSession().getAttribute("userId");
		//是否需要初始化应用数据到session
		initAppSession(userId,request);
		//在web.xml中配置的初始化参数
		String menuFilterInitParameter = servletContext.getInitParameter(getMenuFilter());
		if("true".equals(menuFilterInitParameter)) {
			//如果是get请求
			if("GET".equals(request.getMethod())) {
				//请求的servlet路径例如/a/b.do
				String servletPath = request.getServletPath();
				//是菜单
				if(servletPath.indexOf("Menu.do")>-1) {
					//问号后面都去掉
					if(servletPath.indexOf("?")>-1) {
						servletPath = servletPath.substring(0,servletPath.indexOf("?"));
					}
					//session里保存的叶子菜单map
					@SuppressWarnings("unchecked")
					Map<String,String> leafMenusMap = (Map<String,String>)request.getSession().getAttribute(ConfigUtil.getValue("menuKey"));
					if(leafMenusMap!=null) {
						//是否是自己的菜单
						if(!leafMenusMap.containsKey(servletPath)) {
							LOGGER.info("非法访问菜单【无权限】");
							//直接设置404
							response.setStatus(HttpServletResponse.SC_NOT_FOUND);
							request.getRequestDispatcher("/404.htm").forward(req,res);
				        //有权限
						}else {
							chain.doFilter(req, res);
						}
			        //取不到
					}else {
						LOGGER.info("没有配置角色。。。");
						chain.doFilter(req, res);
					}
					
					//非菜单url，放过
					}else {
						chain.doFilter(req, res);
					}
			//非get请求
			}else {
				chain.doFilter(req, res);
			}
		//未开启过滤，放过	
		}else {
			chain.doFilter(req, res);
		}

	}
	/**
	 * 销毁时置空
	 */
	@Override
	public void destroy() {
		this.servletContext = null; 
	}

	/**
	 * 获取web.xml中配置的menufilter参数的名字
	 * @return
	 */
	private static String getMenuFilter() {
		return MENU_FILTER;
	}
	/**
	 * 尝试将应用数据填充到session里
	 * @param userId 从session里取的userId
	 * @param request 
	 */
	private static void initAppSession(String userId,HttpServletRequest request) {
		//session超时需要在cas过滤器之后重新生成session里的东西
		if(userId==null) {
				//获取session
				HttpSession session = request.getSession(false);
				//通过下面对象获取登录id
				Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
				if (null != assertion) {
					AttributePrincipal principal = assertion.getPrincipal();
					if (null != principal) {
						//获取user信息
						UserInfo4Session userInfo4Session = ACCOUNTING_SERVICE.getUserInfo4SessionByLoginId(principal.getName());//principal.getName()获取loginId
						session.setAttribute("loginId", principal.getName());
						session.setAttribute("staffId", userInfo4Session.getId());
						//session核心属性，不能删除
						session.setAttribute("userId", userInfo4Session.getUserId());
						session.setAttribute("userName", userInfo4Session.getUserName());
						session.setAttribute("orgId", userInfo4Session.getOrgId());
						session.setAttribute("orgName", userInfo4Session.getOrgName());	
						session.setAttribute("province", userInfo4Session.getProvince());//省id
						session.setAttribute("city", userInfo4Session.getCity());//城市id，用于获取城市名称拼音
						session.setAttribute("ip", IpAddressUtil.getReqIp(request));//设置ip用于日志
						
						//获取所有的菜单
						List<AuthMenu> menusLst = AUTHENTICATION_SERVICE.getAllMenusByUserIdInApp(ConfigUtil.getValue("appCode"), userInfo4Session.getId());
						//存放叶子菜单的map（放在session里，做菜单权限校验）
						Map<String,String> leafMenusMap = new HashMap<String,String>();
						//组织成父子结构方便框架展示，并把叶子菜单组织好，以方便放到session里，做菜单权限校验
						extractLeafMenus(menusLst,leafMenusMap);
						//叶子菜单放到session里
						session.setAttribute(ConfigUtil.getValue("menuKey"), leafMenusMap);
					}
				}
				
		}
	}
	/**
	 * 处理叶子菜单
	 * @param menusLst 菜单列表
	 * @return 才对那列表
	 */
	private static void extractLeafMenus(List<AuthMenu> menusLst,Map<String,String> leafMenusMap){
		for(AuthMenu menu: menusLst) {
			//如果是叶子菜单，放到map里
			if(menu.getIsLeaf()==1) {
				leafMenusMap.put(menu.getMenuAction(), "1");
			}
		}
	}
	
}
