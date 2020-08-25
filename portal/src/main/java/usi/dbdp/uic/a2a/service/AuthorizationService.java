package usi.dbdp.uic.a2a.service;

import java.util.Map;

/**
 * 认证服务接口
 * @author lmwang
 * 创建时间：2015-2-5 下午4:08:38
 */
public interface AuthorizationService {

	/**
	 * 验证用户登录
	 * @param userId 用户名（登录名）
	 * @param password 登录密码
	 * @return Map
	 * map说明
	 * {rtCode:1,rtDesc:验证通过} {rtCode:2,rtDesc:用户名或密码错误} {rtCode:3,rtDesc:用户状态失效}
	 * {rtCode:4,rtDesc:用户已锁定} {rtCode:5,rtDesc:用户密码已过期} {rtCode:6,rtDesc:服务异常}
	 */
	public Map<String,String> authorizeUser(String userId,String password);
	
}
