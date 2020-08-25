package usi.dbdp.portal.sysmgr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

 import usi.dbdp.portal.dto.PeopleOrgDataDto;
import usi.dbdp.portal.sysmgr.dao.PeopleOrgDataDao;
import usi.dbdp.uic.dto.PageObj;

@Service
public class PeopleOrgDataService {
	@Resource
	private PeopleOrgDataDao peopleOrgDataDao;
	
	@Transactional(readOnly=true)
	public Map<String,Object > getUsersForNowOrg(Long orgId,String userName,PageObj pageObj){
		List<PeopleOrgDataDto> peo= null ;
		Map<String , Object > map =new HashMap<String , Object >();
		try{
			peo = peopleOrgDataDao.getUsersForNowOrg(orgId, userName,pageObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("peo",peo);
		map.put("pageObj",pageObj);
		return  map;
	}
	
	public String deleteUserOrg(String userId, Long orgId) {
		try{
			 peopleOrgDataDao.deleteUserOrg(userId,orgId);
			 return "success";
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
	}
	/**
	 * 
	* @Title: editUserOrg
	* @date 2017年1月5日 下午4:01:22
	* @Description: 操作机构数据权限type=0修改type=1新加
	* @param userId
	* @param orgId
	* @param type    
	* @return void
	 */
	@Transactional(rollbackFor=Exception.class)
	public void editUserOrg(String userId, Long orgId,Integer  type) {
		//type=0代表新增，type=1代表修改，修改先删除后增加
		if(1==type){
			peopleOrgDataDao.deleteUserOrg(userId,null);
		}
		peopleOrgDataDao.addUserOrg(userId,orgId);

	}

	public long searchPriOrgId(String userId) {
		return peopleOrgDataDao.searchPriOrgId(userId);
	}

	public String checkOrgCode(String orgCode) {
		int num = peopleOrgDataDao.checkOrgCode(orgCode);
		return num>0?"use":"ok";
	}

	public Map<String, Object> getUsersByOrgId(Long adminOrgId, Long orgId, String adminId,
			String userId, PageObj pageObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageObj", pageObj);
		map.put("userInfos",peopleOrgDataDao.getUsersByOrgId(adminOrgId, orgId, adminId, userId, pageObj));
		return map;
	}

	public int searchCount(long orgId, long staffId) {
		return peopleOrgDataDao.searchCount(orgId,staffId);
	}

	public void addUserOrgType2(long orgId, long staffId) {
		peopleOrgDataDao.addUserOrgType2(orgId,staffId);
	}

}
