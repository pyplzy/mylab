package usi.dbdp.portal.sysmgr.dao.impl4mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.dto.OptDto;
import usi.dbdp.portal.dto.ResourceDto;
import usi.dbdp.portal.sysmgr.dao.OptManageDao;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2016年12月29日 上午9:29:33
 * 说明
 */
@MysqlDb
@Repository
public class OptManageDaoImpl extends JdbcDaoSupport4mysql implements OptManageDao{

	@Override
	public int queryResource(String appCode, String resourceCode) {
		String sql = " select count(*) from uic_resource where APP_CODE = ? and RESOURCE_CODE = ? ";
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{appCode,resourceCode}, Integer.class);
	}

	@Override
	public ResourceDto queryReById(long resourceId) {
		String sql = " select RESOURCE_ID ,APP_CODE, RESOURCE_CODE ,RESOURCE_NAME, RESOURCE_DESC ,DEL_FLAG from uic_resource where RESOURCE_ID = ? ";
		List<ResourceDto> list =  this.getJdbcTemplate().query(sql, new Object[]{resourceId}, new RowMapper<ResourceDto>(){

			@Override
			public ResourceDto mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				ResourceDto re = new ResourceDto();
				re.setAppCode(arg0.getString("APP_CODE"));
				re.setResourceCode(arg0.getString("RESOURCE_CODE"));
				re.setResourceName(arg0.getString("RESOURCE_NAME"));
				re.setResourceDesc(arg0.getString("RESOURCE_DESC"));
				re.setResourceId(arg0.getLong("RESOURCE_ID"));
				return re;
			}});
		return list.size()>0?list.get(0):null;
	}

	@Override
	public void editResource(ResourceDto resourceDto) {
		String sql = " update uic_resource set APP_CODE = ? ,RESOURCE_CODE = ?, RESOURCE_NAME=?,"
				+ " RESOURCE_DESC = ? where RESOURCE_ID = ?";
		this.getJdbcTemplate().update(sql, new Object[]{
				resourceDto.getAppCode(),
				resourceDto.getResourceCode(),
				resourceDto.getResourceName(),
				resourceDto.getResourceDesc(),
				resourceDto.getResourceId()
		});
	}

	@Override
	public void addResource(ResourceDto resourceDto) {
		String sql = " insert into uic_resource( APP_CODE,RESOURCE_CODE,RESOURCE_NAME,RESOURCE_DESC) values (?,?,?,?) ";
		this.getJdbcTemplate().update(sql, new Object[]{
				resourceDto.getAppCode(),
				resourceDto.getResourceCode(),
				resourceDto.getResourceName(),
				resourceDto.getResourceDesc(),	
		});
	}

	@Override
	public List<ResourceDto> qryResources(String appCode, PageObj pageObj) {
		String sql = " select RESOURCE_ID ,APP_CODE, RESOURCE_CODE ,RESOURCE_NAME, RESOURCE_DESC ,DEL_FLAG "
				+ " from uic_resource where APP_CODE = ? and DEL_FLAG =0 order by  RESOURCE_ID "; 
		return this.queryByPage(sql, new Object[]{appCode}, new RowMapper<ResourceDto>() {

			@Override
			public ResourceDto mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				ResourceDto re = new ResourceDto();
				re.setAppCode(arg0.getString("APP_CODE"));
				re.setResourceCode(arg0.getString("RESOURCE_CODE"));
				re.setResourceName(arg0.getString("RESOURCE_NAME"));
				re.setResourceDesc(arg0.getString("RESOURCE_DESC"));
				re.setResourceId(arg0.getLong("RESOURCE_ID"));
				return re;
			}
		}, pageObj);
	}

	@Override
	public int delResource(long resourceId) {
		String sql = " update  uic_resource  set DEL_FLAG = 1 where  RESOURCE_ID = ? ";
		return this.getJdbcTemplate().update(sql, resourceId );
	}

	@Override
	public List<OptDto> qryOpts(long resourceId, PageObj pageObj) {
		String sql = " SELECT t.RESOURCE_ID , t.PRIVILEGE_ID , t.PRIVILEGE_CODE , t.PRIVILEGE_NAME , t.DEL_FLAG , a.RESOURCE_NAME "
				+ " FROM uic_opt t, uic_resource a WHERE t.RESOURCE_ID = ? and t.RESOURCE_ID = a.RESOURCE_ID AND t.DEL_FLAG = 0  "; 
		return this.queryByPage(sql, new Object[]{resourceId}, new RowMapper<OptDto>() {

			@Override
			public OptDto mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				OptDto re = new OptDto();
				re.setPrivilegeId(arg0.getLong("PRIVILEGE_ID"));
				re.setPrivilegeCode(arg0.getString("PRIVILEGE_CODE"));
				re.setPrivilegeName(arg0.getString("PRIVILEGE_NAME"));
				re.setResourceId(arg0.getLong("RESOURCE_ID"));
				re.setResourceName(arg0.getString("RESOURCE_NAME"));
				return re;
			}
		}, pageObj);
	}

	@Override
	public OptDto queryOptById(long resourceId, long optId) {
		String sql = " select PRIVILEGE_CODE ,PRIVILEGE_NAME from uic_opt where RESOURCE_ID = ? and PRIVILEGE_ID =? ";
		List<OptDto> list =  this.getJdbcTemplate().query(sql, new Object[]{resourceId,optId}, new RowMapper<OptDto>(){

			@Override
			public OptDto mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				OptDto re = new OptDto();
				re.setPrivilegeCode(arg0.getString("PRIVILEGE_CODE"));
				re.setPrivilegeName(arg0.getString("PRIVILEGE_NAME"));
				return re;
			}});
		return list.size()>0?list.get(0):null;
	}

	@Override
	public int jugdeCode(OptDto optDto) {
		String sql = "";
		if(optDto.getPrivilegeId()>0){  //id存在
			sql = " select count(*) from uic_opt where RESOURCE_ID = ? and PRIVILEGE_CODE =? and PRIVILEGE_ID <> "+optDto.getPrivilegeId() ;
		}else{
			sql = " select count(*) from uic_opt where RESOURCE_ID = ? and PRIVILEGE_CODE =? ";
		}
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{
				optDto.getResourceId(),optDto.getPrivilegeCode()
		}, Integer.class);
	}

	@Override
	public void editOpt(OptDto optDto) {
		String sql = " update uic_opt set PRIVILEGE_CODE = ? , PRIVILEGE_NAME = ? where PRIVILEGE_ID = ? ";
		this.getJdbcTemplate().update(sql, new Object[]{
				optDto.getPrivilegeCode(),
				optDto.getPrivilegeName(),
				optDto.getPrivilegeId()
		});
	}

	@Override
	public void addOpt(OptDto optDto) {
		String sql = " insert into uic_opt (RESOURCE_ID ,PRIVILEGE_CODE,PRIVILEGE_NAME) values(?,?,?)";
		this.getJdbcTemplate().update(sql, new Object[]{
				optDto.getResourceId(),
				optDto.getPrivilegeCode(),
				optDto.getPrivilegeName()
		});
	}
	
	public void delOpt(long optId) {
		String sql = " update uic_opt set DEL_FLAG = 1 where PRIVILEGE_ID =? ";
		this.getJdbcTemplate().update(sql,optId);
	}

	@Override
	public int qryOptRoleNum(long optId) {
		String sql = " select count(*) from UIC_ROLE_HAS_OPT where PRIVILEGE_ID = ? ";
		return this.getJdbcTemplate().queryForObject(sql,new Object[]{optId}, Integer.class);
	}

	@Override
	public int qryOptRes(long resourceId) {
		String sql = " select count(*) from uic_opt t where t.RESOURCE_ID = ? and t.DEL_FLAG = 0";
		return this.getJdbcTemplate().queryForObject(sql,new Object[]{resourceId}, Integer.class);
	}

	@Override
	public List<ResourceDto> queryResources(String appCode) {
		String sql = " select RESOURCE_ID ,APP_CODE, RESOURCE_CODE ,RESOURCE_NAME, RESOURCE_DESC ,DEL_FLAG "
				+ " from uic_resource where APP_CODE = ? and DEL_FLAG =0 order by  RESOURCE_ID "; 
		return this.getJdbcTemplate().query(sql, new Object[]{appCode}, new RowMapper<ResourceDto>() {

			@Override
			public ResourceDto mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				ResourceDto re = new ResourceDto();
				re.setAppCode(arg0.getString("APP_CODE"));
				re.setResourceCode(arg0.getString("RESOURCE_CODE"));
				re.setResourceName(arg0.getString("RESOURCE_NAME"));
				re.setResourceDesc(arg0.getString("RESOURCE_DESC"));
				re.setResourceId(arg0.getLong("RESOURCE_ID"));
				return re;
			}
		});
	}

	@Override
	public ResourceDto queryOldResource(long resourceId) {
		String sql = " select RESOURCE_ID ,APP_CODE, RESOURCE_CODE ,RESOURCE_NAME, RESOURCE_DESC ,DEL_FLAG "
				+ " from uic_resource where DEL_FLAG =0  and RESOURCE_ID = ?"; 
		return this.getJdbcTemplate().query(sql,  new Object[]{resourceId},new RowMapper<ResourceDto>() {

			@Override
			public ResourceDto mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				ResourceDto re = new ResourceDto();
				re.setAppCode(arg0.getString("APP_CODE"));
				re.setResourceCode(arg0.getString("RESOURCE_CODE"));
				re.setResourceName(arg0.getString("RESOURCE_NAME"));
				re.setResourceDesc(arg0.getString("RESOURCE_DESC"));
				re.setResourceId(arg0.getLong("RESOURCE_ID"));
				return re;
			}
		}).get(0);
	}

	@Override
	public OptDto queryOldOpt(long privilegeId) {
		String sql = " SELECT t.RESOURCE_ID , t.PRIVILEGE_ID , t.PRIVILEGE_CODE , t.PRIVILEGE_NAME , t.DEL_FLAG "
				+ " FROM uic_opt t WHERE  t.PRIVILEGE_ID = ?  "; 
		return this.getJdbcTemplate().query(sql, new Object[]{privilegeId}, new RowMapper<OptDto>() {

			@Override
			public OptDto mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				OptDto re = new OptDto();
				re.setPrivilegeId(arg0.getLong("PRIVILEGE_ID"));
				re.setPrivilegeCode(arg0.getString("PRIVILEGE_CODE"));
				re.setPrivilegeName(arg0.getString("PRIVILEGE_NAME"));
				re.setResourceId(arg0.getLong("RESOURCE_ID"));
				return re;
			}
		}).get(0);
	}

}
