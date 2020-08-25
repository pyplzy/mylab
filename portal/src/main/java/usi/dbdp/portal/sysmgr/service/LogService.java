package usi.dbdp.portal.sysmgr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.dto.ChangeLogInfo;
import usi.dbdp.portal.dto.LoginLogInfo;
import usi.dbdp.portal.entity.ChangeLog;
import usi.dbdp.portal.entity.LoginLog;
import usi.dbdp.portal.sysmgr.dao.LogDao;
import usi.dbdp.uic.dto.PageObj;

/**
 * 操作日志service实现
 * @author ma.guangming
 *
 */
@Service
public class LogService    {
	
	//插入操作
	public static final String OPT_INSERT = "INSERT";
	//更新操作
	public static final String OPT_UPDATE = "UPDATE";
	//删除操作
	public static final String OPT_DELETE = "DELETE";
	//登录成功
	public static final int LOGIN_SUCCESS = 1;
	//登录失败
	public static final int LOGIN_FAIL = 0;
	@Resource
	private LogDao logDao;
	/**
	 * 登录日志查询
	 * @param loginLog 登录日志
	 * @param pageObj 分页对象
	 * @return 登录日志dto列表
	 */
	@Transactional(readOnly=true)
	public Map<String , Object > getUicLoginLogInfo(LoginLog loginLog ,PageObj pageObj ){
		List<LoginLogInfo> loginLogInfos= null ;
		Map<String , Object > map =new HashMap<String , Object >();
		try{
			loginLogInfos = logDao.getUicLoginLogInfo(loginLog, pageObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("loginLogs",loginLogInfos);
		map.put("pageObj",pageObj);
		return  map;
	}
	/**
	 * 信息变更日志查询
	 * @param changeLogInfo 信息变更日志dto
	 * @param pageObj 分页对象
	 * @return 信息变更日志dto列表
	 */
	@Transactional(readOnly=true)
	public Map<String , Object > getUicChangeLogInfo(ChangeLogInfo changeLogInfo, PageObj pageObj) {
		List<ChangeLogInfo> changeLogInfos= null ;
		Map<String , Object > map =new HashMap<String , Object >();
		try{
			changeLogInfos = logDao.getUicChangeLogInfo(changeLogInfo, pageObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("changeLogs",changeLogInfos);
		map.put("pageObj",pageObj);
		return map;
	}
	/**
	 * 保存登录日志
	 * @param loginLog 登录日志实体
	 * @return true 成功 false 失败
	 */
	@Transactional(rollbackFor=Exception.class)
	public boolean saveLoginLogInfo(LoginLog loginLog){
		return logDao.saveLoginLogInfo(loginLog);
	}
	/**
	 * 保存信息变更日志
	 * @param changeLog 信息变更日志实体
	 * @return true 成功 false 失败
	 */
	@Transactional(rollbackFor=Exception.class)
	public boolean saveChangeLogInfo(ChangeLog changeLog){
		return logDao.saveChangeLogInfo(changeLog);
	}
	/**
	 * @Title: saveLogoutInfo
	 * @Author: zhang.rui
	 * @Date: 2018年8月2日上午10:58:02
	 * @Description: 保存登出信息
	 * @param userId
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public boolean saveLogoutInfo(String userId){
		return logDao.saveLogoutInfo(userId);
	}
	
}
