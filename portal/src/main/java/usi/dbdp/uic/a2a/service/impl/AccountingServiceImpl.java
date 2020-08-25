package usi.dbdp.uic.a2a.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.task.service.UserOrgIncService;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.WebServiceUtils;
import usi.dbdp.uic.a2a.dao.AccountingDao;
import usi.dbdp.uic.a2a.service.AccountingService;
import usi.dbdp.uic.base.dao.OrgDao;
import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.dto.UserInfo4Session;
import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.entity.User;
/**
 *  用户管理服务接口
 * @author ma.guangming
 *
 */	
@Service
public class AccountingServiceImpl implements AccountingService {
	/*日志*/
	private final static Logger logger = Logger.getLogger(AccountingServiceImpl.class);
	@Resource
	private AccountingDao accountingDao;
	@Resource
	private OrgDao orgDao;
	@Resource
	UserOrgIncService userOrgIncService;
	/**
	 * 删除用户
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean deleteUserByUserId(UserInfo user) {
		boolean result = WebServiceUtils.invokeDel(user);
		if(result==false){
			throw new RuntimeException("信息同步失败!");
		}
		if(user.getType()==1){
			//删除角色用户关联表
			accountingDao.delUserRoleRel(user.getUserId());
			//删除人员和机构的关联关系 
//			accountingDao.delUserOrgRel(user.getId(),user.getType());
			return accountingDao.setUserStopByUserId(user.getUserId());
		}else{
			return accountingDao.delUserHasOrg(user.getId(),user.getOrgId());
		}
	}
	
	
	/**
	 * 增加用户
	 * @param user 用户信息模型 
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean addUser(User user) {
		boolean result = WebServiceUtils.invokeAdd(user);
		if(result==false){
			logger.info("接口同步人员信息失败");
			return false;
		}
		//新增行政归属用户  先插入用户表 返回id 
		long id = accountingDao.addUser(user);
		//设置默认角色
		accountingDao.setUserHadRole(id);
		
		//增量人员，插入人员增量数据表
		User userForInc=userOrgIncService.getUserByUserIdForInc(id);
		userOrgIncService.insertIncUser(userForInc,"A");
		//再插入uic_user_has_org表
		return accountingDao.insertUserHaOrg(id,user.getOrgId());
	}
	/**
	 * 更新用户
	 * @param user 用户信息模型 
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateUser(User user) {
		User user2 = getUserByLoginId(user.getLoginId());
		long orgIdNew = user.getOrgId();
		long orgIdOld = user2.getOrgId();
		if(orgIdNew!=orgIdOld){  //  判断机构修改机构 
			accountingDao.updateUserHasOrg(user,orgIdOld);
		}
		return accountingDao.updateUser(user);
	}
	
	
	/**
	 * 根据原密码更新用户密码
	 * @param loginId 登录号
	 * @param oldPassword 原密码
	 * @param newPassword 新密码
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateUserPasswordByLoginId(String loginId,String oldPassword, String newPassword) {
		boolean flag= false;
		User user=accountingDao.getUserByLoginId(loginId);
		boolean result = WebServiceUtils.invokeUpdate(user,newPassword);
		if(result==false){
			throw new RuntimeException("信息同步失败!");
		}
		if(user !=null && user.getPassword().equals(oldPassword)){
			flag = accountingDao.updateUserPasswordByLoginId(loginId, oldPassword, newPassword);
		}
		return flag;
	}
	
	/**
	 * 根据工号获取用户信息
	 * @param userId 工号
	 * @return User用户信息模型 
	 */
	@Override
	@Transactional(readOnly = true)
	public User getUserByUserId(String userId) {
		User user = null;
		try{
			user = accountingDao.getUserByUserId(userId);
		}catch(DataAccessException e){
			e.printStackTrace();
			throw e ;
		}
		return user;
	}
	
