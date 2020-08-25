package usi.dbdp.uic.base.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.uic.base.dao.DataPriDao;
import usi.dbdp.uic.base.service.DataPriService;
import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.User;
/**
 * 数据权限服务实现
 * @author ma.guangming
 *
 */
@Service
public class DataPriServiceImpl implements DataPriService {
	@Resource
	private DataPriDao dataPriDao;
	/**
	  * 批量保存数据权限
	 * @param dataPris 数据权限列表
	 * @return true 添加成功 false 添加失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean batchAddDataPris(List<DataPri> dataPris) {
		return dataPriDao.batchAddDataPris(dataPris);
	}
	/**
	 * @author ma.guangming
	  * 删除应用权限
	 * @param userId 登录帐号
	 */
	public boolean  delDataPri(String userId ){
		return dataPriDao.delDataPri(userId);
	}
	
	/**
	 * 查询登录帐号拥有的权限
	 * @param userId 登录帐号
	 * @param privilegeType 权限类型（1：应用数据权限，2：机构数据权限）
	 * @return dataPris 数据权限列表
	 */
	@Override
	@Transactional(readOnly=true)
	public List<DataPri> getDataPrisByUserIdAndPrivilegeType(String userId , int privilegeType) {
		return dataPriDao.getDataPrisByUserIdAndPrivilegeType(userId , privilegeType);
	}
	
	/**
	 * 根据登录帐号或用户姓名查询管理员角色的用户
	 * @param value 查询字符串 
	 * @return 用户列表
	 */
	@Override
	@Transactional(readOnly=true)
	public List<User> getAdminsByUserIdOrUserNameFromRole(String value,Long province,String userId){
		return dataPriDao.getAdminsByUserIdOrUserNameFromRole(value,province,userId);
	}
	
}
