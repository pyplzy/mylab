package usi.dbdp.portal.sysmgr.dao;

import java.util.List;

import usi.dbdp.portal.dto.OptDto;
import usi.dbdp.portal.dto.OrgDto;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2016年10月18日 下午5:29:56
 * 说明
 */
public interface RoleManageDao {

	public List<OrgDto> getProvinces(Long province);

	public List<OptDto> qryOptsByRoleId(long roleId, long resourceId,
			String optName,String appCode, PageObj pageObj);

	public void delRoleOpt(long roleId, long optId);

	public void giveRoleOpt(long roleId, long optId);

	public List<OptDto> qryOptListById(long staffId);
	
}
