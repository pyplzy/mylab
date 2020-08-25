package usi.dbdp.portal.util;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.*;
import org.springframework.stereotype.Component;
import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.entity.User;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebServiceUtils {
	/*日志*/
	private static Logger logger = LogManager.getLogger(WebServiceUtils.class.getName());


	private static final String CHECK_URL = ConfigUtil.getValue("person_url");
	private static final String CHECK_NAME_SPACE = ConfigUtil.getValue("person_namespace");
	private static final String CHECK_METHOD = ConfigUtil.getValue("person_method");
	// ============设置调用接口超时时长============
	private static final Integer TIME_OUT = Integer.parseInt(ConfigUtil.getValue("person_invokeTimeouts"));

	/**
	 * 
	 * @Title：invoke
	 * @Description：
	 * @return
	 * @throws
	 */
	public static String invoke(String param) {
		String result = null;
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTimeout(TIME_OUT);
			call.setTargetEndpointAddress(new URL(CHECK_URL));
			call.setUseSOAPAction(true);
			call.setOperationName(new QName(CHECK_NAME_SPACE, CHECK_METHOD));
			call.addParameter("data", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			logger.info("开始调用接口：" + CHECK_URL + ";入参：" + param);
			result = call.invoke(new Object[] { param }).toString();
			logger.info("调用接口结束：" + CHECK_URL + ";调用结果:" + result);
		} catch (Exception e) {
			logger.info("调用接口结束：" + CHECK_URL + ";调用结果:fail，" + e.getMessage());
			logger.error("调用接口异常:" + e.getMessage(), e);
		}
		return result;
	}
	/**
	 *
	 * @Title：invoke
	 * @Description：新增
	 * @return
	 * @throws
	 */
	public static boolean invokeAdd(User user) {
		String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<root><person>"
					+ "<personitem opt=add userid=\""+user.getLoginId()+"\" username=\""+user.getUserName()+"\" gender=\""+user.getGender()+"\" mobileno=\""+user.getMobileNo()+"\" pwd=\""+user.getPassword()+"/>"
					+ "</person></root>";
		Map<String, String> map = parseXml(invoke(param));
		if (map!=null&&map.size()>0){
			return map.get("result").equals("success");
		}else {
			return false;
		}
	}
	/**
	 *
	 * @Title：invoke
	 * @Description：修改密码
	 * @return
	 * @throws
	 */
	public static boolean invokeUpdate(User user,String newPassword) {
		String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<root><person>"
				+ "<personitem opt=update userid=\""+user.getLoginId()+"\" username=\""+user.getUserName()+"\" gender=\""+user.getGender()+"\" mobileno=\""+user.getMobileNo()+"\" pwd=\""+user.getPassword()+ "\" newpwd=\""+newPassword+"/>"
				+ "</person></root>";
		Map<String, String> map = parseXml(invoke(param));
		if (map!=null&&map.size()>0){
			return map.get("result").equals("success");
		}else {
			return false;
		}
	}
	/**
	 *
	 * @Title：invoke
	 * @Description：删除
	 * @return
	 * @throws
	 */
	public static boolean invokeDel(UserInfo user) {
		String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<root><person>"
				+ "<personitem opt=del userid=\""+user.getLoginId()+"\" username=\""+user.getUserName()+"\" gender=\""+user.getGender()+"\" mobileno=\""+user.getMobileNo()+"/>"
				+ "</person></root>";
		Map<String, String> map = parseXml(invoke(param));
		if (map!=null&&map.size()>0){
			return map.get("result").equals("success");
		}else {
			return false;
		}
	}
	/**
	 * 
	 * @Title：parseXml 
	 * @Description：解析调用结果并返回 
	 * @param ：
	 * @return：UicUserInfo 
	 * @throws
	 */
	public static Map<String, String> parseXml(String xml) {
		if (xml==null)return null;
		Map<String, String> map = new HashMap<String, String>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			Element responses = rootElt.element("responses");
			Element response = responses.element("response");
			List<Attribute> responseAttrs = response.attributes();
			for (Attribute attr : responseAttrs) {
				map.put(attr.getName(), attr.getValue());
//				System.out.println("属性名：" + attr.getName() + "--属性值：" + attr.getValue());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
