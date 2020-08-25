package usi.dbdp.portal.sysmgr.dao;

import java.util.List;

import usi.dbdp.portal.dto.ChangeLogInfo;
import usi.dbdp.portal.dto.LoginLogInfo;
import usi.dbdp.portal.entity.ChangeLog;
import usi.dbdp.portal.entity.LoginLog;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午11:04:44
 * 说明
 */
public interface LogDao {

	/**
	 * 登录日志查询
	 * @param uicLoginLog 登录日志
	 * @param pageObj 分页对象
	 * @return 登录日志dto
	 */
	public List<LoginLogInfo> getUicLoginLogInfo(LoginLog uicLoginLog,
			PageObj pageObj);

	/**
	 * 信息变更日志查询
	 * @param uicChangeLogInfo 信息变更日志dto
	 * @param pageObj 分页对象
	 * @return 登录日志dto
	 */
	public List<ChangeLogInfo> getUicChangeLogInfo(
			ChangeLogInfo uicChangeLogInfo, PageObj pageObj);

	/**
	 * 保存登录日志
	 * @param loginLog 登录日志对象实体
	 * @return true 成功 false 失败
	 */
	public boolean saveLoginLogInfo(LoginLog loginLog);

	/**
	 * 保存信息变更日志
	 * @param changeLog 信息变更日志对象实体
	 * @return true 成功 false 失败
	 */
	public boolean saveChangeLogInfo(ChangeLog changeLog);
	/**
	 * 
	 * @Title: saveLogoutInfo
	 * @Author: zhang.rui
	 * @Date: 2018年8月2日上午10:58:47
	 * @Description: 保存登出信息
	 * @param userId
	 * @return
	 */
	public boolean saveLogoutInfo(String userId);
}