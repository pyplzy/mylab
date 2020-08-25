package usi.dbdp.portal.addressbook.dao;

import java.util.List;

import usi.dbdp.portal.entity.PtlAddressBook;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午10:50:37
 * 说明
 */
public interface AddressBookManagerDao {

	/**
	 * 删除通讯录人员
	 */
	public int deletePersonFromAddressBook(int addressBookId);

	/**
	 * 更新通讯录人员信息
	 * @param ptlAddressBook 通讯录实体
	 * @return
	 */
	public int updatePersonInAddressBook(PtlAddressBook ptlAddressBook);

	/**
	 * 新增通讯录记录
	 * @param ptlAddressBook 通讯录实体
	 * @return
	 */
	public int addAddressBookRecord(PtlAddressBook ptlAddressBook);

	/**
	 * 批量添加新的通讯录联系人
	 * 
	 */
	public int[] batchAddPersonInfos(
			List<PtlAddressBook> ptladdressbooks);

}