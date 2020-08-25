package usi.dbdp.portal.addressbook.dao;

import java.util.List;

import usi.dbdp.portal.dto.PtlAddressBookDto;
import usi.dbdp.portal.dto.PtlPabGroupAndPersonInfo;
import usi.dbdp.portal.entity.PtlAddressBook;
import usi.dbdp.portal.entity.PtlPabGroup;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午10:51:10
 * 说明
 */
public interface SearchDao {

	/**
	 * 搜索通讯录人员列表
	 * 
	 */
	public List<PtlAddressBook> queryPersonList(PageObj pageObj);

	/**
	 * 查询通讯录人员具体信息
	 * @param flag=1根据user_id查询人员信息；2根据姓名模糊匹配；其他时根据通讯录主键查询人员信息
	 */
	public List<PtlAddressBookDto> queryPersonInfoByParam(
			Object param, int flag, PageObj pageObj);

	/**
	 * 根据主键查询通讯录具体信息
	 * @param addressBookId 主键
	 * @return 
	 */
	public PtlAddressBook queryPersonDetailInfoById(int addressBookId);

	/**
	 * 根据userId查询通讯录具体信息
	 * @param userId 登录账号
	 * @return 
	 */
	public PtlAddressBook queryPersonDetailInfoByUserId(String userId);

	/**
	 * 根据个人的userId查询个人通讯录的分组及人员信息
	 */

	public List<PtlPabGroupAndPersonInfo> queryGroupAndPersonInfo(
			String userId, PageObj pageObj);

	/**
	 * 查询个人通讯录联系人组名
	 */
	public List<PtlPabGroup> queryPabGroup(String userId);

	/**
	 * 创建个人通讯录联系人组
	 */
	public int addPabGroup(String groupName, String userId);

	/**
	 * 将人员添加到相应的通讯录联系人组
	 *
	 */

	public int addPersonToGroup(String userId, int addressBookId,
			int groupId);

}