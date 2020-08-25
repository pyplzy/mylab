package usi.dbdp.uic.a2a.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.uic.a2a.dao.AccountingDao;
import usi.dbdp.uic.a2a.service.AuthorizationService;
import usi.dbdp.uic.entity.User;
/**
 * 认证服务接口
 * @author ma.guangming
 *
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
	@Resource
	private AccountingDao accountingDao;
	/**
	 * 验证用户登录
	 * @param userId 用户名（登录名）
	 * @param password 登录密码
	 * @return Map
	 * map说明
	 * {rtCode:1,rtDesc:验证通过} {rtCode:2,rtDesc:用户名或密码错误} {rtCode:3,rtDesc:用户状态失效}
	 * {rtCode:4,rtDesc:用户已锁定} {rtCode:5,rtDesc:用户密码已过期} {rtCode:6,rtDesc:服务异常}
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, String> authorizeUser(String userId, String password) {
		Map<String, String> map=new HashMap<String, String>();
		try{
			/*
			 * 根据登录名获取用户信息
			 */
			User user=accountingDao.getUserByUserId(userId);
			//用户不存在
			if(null==user){
				map.put("rtCode", "2");
				map.put("rtDesc", "用户名或密码错误");
				return map;
			//用户密码错误
			}else if(!password.equals(user.getPassword())){
				map.put("rtCode", "2");
				map.put("rtDesc", "用户名或密码错误");
				return map;
			//用户名、密码正确，未删除
			}else if(0==user.getDelFlag()){
				map.put("rtCode", "1");
				map.put("rtDesc", "验证通过");
				return map;
			//状态为停用:密码错误次数大于x等于5次，锁定
			}else if(5<=user.getPwdErrCnt()){
				map.put("rtCode", "4");
				map.put("rtDesc", "用户已锁定");
				return map;
			//密码错误次数小于5次，判断是否密码过期
			}else if((new Date().getTime()-user.getPwdLastModTime().getTime())/(24*60*60*1000)>user.getDuration()){
				map.put("rtCode", "5");
				map.put("rtDesc", "用户密码已过期");
				return map;
			}else{
				map.put("rtCode", "3");
				map.put("rtDesc", "用户状态失效");
				return map;
			}
		}catch(Exception e){
			map.put("rtCode", "6");
			map.put("rtDesc", "服务异常");
			e.printStackTrace();
			return map;
		}
	}
	
}
