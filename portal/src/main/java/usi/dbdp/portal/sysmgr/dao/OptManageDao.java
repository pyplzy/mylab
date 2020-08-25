package usi.dbdp.portal.sysmgr.dao;

import java.util.List;

import usi.dbdp.portal.dto.OptDto;
import usi.dbdp.portal.dto.ResourceDto;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2016年12月29日 上午9:29:03
 * 说明
 */
public interface OptManageDao {

	public int queryResource(String appCode, String resourceCode);

	public ResourceDto queryReById(long resourceId);

	public void editResource(ResourceDto resourceDto);

	public void addResource(ResourceDto resourceDto);

	public List<ResourceDto> qryResources(String appCode, PageObj pageObj);

	public int delResource(long resourceId);

	public List<OptDto> qryOpts(long resourceId, PageObj pageObj);

	public OptDto queryOptById(long resourceId, long optId);

	public int jugdeCode(OptDto optDto);

	public void editOpt(OptDto optDto);

	public void addOpt(OptDto optDto);

	public void delOpt(long optId);

	public int qryOptRoleNum(long optId);

	public int qryOptRes(long resourceId);

	public List<ResourceDto> queryResources(String appCode);

	public ResourceDto queryOldResource(long resourceId);

	public OptDto queryOldOpt(long privilegeId);

}
