package usi.dbdp.uic.base.service;

import java.util.List;

import usi.dbdp.uic.dto.AppRegisterInfo;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.entity.AppRegister;

/**
 * 应用注册服务接口
 * @author nie.zhengqian
 * 创建时间：2015年3月10日 下午4:54:08
 */

public interface AppRegisterService {
	
	/**
	 * 增加一个应用
	 * @param appRegister 应用信息对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addApp(AppRegister appRegister);
	
	/**
	 * 更新某应用信息
	 * @param appRegister 应用信息对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateApp(AppRegister appRegister);
	
	/**
	 * 根据应用appCode查应用信息
	 * @param appCode 应用code
	 * @return 应用信息对象
	 */
	public AppRegister queryAppByAppCode(String appCode);
	
	/**
	 * 删除某应用（逻辑删除，设置应用信息状态为失效）
	 * @param appCode 应用code
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean deleteAppByAppCode(String appCode);
	
	/**
	 * 激活某应用（将应用信息状态从失效状态改为生效）
	 * @param appCode 应用code
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean activateAppByAppCode(String appCode);
	
	/**
	 * 判断应用code 是否可用
	 * @param appCode 应用code
	 * @return true表示操作可用，false表示重复不可用
	 */
	public boolean judgeAppByAppCode(String appCode);
	
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
	 * 查询应用级应用
	 * @return List<AppRegister>
	 */
	public List<AppRegister> getNomalAppsByState();
	
	/**
	 * 获取某用户拥有菜单的非系统级应用(分页)
	 * @param id 用户id
	 * @return 应用dto
	 */
	public List<AppRegisterInfo> getAllAppsByUserIdWithPage(PageObj pageObj , String  userId);
	
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