	/**
	 * 根据登录号获取用户信息
	 * @param loginId 登录号
	 * @return User用户信息模型 
	 */
	@Override
	@Transactional(readOnly = true)
	public User getUserByLoginId(String loginId) {
		User user = null;
		try{
			user = accountingDao.getUserByLoginId(loginId);
		}catch(DataAccessException e){
			e.printStackTrace();
			throw e ;
		}
		return user;
	}	
	

	
	/**
	 * 重置密码
	 * @param loginId 登录号
	 * @return true 成功 false 失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean resetPasswordByLoginId(String loginId,String newPassword) {
//		String password = CommonUtil.getMd5(loginId+"000000");
		//初始密码userId+"000000"  md5加密
		User user=accountingDao.getUserByLoginId(loginId);
		boolean result = WebServiceUtils.invokeUpdate(user,newPassword);
		if(result==false){
			throw new RuntimeException("信息同步失败!");
		}
		return accountingDao.resetPasswordByLoginId(loginId, newPassword);
	}
	
	/**
	 * 根据登录号获取用户信息，具体见UserInfo4Session
	 * @param loginId 登录号
	 * @return UserInfo4Session 包含用户信息
	 */
	@Transactional(readOnly = true)
	@Override
	public UserInfo4Session getUserInfo4SessionByLoginId(String loginId) {
		UserInfo4Session userInfo4Session = accountingDao.getUserInfo4SessionByLoginId(loginId);
		if(userInfo4Session != null){
			List<Org> parentOrgs = orgDao.getParentOrgsByOrgIdSeq(userInfo4Session.getOrgIdSeq());
			//parentOrgs不会为空，所以
			int lenOfOrgs = parentOrgs.size();
			//不是直接挂在根机构下的人
			if(lenOfOrgs>1) {
				for(int i = 0;i<lenOfOrgs;i++) {
					long orgId = parentOrgs.get(i).getOrgId();
					if(i==0) {
						//设置根机构(这个时候是一级机构)
						userInfo4Session.setRootOgrId(orgId);
					}
					if(i==1) {
						//需要截取，所以改成字符串
						String orgIdStr = String.valueOf(orgId);
						//设置sharding_id（二级机构）
						userInfo4Session.setShardingId(Integer.parseInt(orgIdStr));
						//userInfo4Session.setShardingId(Integer.parseInt(orgIdStr.substring(0, 2)));
						//是行政机构设置省
//						if(parentOrgs.get(i).getIsAdministrative()==1) {
							userInfo4Session.setProvince(orgId);
							//如果二级机构是非行政机构，跳出循环，即省、市、县为空
//						}else {
//							break;
//						}
					}
					//设置市
					if(i==2) {//&&parentOrgs.get(i).getIsAdministrative()==1
						userInfo4Session.setCity(orgId);
					}
					//设置县
					if(i==3) {//&&parentOrgs.get(i).getIsAdministrative()==1
						userInfo4Session.setCounty(orgId);
					}
				}
				//直接挂在根机构下的人截取根机构id的前两位作为sharding_id
			}
//			else {
//				//取跟机构id
//				String rootOrgIdStr = String.valueOf(parentOrgs.get(0).getOrgId());
//				//设置rootOrgId
//				userInfo4Session.setRootOgrId(Long.parseLong(rootOrgIdStr));
//				//截取前两位作为sharding_id
//				userInfo4Session.setShardingId(Integer.parseInt(rootOrgIdStr.substring(0, 2)));
//			}
		}
		return userInfo4Session;
	}
	/**
	 * 判断userId工号是否可用
	 * @param userId 工号
	 * @return true 可用  false 不可用
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean checkUserIdInUse(String userId) {
		return (accountingDao.checkUserIdInUse(userId)==0);
	}
	/**
	 * 判断loginId登录号是否可用
	 * @param loginId 登录号
	 * @return true 可用  false 不可用
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean checkLoginIdInUse(String loginId) {
		return (accountingDao.checkLoginIdInUse(loginId)==0);
	}

	/**
	 * 更新用户状态（锁定:2/解锁:0）
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateUserStatus(String loginId, String status) {
		return accountingDao.updateUserStatus(loginId, status); 
	}

}
