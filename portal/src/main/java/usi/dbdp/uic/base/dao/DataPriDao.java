package usi.dbdp.uic.base.dao;

import java.util.List;

import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.User;

/**
 * @author zhqnie
 * @version 2015年9月6日 上午10:32:19
 * 说明
 */
public interface DataPriDao {

	/**
	 * @author ma.guangming
	 * 批量保存数据权限
	 * @param dataPris 数据权限列表
	 */
	public boolean batchAddDataPris(List<DataPri> dataPris);


	/**
	 * @author ma.guangming
	 * 删除应用权限
	 * @param userId 登录帐号
	 */
	public boolean delDataPri(String userId);

	/**
	 * @author ma.guangming
	 * 查询登录帐号拥有的权限
	 * @param userId 登录帐号
	 * @param privilegeType 权限类型（1：应用数据权限，2：机构数据权限）
	 * @return 数据权限列表
	 */
	public List<DataPri> getDataPrisByUserIdAndPrivilegeType(
			String userId, int privilegeType);

	/**
	 * @author ma.guangming
	 * 根据登录帐号或用户姓名查询管理员角色的用户
	 * @param value 查询字符串 
	 * @return 用户列表
	 */
	public List<User> getAdminsByUserIdOrUserNameFromRole(String value,Long province,String userId);

}