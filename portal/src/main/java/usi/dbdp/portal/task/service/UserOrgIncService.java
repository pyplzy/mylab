package usi.dbdp.portal.task.service;

 
import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
 


import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.task.dao.UserOrgIncDao;
import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.entity.User;

@Service
public class UserOrgIncService {
	
	@Resource
	private UserOrgIncDao userOrgIncDao;
	/**
	 * 根据工号获取用户信息(只查询用户表，不关联其他表，不考虑是否逻辑删除)
	 * @param id 用户主键
	 * @return 用户信息模型
	 */
	public User getUserByUserIdForInc(Long id){
		User user = null;
		try{
			user = userOrgIncDao.getUserByUserIdForInc(id);
		}catch(DataAccessException e){
			e.printStackTrace();
		}
		return user;
	};
	/**
	 * 人员变更信息插入用户信息增量表
 	* @param User
 	* @param incType
 	* @return void
	 */
	@Transactional(rollbackFor=Exception.class)
	public void insertIncUser(User user,String incType){
		userOrgIncDao.insertIncUser(user,incType);
	}
	/**
	 * 机构变更信息插入机构信息增量表
 	* @param org
 	* @param incType
 	* @return void
	 */
	@Transactional(rollbackFor=Exception.class)
	public void insertIncOrg(Org org,String incType){
		userOrgIncDao.insertIncOrg(org,incType);
	}
}
