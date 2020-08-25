package usi.dbdp.portal.sysmgr.dao.impl4oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.app.util.OraclePasswdUtil;
import usi.dbdp.portal.dto.OptDto;
import usi.dbdp.portal.dto.OrgDto;
import usi.dbdp.portal.sysmgr.dao.RoleManageDao;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2016年10月18日 下午5:48:28
 * 说明
 */
@OracleDb
@Repository
public class RoleManageDaoImpl extends JdbcDaoSupport4oracle implements RoleManageDao{

	@Override
	public List<OrgDto> getProvinces(Long province) {
		String sql = "";
		if(province==0){
			sql = " SELECT org_id,"+OraclePasswdUtil.getDecryptStr("org_name")+" as org_name FROM uic_org  WHERE org_grade = 2 AND DEL_FLAG = 0 ";
			return this.getJdbcTemplate().query(sql, new RowMapper<OrgDto>(){

				@Override
				public OrgDto mapRow(ResultSet rs, int rowNum) throws SQLException {
					OrgDto od = new OrgDto();
					od.setOrgId(rs.getLong("org_id"));
					od.setOrgName(rs.getString("org_name"));
					return od;
				}
				
			});
		}else{
			sql = " SELECT org_id,"+OraclePasswdUtil.getDecryptStr("org_name")+" as org_name FROM uic_org  WHERE org_grade = 2 AND DEL_FLAG = 0 and org_id = ?";
			return this.getJdbcTemplate().query(sql,new Object[]{province}, new RowMapper<OrgDto>(){

				@Override
				public OrgDto mapRow(ResultSet rs, int rowNum) throws SQLException {
					OrgDto od = new OrgDto();
					od.setOrgId(rs.getLong("org_id"));
					od.setOrgName(rs.getString("org_name"));
					return od;
				}
				
			});
		}
	}

	@Override
	public List<OptDto> qryOptsByRoleId(long roleId, long resourceId,
			String optName, String appCode,PageObj pageObj) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(roleId);
		list.add(appCode);
		String sql = " SELECT t.PRIVILEGE_ID ,t.RESOURCE_ID ,t.PRIVILEGE_CODE ,t.PRIVILEGE_NAME ,a.RESOURCE_NAME ,"
				+ " (CASE WHEN t.PRIVILEGE_ID IN (SELECT b.PRIVILEGE_ID FROM uic_role_has_opt b LEFT JOIN uic_opt c ON c.PRIVILEGE_ID = b.PRIVILEGE_ID WHERE b.role_id = ? ) THEN 1 ELSE 0 END)  IS_GRANTED "
				+ " FROM uic_opt t ,uic_resource a WHERE t.RESOURCE_ID = a.RESOURCE_ID and a.app_code = ? and t.del_flag = 0 and a.del_flag = 0 ";
		if(resourceId > 0){
			sql += " and t.RESOURCE_ID = ? ";
			list.add(resourceId);
		}
		if(CommonUtil.hasValue(optName)){
			sql += " and t.PRIVILEGE_NAME  like  CONCAT(CONCAT('%',?),'%')  ";
			list.add(optName);
		}
		sql += " order by IS_GRANTED DESC ";
		return this.queryByPage(sql, list.toArray(), new RowMapper<OptDto>() {

			@Override
			public OptDto mapRow(ResultSet arg0, int arg1) throws SQLException {
				OptDto op = new OptDto();
				op.setPrivilegeId(arg0.getLong("PRIVILEGE_ID"));
				op.setPrivilegeCode(arg0.getString("PRIVILEGE_CODE"));
				op.setPrivilegeName(arg0.getString("PRIVILEGE_NAME"));
				op.setResourceId(arg0.getLong("RESOURCE_ID"));
				op.setResourceName(arg0.getString("RESOURCE_NAME"));
				op.setIsGranted(arg0.getInt("IS_GRANTED"));
				return op;
			}
		}, pageObj);
	}

	@Override
	public void delRoleOpt(long roleId, long optId) {
		String sql = " delete from uic_role_has_opt where role_id = ? and PRIVILEGE_ID = ? " ;
		this.getJdbcTemplate().update(sql, new Object[]{roleId,optId});
	}

	@Override
	public void giveRoleOpt(long roleId, long optId) {
		String sql = " insert into uic_role_has_opt(role_id,PRIVILEGE_ID) values(?,?) " ;
		this.getJdbcTemplate().update(sql, new Object[]{roleId,optId});
	}

	@Override
	public List<OptDto> qryOptListById(long staffId) {
		String sql =
				"SELECT " +
						"  t.PRIVILEGE_ID, " + 
						"  t.RESOURCE_ID, " + 
						"  t.PRIVILEGE_CODE, " + 
						"  t.PRIVILEGE_NAME, " + 
						"  a.RESOURCE_NAME, " + 
						"  ( " + 
						"    CASE " + 
						"      WHEN t.PRIVILEGE_ID IN " + 
						"      (SELECT " + 
						"        b.PRIVILEGE_ID " + 
						"      FROM " + 
						"        uic_role_has_opt b " + 
						"        LEFT JOIN uic_opt c " + 
						"          ON c.PRIVILEGE_ID = b.PRIVILEGE_ID " + 
						"      WHERE b.role_id IN ( SELECT w.role_id FROM uic_user_has_role w WHERE w.id = ? ) ) " + 
						"      THEN 1 " + 
						"      ELSE 0 " + 
						"    END " + 
						"  ) IS_GRANTED " + 
						"FROM " + 
						"  uic_opt t, " + 
						"  uic_resource a " + 
						"WHERE t.RESOURCE_ID = a.RESOURCE_ID " + 
						"  AND a.app_code = 'portal' AND a.resource_code = 'org_resource' " + 
						"  AND t.del_flag = 0 AND a.del_flag = 0 order by IS_GRANTED DESC ";
		return this.getJdbcTemplate().query(sql, new Object[]{staffId}, new RowMapper<OptDto>() {

			@Override
			public OptDto mapRow(ResultSet arg0, int arg1) throws SQLException {
				OptDto op = new OptDto();
				op.setPrivilegeId(arg0.getLong("PRIVILEGE_ID"));
				op.setPrivilegeCode(arg0.getString("PRIVILEGE_CODE"));
				op.setPrivilegeName(arg0.getString("PRIVILEGE_NAME"));
				op.setResourceId(arg0.getLong("RESOURCE_ID"));
				op.setResourceName(arg0.getString("RESOURCE_NAME"));
				op.setIsGranted(arg0.getInt("IS_GRANTED"));
				return op;
			}
		});
	
	}

}
