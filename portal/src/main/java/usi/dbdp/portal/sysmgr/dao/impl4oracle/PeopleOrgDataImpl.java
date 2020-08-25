package usi.dbdp.portal.sysmgr.dao.impl4oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.app.util.OraclePasswdUtil;
import usi.dbdp.portal.dto.PeopleOrgDataDto;
import usi.dbdp.portal.sysmgr.dao.PeopleOrgDataDao;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.util.CommonUtil;

@OracleDb
@Repository
public class PeopleOrgDataImpl  extends JdbcDaoSupport4oracle  implements PeopleOrgDataDao{

	@Override
	public List<PeopleOrgDataDto> getUsersForNowOrg(Long orgId, String userName,PageObj pageObj) {
		String sql="select u.id,u.user_id,"+OraclePasswdUtil.getDecryptStr("u.user_name")+" as user_name,"
				+ OraclePasswdUtil.getDecryptStr("o.org_name")+" as org_name,pri.org_id as isadd,o.org_id,pri.org_id as manageOrgId,"+OraclePasswdUtil.getDecryptStr("uor.org_name")+" as manageOrgName  from uic_org o, uic_user_has_org uo,uic_user u left join  uic_org_data_pri pri  on u.user_id=pri.user_id  left join uic_org uor  on (uor.org_id=pri.org_id    and uor.del_flag=0) where   uo.id=u.id and uo.org_id=o.org_id and uo.type=1 and u.del_flag=0 and o.del_flag=0 ";
		ArrayList<Object> list = new ArrayList<Object>();
		if(orgId!=null){
			list.add(orgId);
			sql+=" and  o.org_id=? ";
		}
		if(userName!=null && !"".equals(userName)){
			list.add(userName);
			sql+=" and  "+OraclePasswdUtil.getDecryptStr("u.user_name")+" like  concat(CONCAT('%' ,?),'%')";
		}
		sql+="   order by u.id desc  ";
		
		return this.queryByPage(sql,list.toArray(),new RowMapper<PeopleOrgDataDto>() {

					@Override
					public PeopleOrgDataDto mapRow(ResultSet rs, int rowNum) throws SQLException {
						PeopleOrgDataDto peoOrgData = new PeopleOrgDataDto();
						peoOrgData.setIsAdd(rs.getString("isadd"));
						peoOrgData.setUserName(rs.getString("user_name"));
						peoOrgData.setUserId(rs.getString("user_id"));
						peoOrgData.setOrgName(rs.getString("org_name"));
						peoOrgData.setOrgId(rs.getString("org_id"));
						peoOrgData.setManageOrgId(rs.getString("manageOrgId"));
						peoOrgData.setManageOrgName(rs.getString("manageOrgName"));
						return peoOrgData;
					}
				}, pageObj);
	}

	@Override
	public void deleteUserOrg(String userId, Long orgId) {
		
		String sql="delete from uic_org_data_pri  where user_id=?  ";
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userId);
		if(orgId!=null){
			list.add(orgId);
			sql+=" and  org_id=? ";
		}
		this.getJdbcTemplate().update(sql, list.toArray());
	}

	@Override
	public void addUserOrg(String userId, Long orgId) {
		String 	sql="insert into uic_org_data_pri (user_id,org_id)  values(?,?)";	
		this.getJdbcTemplate().update(sql, new Object[]{userId,orgId});
	}

	@Override
	public long searchPriOrgId(String userId) {
		String sql = " select ORG_ID from uic_org_data_pri where user_id = ? ";
		List<Long> list = this.getJdbcTemplate().queryForList(sql, new Object[]{userId}, Long.class);
		
		return list.size()>0?list.get(0):0;
	}

	@Override
	public int checkOrgCode(String orgCode) {
		String sql = " select count(*) from uic_org where org_code = ? and del_flag = 0 ";
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{orgCode}, Integer.class);
	}

	@Override
	public List<UserInfo> getUsersByOrgId(Long adminOrgId, Long orgId,String adminId, String userId, PageObj pageObj) {
		String sql="select u.id ,u.user_id ,u.login_id ,"+OraclePasswdUtil.getDecryptStr("u.user_name")+" as user_name ,"+OraclePasswdUtil.getDecryptStr("o.org_name_seq")+" as org_name_seq,o.org_id   "
				+ " from uic_user u , uic_org o , uic_user_has_org og "
				+ " where u.id=og.id and og.org_id = o.org_id "
				+ " and u.del_flag=0  and og.type=1 ";
		if(orgId!=null){//操作人员选择了机构
			sql +=
			"and o.org_id_seq like "+
			"( select concat(t.org_id_seq,'%') from   "+
				"( select o2.org_id_seq from uic_org o2 where  o2.org_id="+orgId+" ) t  "+
			") "; 	
		}else if(adminOrgId!=null){//操作人员未选择机构，但是操作人员具有机构id
			sql +=
			"and o.org_id_seq like "+
			"( select concat(t.org_id_seq,'%') from   "+
				"( select o2.org_id_seq from uic_org o2 where  o2.org_id="+adminOrgId+" ) t  "+
			") ";
		}else{//操作人员不具有机构id
			sql +=
			"and o.org_id_seq like  "+
			"( select concat(t.org_id_seq,'%') from   "+
				"( select o2.org_id_seq from uic_org o2 where  o2.org_id='1' ) t  "+
			") ";
		}
		if (CommonUtil.hasValue(userId)){
			sql += " and u.user_id like '%" + userId + "%' ";
		}	
		return this.queryByPage(sql, new Object[] {}, new RowMapper<UserInfo>() {

			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(rs.getLong("id"));
				userInfo.setOrgId(rs.getLong("org_id"));
				userInfo.setUserId(rs.getString("user_id"));
				userInfo.setLoginId(rs.getString("login_id"));
				userInfo.setUserName(rs.getString("user_name"));
				userInfo.setOrgNameSeq(rs.getString("org_name_seq"));
				return userInfo;
			}
		}, pageObj);
	}

	@Override
	public int searchCount(long orgId, long staffId) {
		String sql = " select count(*) from uic_user_has_org a ,uic_user b where a.id = b.id and a.id=? and a.org_id = ? and b.del_flag = 0 and a.type = 2 ";
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{staffId,orgId}, Integer.class);
	}

	@Override
	public void addUserOrgType2(long orgId, long staffId) {
		String sql = " insert into uic_user_has_org(org_id ,id ,type ) values (?,?,2) ";
		this.getJdbcTemplate().update(sql, new Object[]{orgId,staffId});
	}

}
