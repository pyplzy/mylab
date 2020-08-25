package usi.dbdp.portal.addressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.addressbook.dao.AddressBookSyncDao;
import usi.dbdp.uic.entity.User;

/**
 * 机构人员管理时，新增人员或者修改人员信息，或者个人信息修改时同步到通讯录
 * @author lmwang
 * 创建时间：2015-5-7 下午12:43:48
 */
@Service
public class AddressBookSyncService {

	@Resource
	private AddressBookSyncDao addressBookSyncDao;
	
	/**
	 * 尝试同步到通讯录（更新成功则不插入，否则插入）
	 * @param user 用户对象
	 * @return 
	 */
	@Transactional
	public boolean sync2AddressBook(User user) {
		//更新失败
		if(addressBookSyncDao.updateAddressBookByUserId(user)==0) {
			//插入
			if(addressBookSyncDao.insert2AddressBook(user)==1) {
				return true;
			}
			return false;
		}
		//TODO 同步公司和部门（用机构名称序列来整合，公司=联通集团，或者省公司，部门为后面，若没有，则部门默认为空）
		return true;
	}
	/**
	 * 插入到通讯录
	 * @param user 用户对象
	 * @return 插入记录个数
	 */
	@Transactional
	public int insert2AddressBook(User user) {
		return addressBookSyncDao.insert2AddressBook(user);
	}
	/**
	 * 更新到通讯录
	 * @param user 用户对象
	 * @return 更新记录个数
	 */
	@Transactional
	public int updateAddressBookByUserId(User user) {
		return addressBookSyncDao.updateAddressBookByUserId(user);
	}
	/**
	 * 更新通讯录公司和部门
	 * @param company 公司（集团或者是省）
	 * @param department 部门（机构名称序列去掉省以后的，或者是空）
	 * @param userId 登录账号
	 * @return
	 */
	public int updateABcompanyAndDepartment(String company,String department,String userId) {
		return addressBookSyncDao.updateABcompanyAndDepartment(company, department, userId);
	}
}
