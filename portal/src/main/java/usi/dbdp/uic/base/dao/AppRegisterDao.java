package usi.dbdp.uic.base.dao;

import java.util.List;

import usi.dbdp.uic.dto.AppRegisterInfo;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.entity.AppRegister;

/**
 * @author zhqnie
 * @version 2015年9月6日 上午10:31:47
 * 说明
 */
public interface AppRegisterDao {

	/**
	 * 应用code 个数
	 * @param appCode
	 * @return 应用code 个数
	 */
	public int judgeAppByAppCode(String appCode);

	/**
	 * 增加一个应用
	 * @param AppRegister 应用信息对象
	 * @return 1表示操作成功，0表示操作失败
	 */
	public int addApp(AppRegister appRegister);

	/**
	 * 更新某应用信息
	 * @param AppRegister 应用信息对象
	 * @return 1表示操作成功，0表示操作失败
	 */
	public int updateApp(AppRegister appRegister);

	/**
	 * 根据应用appCode查应用信息
	 * @param appCode 应用code
	 * @return 应用信息对象
	 */
	public List<AppRegister> queryAppByAppCode(String appCode);

	/**
	 * 删除某应用（逻辑删除，设置应用信息状态为失效）
	 * @param appCode 应用code
	 * @return 1表示操作成功，0表示操作失败
	 */
	public int deleteAppByAppCode(String appCode);

	/**
	 * 激活某应用（将应用信息状态从失效状态改为生效）
	 * @param appCode 应用code
	 * @return 1表示操作成功，0表示操作失败
	 */
	public int activateAppByAppCode(String appCode);

	/**
	 * 查询所有激活的应用
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAppByState();

	/**
	 * 查询所有的应用
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAllAppList();

	/**
	 * @author  ma.guangming
	 * 查询应用级应用
	 * @return List<AppRegister>
	 */
	public List<AppRegister> getNomalAppsByState();

	/**
	 * 获取某用户拥有菜单的非系统级应用(分页)
	 * @param id 用户id
	 * @return 应用dto
	 */
	public List<AppRegisterInfo> getAllAppsByUserIdWithPage(
			PageObj pageObj, String userId);

	/**
	 * 查询所有的应用
	 * @param id 用户id 
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAllAppList(long id);

	/**
	 * 查询机构当前登录人查看符合权限的所有应用
	 * @param province
	 * @return
	 */
	public List<AppRegister> getAppCodes(long province);

}