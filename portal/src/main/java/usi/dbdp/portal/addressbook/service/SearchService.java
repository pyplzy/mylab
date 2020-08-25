package usi.dbdp.portal.addressbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.addressbook.dao.SearchDao;
import usi.dbdp.portal.dto.PtlPabGroupAndPersonInfo;
import usi.dbdp.portal.entity.PtlAddressBook;
import usi.dbdp.portal.entity.PtlPabGroup;
import usi.dbdp.uic.dto.PageObj;

/**
 * 通讯录搜索增、删、改、查Service
 * @author lxci
 * 创建时间：2015-03-27 上午 13:00:00
 */

@Service
public class SearchService {
	
	@Resource
	 private SearchDao searchDao;
	 
	/**
	 * 搜索全部人员
	 */
	public List<PtlAddressBook> queryPersonListService(PageObj pageObj){
		return searchDao.queryPersonList(pageObj);
	}
	
	/**
	 * 根据条件搜索人员
	 * @param param 查询条件
	 * @param flag flag=1根据user_id；2时根据name；其他时根据主键查询
	 * @param pageObj 分页对象
	 * @return 符合搜索条件的记录列表
	 */
	@Transactional(readOnly=true)
	public Map<String , Object> queryPersonInfoByParamService(Object param,int flag,PageObj pageObj){
		Map<String , Object > map =new HashMap<String , Object >();
		map.put("personLst",searchDao.queryPersonInfoByParam(param,flag,pageObj));
		map.put("pageObj",pageObj);
		return map;
	}
/*	@Transactional(readOnly=true)
	public Map<String , Object> queryPersonInfoByParamService(Object param,int flag,PageObj pageObj){
		Map<String , Object > map =new HashMap<String , Object >();
		map.put("personLst",searchDao.queryPersonInfoByParam(param,flag,pageObj));
		map.put("pageObj",pageObj);
		return map;
	}*/
	/**
	 * 根据主键获取通讯录详细信息
	 * @param addressBookId 通讯录主键
	 * @return
	 */
	@Transactional(readOnly=true)
	public PtlAddressBook getDetailAddressBookById(int addressBookId) {
		return searchDao.queryPersonDetailInfoById(addressBookId);
	}
	/**
	 * 根据userId查询通讯录具体信息
	 * @param userId 登录账号
	 * @return 
	 */
	@Transactional(readOnly=true)
	public PtlAddressBook getDetailUserId(String userId) {
		return searchDao.queryPersonDetailInfoByUserId(userId);
	}
	/**
	 * 查询个人通讯录的分组及人员信息
	 */
	
	public List<PtlPabGroupAndPersonInfo> queryGroupAndPersonInfoService(String userId,PageObj pageObj){
	    return searchDao.queryGroupAndPersonInfo(userId,pageObj);
	}
	
	/**
	 * 查询个人通讯录联系人组名
	 */
	public List<PtlPabGroup> queryPabGroupService(String userId){
		return searchDao.queryPabGroup(userId);
	}
	
	/**
	 * 创建个人通讯录联系人组
	 */
	public int addPabGroupService(String groupName,String userId){
		return searchDao.addPabGroup(groupName, userId);
	}
	
	/**
	 * 将人员添加到相应的通讯录联系人组
	 *
	 */
	public int addPersonToGroupService(String userId, int addressBookId, int groupId){
		return searchDao.addPersonToGroup(userId, addressBookId, groupId);
	}
	
}
