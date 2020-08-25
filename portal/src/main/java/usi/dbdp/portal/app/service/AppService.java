package usi.dbdp.portal.app.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import usi.dbdp.uic.base.service.AppRegisterService;
import usi.dbdp.uic.dto.AppRegisterInfo;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.entity.AppRegister;


/**
 * 应用Service
 * @author nie.zhengqian
 * 创建时间：2015年3月26日 下午1:30:15
 */
@Service
public class AppService {
	@Resource
	private AppRegisterService appRegisterService;
	/**
	 * 增加一个应用
	 * @param appRegister 应用信息对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addApp(AppRegister appRegister){
		return appRegisterService.addApp(appRegister);
	}
	
	/**
	 * 更新某应用信息
	 * @param appRegister 应用信息对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateApp( AppRegister appRegister){
		return appRegisterService.updateApp(appRegister);
	}
	
	/**
	 * 查询所有的应用
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAppByState() {
		return appRegisterService.queryAllAppList();
	}
	
	/**
	 * 查询所有的应用2
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAllAppList(long id) {
		return appRegisterService.queryAllAppList(id);
	}
	
	/**
	 * 删除某应用（逻辑删除，设置应用信息状态为失效）
	 * @param appCode 应用code
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean deleteAppByAppCode(String appCode){
		return appRegisterService.deleteAppByAppCode(appCode);
	}
	
	/**
	 * 激活某应用（将应用信息状态从失效状态改为生效）
	 * @param appCode 应用code
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean activateAppByAppCode(String appCode){
		return appRegisterService.activateAppByAppCode(appCode);
	}
	
	/**
	 * 判断应用code 是否可用
	 * @param appCode 应用code
	 * @return true表示操作可用，false表示重复不可用
	 */
	public boolean judgeAppByAppCode(String appCode){
		return appRegisterService.judgeAppByAppCode(appCode);
	}
	/**
	 * 获取某用户拥有菜单的非系统级应用
	 * @param id 用户id
	 * @return 
	 */
	public List<AppRegisterInfo> getAllAppsByUserIdWithPage(PageObj pageObj , String  userId){
		List<AppRegisterInfo> ars= appRegisterService.getAllAppsByUserIdWithPage(pageObj , userId);
//		for(AppRegisterInfo ar : ars){
//			String toUrl=ar.getTodoUrl();
//			Map<String , String > map=new HashMap<String , String>();
//			map.put("type", "0");
//			map.put("userId", userId);
//			String json="";
//			String result="";
//			try {
//				json = JacksonUtil.obj2json(map);
//				result=HttpclientUtil.sendRequestByPost(toUrl, json);
//				if(JacksonUtil.json2map(result).get("total")!=null){
//					ar.setNum(Integer.parseInt(JacksonUtil.json2map(result).get("total").toString()));
//				}
			//	ar.setNum(2);	
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		return ars;
	}

	/**
	 * 根据应用appCode查应用信息
	 * @param appCode 应用code
	 * @return 应用信息对象
	 */
	public AppRegister queryAppByAppCode(String appCode){
		return appRegisterService.queryAppByAppCode(appCode);
	}
	
}
