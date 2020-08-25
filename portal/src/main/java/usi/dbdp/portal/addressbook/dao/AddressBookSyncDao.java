package usi.dbdp.portal.addressbook.dao;

import usi.dbdp.uic.entity.User;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午10:50:57
 * 说明
 */
public interface AddressBookSyncDao {

	/**
	 * 插入到通讯录
	 * @param user 用户对象
	 * @return 插入记录个数
	 */
	public int insert2AddressBook(User user);

	/**
	 * 更新到通讯录
	 * @param user 用户对象
	 * @return 更新记录个数
	 */
	public int updateAddressBookByUserId(User user);

	/**
	 * 更新通讯录公司和部门
	 * @param company 公司（集团或者是省）
	 * @param department 部门（机构名称序列去掉省以后的，或者是空）
	 * @param userId 登录账号
	 * @return
	 */
	public int updateABcompanyAndDepartment(String company,
			String department, String userId);

}