package usi.dbdp.uic.a2a.service;


import javax.servlet.http.HttpSession;

import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.dto.UserInfo4Session;
import usi.dbdp.uic.entity.User;


/**
 * 用户管理服务接口
 * @author lmwang
 * 创建时间：2015-2-5 下午3:48:39
 */
public interface AccountingService {
	
	/**
	 * 删除用户
	 * @param userId 工号
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean deleteUserByUserId(UserInfo user);
	
	/**
	 * 增加用户
	 * @param user 用户信息模型 
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addUser(User user);
	/**
	 * 更新用户
	 * @param user 用户信息模型 
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateUser(User user);

	/**
	 * 根据原密码更新用户密码
	 * @param loginId 登录号
	 * @param oldPassword 原密码
	 * @param newPassword 新密码
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateUserPasswordByLoginId(String loginId,String oldPassword,String newPassword);
	/**
	 * 根据工号获取用户信息
	 * @param userId 工号
	 * @return User用户信息模型 
	 */
	public User getUserByUserId(String userId);
	
	/**
	 * 根据登录号获取用户信息
	 * @param loginId 登录号
	 * @return User用户信息模型 
	 */
	public User getUserByLoginId(String loginId);	
	
	/**
	 * 重置密码
	 * @param loginId 登录号
	 * @return true 成功 false 失败
	 */
	public boolean resetPasswordByLoginId(String loginId,String newPassword);
	
	/**
	 * 根据登录号获取用户信息，具体见UserInfo4Session
	 * @param loginId 登录号
	 * @return
	 */
	public UserInfo4Session getUserInfo4SessionByLoginId(String loginId);
	/**
	 * 判断userId是否可用
	 * @param userId 工号
	 * @return true 可用  false 不可用 
	 */
	public boolean checkUserIdInUse(String userId);
	/**
	 * 判断loginId是否可用
	 * @param loginId 登录号
	 * @return true 可用  false 不可用
	 */
	public boolean checkLoginIdInUse(String loginId);
	/**
	 * 
	 * @Title: updateUserStatus
	 * @Author: zhang.rui
	 * @Date: 2018年8月3日上午9:52:22
	 * @Description: 更新用户状态（锁定:2/解锁:0）
	 * @param loginId
	 * @param status
	 * @return
	 */
	public boolean updateUserStatus(String loginId,String status);
	
}
