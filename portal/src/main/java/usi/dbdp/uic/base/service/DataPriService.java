package usi.dbdp.uic.base.service;

import java.util.List;

import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.User;

/**
 * 数据权限服务
 * @author ma.guangming
 *
 */
public interface DataPriService {
	/**
	  * 批量保存数据权限
	 * @param dataPris 数据权限列表
	 * @return true 添加成功 false 添加失败
	 */
	public boolean batchAddDataPris(List<DataPri> dataPris);
	
	
	/**
	 * @author ma.guangming
	  * 删除应用权限
	 * @param userId 登录帐号
	 */
	public boolean  delDataPri(String userId );
	
	/**
	 * 查询登录帐号拥有的权限
	 * @param userId 登录帐号
	 * @param privilegeType 权限类型（1：应用数据权限，2：机构数据权限）
	 * @return 数据权限列表
	 */
	public List<DataPri> getDataPrisByUserIdAndPrivilegeType(String userId,int privilegeType);
	
	/**
	 * 根据登录帐号或用户姓名查询管理员角色的用户
	 * @param value 查询字符串 
	 * @return 用户列表
	 */
	public List<User> getAdminsByUserIdOrUserNameFromRole(String value,Long province,String userId);
	
	
}
