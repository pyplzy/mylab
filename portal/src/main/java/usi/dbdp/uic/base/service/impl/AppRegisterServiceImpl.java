package usi.dbdp.uic.base.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.uic.base.dao.AppRegisterDao;
import usi.dbdp.uic.base.service.AppRegisterService;
import usi.dbdp.uic.dto.AppRegisterInfo;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.entity.AppRegister;

/**
 * 应用注册服务
 * @author nie.zhengqian
 * 创建时间：2015年3月10日 下午5:15:40
 */
@Service
public class AppRegisterServiceImpl implements AppRegisterService{
	@Resource
	private AppRegisterDao appRegisterDao;
	/**
	 * 增加一个应用
	 * @param AppRegister 应用信息对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean addApp(AppRegister appRegister) {
		if(appRegisterDao.judgeAppByAppCode(appRegister.getAppCode())==0){
			return (appRegisterDao.addApp(appRegister)>0);
		}else{
			return false;
		}
	}

	/**
	 * 更新某应用信息
	 * @param AppRegister 应用信息对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateApp(AppRegister appRegister) {
		return (appRegisterDao.updateApp(appRegister)>0);
	}

	/**
	 * 根据应用appCode查应用信息
	 * @param appCode 应用code
	 * @return 应用信息对象
	 */
	@Override
	@Transactional(readOnly=true)
	public AppRegister queryAppByAppCode(String appCode) {
		List<AppRegister> appRegister =  appRegisterDao.queryAppByAppCode(appCode);
		if(appRegister.size()>0){
			return appRegister.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 删除某应用（逻辑删除，设置应用信息状态为失效）
	 * @param appCode 应用code
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean deleteAppByAppCode(String appCode) {
		return (appRegisterDao.deleteAppByAppCode(appCode)>0);
	}

	/**
	 * 激活某应用（将应用信息状态从失效状态改为生效）
	 * @param appCode 应用code
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean activateAppByAppCode(String appCode) {
		return (appRegisterDao.activateAppByAppCode(appCode)>0);
	}

	/**
	 * 判断应用code 是否可用
	 * @param appCode 应用code
	 * @return true表示操作可用，false表示重复不可用
	 */
	@Override 
	@Transactional(readOnly=true)
	public boolean judgeAppByAppCode(String appCode) {
		return (appRegisterDao.judgeAppByAppCode(appCode)==0);
	}

	/**
	 * 查询所有激活的应用
	 * @return List<AppRegister>
	 */
	@Override
	public List<AppRegister> queryAppByState() {
		return appRegisterDao.queryAppByState();
	}
	/**
	 * 查询所有的应用
	 * @return List<AppRegister>
	 */
	@Override
	public List<AppRegister> queryAllAppList() {
		return appRegisterDao.queryAllAppList();
	}
	/**
	 * 查询应用级应用
	 * @return List<AppRegister>
	 * @author  ma.guangming
	 */
	@Override
	@Transactional(readOnly=true)
	public List<AppRegister> getNomalAppsByState(){
		return appRegisterDao.getNomalAppsByState();
	}
	
	/**
	 * 获取某用户拥有菜单的非系统级应用(分页)
	 * @param id 用户id
	 * @return 应用dto
	 */
	public List<AppRegisterInfo> getAllAppsByUserIdWithPage(PageObj pageObj , String  userId){
		return appRegisterDao.getAllAppsByUserIdWithPage(pageObj, userId);
	}
	
	/**
	 * 查询所有的应用
	 * @param id 用户id 
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAllAppList(long id) {
		return appRegisterDao.queryAllAppList(id);
	}

	/**
	 * 查询机构当前登录人查看符合权限的所有应用
	 * @param province
	 * @return
	 */
	public List<AppRegister> getAppCodes(long province) {
		return appRegisterDao.getAppCodes(province);
	}
	
}
