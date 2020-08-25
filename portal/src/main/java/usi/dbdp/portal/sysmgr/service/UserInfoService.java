package usi.dbdp.portal.sysmgr.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.uic.a2a.service.AccountingService;
import usi.dbdp.uic.entity.User;

/**
 * 个人信息
 * @author ma.guangming
 *
 */
@Service
public class UserInfoService  {
	@Resource
	private AccountingService accountingService;
	/**
	 * 根据用户名（登录名）获取用户信息
	 * @param userId 用户名（登录名）
	 * @return User用户信息模型 
	 */
	@Transactional(readOnly=true)
	public User getUserByUserId(String userId){
		return accountingService.getUserByUserId(userId);
	}
	/**
	 * 修改个人信息
	 * @param user 用户对象
	 * @return true 成功 false 失败
	 */
	@Transactional(rollbackFor=Exception.class)
	public boolean updateUser(User user){
		return accountingService.updateUser(user);
	}
	
	
	
}
