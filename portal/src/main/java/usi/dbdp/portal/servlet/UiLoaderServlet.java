package usi.dbdp.portal.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 配置在web.xml里用于启动的时候，从配置文件读取样式，决定系统样式
 * @author lmwang
 *
 */
public class UiLoaderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 实现接口中的方法
	 */
	@Override
	public void init() throws ServletException {
		Map<String, String> uiInfo = new HashMap<String, String>();
		InputStream is = null;
		try {

			/*
			 * 读取ui配置信息到servletContext
			 */
			String path = this.getServletConfig().getInitParameter("uiConfigLocation").substring("classpath:".length());
			SAXReader reader = new SAXReader();
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			Document doc = reader.read(is);
			Element application = (Element) doc.selectObject("/app");
			
			uiInfo.put("theme", application.attributeValue("theme"));
			uiInfo.put("title", application.attributeValue("title"));
			uiInfo.put("Copyright", application.attributeValue("Copyright"));
			
			this.getServletContext().setAttribute("uiInfo", uiInfo);
		} catch (Exception e) {
			/*
			 * 配置文件如果解析失败，设置默认信息
			 */
			e.printStackTrace();
			uiInfo.put("theme", "blue");
			uiInfo.put("title", "首页");
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
