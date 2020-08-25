package usi.dbdp.portal.sysmgr.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import usi.dbdp.uic.base.service.AppRegisterService;
import usi.dbdp.uic.base.service.DataPriService;
import usi.dbdp.uic.entity.AppRegister;
import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.User;
/**
 * 应用数据权限服务层需要的service
 * @author ma.guangming
 *
 */
@Service
public class PortalDataPriService {
	
	@Resource
	private DataPriService dataPriService;
	
	@Resource
	private AppRegisterService appRegisterService;
	
	
	/**
	 * @author ma.guangming
	 * 根据登录帐号或用户姓名查询管理员角色的用户
	 * @param value 查询字符串 
	 * @return 用户列表
	 */
	public List<User> getAdminsByUserIdOrUserNameFromRole(String value,Long province,String userId){
		return dataPriService.getAdminsByUserIdOrUserNameFromRole(value,province,userId);
	}
	
	/**
	 * 查询应用级应用
	 * @return List<AppRegister>
	 * @author  ma.guangming
	 */
	public List<AppRegister> getNomalAppsByState(){
		return appRegisterService.getNomalAppsByState();
	}
	
	/**
	 * @author ma.guangming
	 * 查询登录帐号拥有的权限
	 * @param userId 登录帐号
	 * @param privilegeType 权限类型（1：应用数据权限，2：机构数据权限）
	 * @return 数据权限列表
	 */
	public List<DataPri> getDataPriByUserIdAndPrivilegeType(String userId , int privilegeType){
		return dataPriService.getDataPrisByUserIdAndPrivilegeType(userId, privilegeType);
	}
	
	
	/**
	  * 批量保存数据权限
	 * @param dataPris 数据权限列表
	 * @return true 添加成功 false 添加失败
	 */
	public boolean batchAddDataPris(List<DataPri> dataPris) {
		return dataPriService.batchAddDataPris(dataPris);
	}
	
	/**
	 * @author ma.guangming
	  * 删除数据权限
	 * @param dataPri 数据权限
	 */
	public boolean  delDataPri(final String  userId ){
		return dataPriService.delDataPri(userId);
	}
	
}
