package usi.dbdp.uic.a2a.dao;

import java.util.List;

import usi.dbdp.uic.dto.UserInfo4Session;
import usi.dbdp.uic.entity.User;

/**
 * @author zhqnie
 * @version 2015年9月6日 上午10:01:13
 * 说明
 */
public interface AccountingDao {

	/**
	 * 设置用户状态为停用状态
	 * @param userId 工号
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean setUserStopByUserId(String userId);

	/**
	 * 增加用户
	 * @param user 用户信息模型
	 * @return true表示操作成功，false表示操作失败
	 */
	public long addUser(User user);

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
	public boolean updateUserPasswordByLoginId(String loginId,	String oldPassword, String newPassword);	

	/**
	 * 根据工号获取用户信息
	 * @param userId 工号
	 * @return 用户信息模型
	 */
	public User getUserByUserId(String userId);

	/**
	 * 根据登录号获取用户信息
	 * @param loginId 登录号
	 * @return 用户信息模型
	 */
	public User getUserByLoginId(String loginId);
	

	/**
	 * 重置密码
	 * @param loginId 登录号
	 * @param password 初始密码
	 * @return true 成功 false 失败
	 */
	public boolean resetPasswordByLoginId(String loginId, String password);
	/**
	 * 
	 * @Title: updateUserStatus
	 * @Author: zhang.rui
	 * @Date: 2018年8月3日上午9:55:28
	 * @Description:更新用户状态（锁定:2/解锁:0）
	 * @param loginId
	 * @param status
	 * @return
	 */
	public boolean updateUserStatus(String loginId, String status);
	/**
	 * 根据登录号获取用户信息，具体见UserInfo4Session
	 * @param loginId 登录号
	 * @return UserInfo4Session 包含用户信息
	 */
	public UserInfo4Session getUserInfo4SessionByLoginId(String loginId);
	/**
	 * 判断userId是否可用
	 * @param userId 工号
	 * @return true 可用  false 不可用
	 */
	
	public int checkUserIdInUse(String userId);
	
	/**
	 * 判断loginId是否可用
	 * @param loginId 登录号
	 * @return true 可用  false 不可用
	 */
	public int checkLoginIdInUse(String loginId);	

	/**
	 * 删除角色用户管理表
	 * @param userId 工号
	 */
	public void delUserRoleRel(String userId);

	/**
	 * 用户表userId 变为 id+userid
	 * @param userId 工号
	 */
	public void updateUserIdByUserId(String userId);

	public void updateUserHasOrg(User user, long orgId);

	public void delUserOrgRel(String userId, int type);

	public boolean delUserHasOrg(long id, long orgId);

	public boolean insertUserHaOrg(long id, long orgId);

	public void setUserHadRole(long id);
	
	/**   
	* @Title: getAllUser    
	* @Description: 获取所有的用户数据
	* @return
	* @author 
	* @Date   2017年2月8日   
	*/
	public List<User> getAllUser();
}