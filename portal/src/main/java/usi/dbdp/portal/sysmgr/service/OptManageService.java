package usi.dbdp.portal.sysmgr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.dto.OptDto;
import usi.dbdp.portal.dto.ResourceDto;
import usi.dbdp.portal.sysmgr.dao.OptManageDao;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2016年12月29日 上午9:19:37
 * 说明
 */
@Service
public class OptManageService {

	@Resource
	private OptManageDao optManageDao;
	
	public int queryResource(String appCode, String resourceCode) {
		return optManageDao.queryResource(appCode,resourceCode);
	}

	public ResourceDto queryReById(long resourceId) {
		return optManageDao.queryReById(resourceId);
	}

	@Transactional(rollbackFor=Exception.class) 
	public void editResource(ResourceDto resourceDto) {
		optManageDao.editResource(resourceDto);
	}

	@Transactional(rollbackFor=Exception.class) 
	public void addResource(ResourceDto resourceDto) {
		optManageDao.addResource(resourceDto);
	}

	public Map<String, Object> qryResources(String appCode, PageObj pageObj) {
		Map<String , Object> map=new HashMap<String , Object>();
		List<ResourceDto> list = optManageDao.qryResources(appCode,pageObj);
		map.put("total", pageObj.getTotal());
		map.put("rows", list );
		return map;
	}

	@Transactional(rollbackFor=Exception.class) 
	public int delResource(long resourceId) {
		return optManageDao.delResource(resourceId);
	}

	public Map<String, Object> qryOpts(long resourceId, PageObj pageObj) {
		Map<String , Object> map=new HashMap<String , Object>();
		List<OptDto> list = optManageDao.qryOpts(resourceId,pageObj);
		map.put("total", pageObj.getTotal());
		map.put("rows", list );
		return map;
	}

	public OptDto queryOptById(long resourceId, long optId) {
		return optManageDao.queryOptById(resourceId,optId);
	}

	public boolean jugdeCode(OptDto optDto) {
		int num = optManageDao.jugdeCode(optDto);
		return num>0;
	}

	@Transactional(rollbackFor=Exception.class) 
	public void editOpt(OptDto optDto) {
		optManageDao.editOpt(optDto);
	}

	@Transactional(rollbackFor=Exception.class) 
	public void addOpt(OptDto optDto) {
		optManageDao.addOpt(optDto);
	}

	@Transactional(rollbackFor=Exception.class) 
	public void delOpts(long optId) {
		optManageDao.delOpt(optId);
	}

	public boolean checkOpt(long optId) {
		int num = 0;
		num = optManageDao.qryOptRoleNum(optId);
		return num>0;
	}

	public boolean qryOptRes(long resourceId) {
		int num = 0;
		num = optManageDao.qryOptRes(resourceId);
		return num>0;
	}

	public List<ResourceDto> queryResources(String appCode) {
		return optManageDao.queryResources(appCode);
	}

	public ResourceDto queryOldResource(long resourceId) {
		return optManageDao.queryOldResource(resourceId);
	}

	public OptDto queryOldOpt(long privilegeId) {
		return optManageDao.queryOldOpt(privilegeId);
	}



}
