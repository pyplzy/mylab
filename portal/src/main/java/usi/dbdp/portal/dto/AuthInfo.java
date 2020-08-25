package usi.dbdp.portal.dto;



import java.util.List;

import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.uic.dto.AuthMenu;

/**
 * 登录信息
 * @author fan.fan
 * @date 2014-3-27 下午1:50:38
 */
public class AuthInfo {
	//菜单list
	private List<AuthMenu> authMenus;

	public List<AuthMenu> getAuthMenus() {
		return authMenus;
	}

	public void setAuthMenus(List<AuthMenu> authMenus) {
		this.authMenus = authMenus;
	}
	
	@Override
	public String toString() {
		String result = "";
		try {
			result = JacksonUtil.obj2json(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
