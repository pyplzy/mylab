package usi.dbdp.portal.sysmgr.dao;

import java.util.List;

import usi.dbdp.portal.dto.PeopleOrgDataDto;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.UserInfo;

public interface PeopleOrgDataDao {
	/**
	 * 
  	* @date 2017年1月3日 下午5:21:27
	* @Description: 获取当前机构下的所有人
	* @param orgId
	* @param userName
	 */
	public List<PeopleOrgDataDto> getUsersForNowOrg(Long orgId,String userName,PageObj pageObj);
	/**
	 * 
  	* @date 2017年1月4日 下午3:03:30
	* @Description: 删除人员机构管理权限
	* @param userId
	* @param orgId
	 */
	public void deleteUserOrg(String  userId, Long orgId);
	/**
	 * 
	* @Title: addUserOrg
 	* @date 2017年1月4日 下午3:19:51
	* @Description: 添加人员机构管理权限
	* @param userId
	* @param orgId    
	* @return void
	 */
	public void addUserOrg(String userId, Long orgId);
	/**
	 * 查询用户机构权限
	 * @param userId
	 * @return
	 */
	public long searchPriOrgId(String userId);
	
	public int checkOrgCode(String orgCode);
	
	public List<UserInfo> getUsersByOrgId(Long adminOrgId, Long orgId, String adminId, String userId, PageObj pageObj);
	
	public int searchCount(long orgId, long staffId);
	public void addUserOrgType2(long orgId, long staffId);
}
