package usi.dbdp.portal.task.dao.impl4oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.app.util.OraclePasswdUtil;
import usi.dbdp.portal.task.dao.UserOrgIncDao;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;
import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.entity.UicOrgInc;
import usi.dbdp.uic.entity.UicUserInc;
import usi.dbdp.uic.entity.User;
@OracleDb
@Repository
public class UserOrgIncDaoImpl extends JdbcDaoSupport4oracle implements UserOrgIncDao {

	@Override
	public User getUserByUserIdForInc(Long id) {
		String sql="select " +
				"  a.create_time,a.duration,a.gender,a.id,a.lst_err_pwd_time,"+OraclePasswdUtil.getDecryptStr("a.mobile_no")+" as mobile_no,a.password,a.pwd_err_cnt,a.pwd_last_mod_time,a.del_flag, " + 
				"  a.user_id,"+OraclePasswdUtil.getDecryptStr("a.user_name")+" as user_name,a.login_id,a.user_type " + 
				"  from uic_user a  " + 
				"  where a.id=? ";
		List<User>	users =	this.getJdbcTemplate().query(sql, new Object[] {id}, new RowMapper<User>(){

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User  user= new User();
				user.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
				user.setDuration(rs.getInt("duration"));
				user.setGender(rs.getInt("gender"));
				user.setId(rs.getLong("id"));
				user.setLstErrPwdTime(new Date(rs.getTimestamp("lst_err_pwd_time").getTime()));
				user.setMobileNo(rs.getString("mobile_no"));
				user.setPassword(rs.getString("password"));
				user.setPwdErrCnt(rs.getInt("pwd_err_cnt"));
				user.setPwdLastModTime(new Date(rs.getTimestamp("pwd_last_mod_time").getTime()));
				user.setDelFlag(rs.getInt("del_flag"));
				user.setUserId(rs.getString("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setLoginId(rs.getString("login_id"));
				user.setUserType(rs.getInt("user_type"));
				return user;
			}
			
		});
		User user=null;
		if(users.size()>0){
			user=users.get(0);
		}
		return user; 
		}

	@Override
	public boolean insertIncUser(final User user, final String incType) {
		String sql="insert into uic_user_inc (id,user_id,user_name,login_id,gender,mobile_no,user_type,del_flag,password,duration,pwd_last_mod_time,lst_err_pwd_time,pwd_err_cnt,create_time,inc_type,occur_time) values(?,?,"+OraclePasswdUtil.getEncryptStr("?")+",?,?,"+OraclePasswdUtil.getEncryptStr("?")+",?,?,?,?,?,?,?,?,?,sysdate)";
		return  this.getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, user.getId());
				ps.setString(2, user.getUserId());
				ps.setString(3, user.getUserName());
				ps.setString(4, user.getLoginId());
				ps.setInt(5, user.getGender());
				ps.setString(6, user.getMobileNo());
				ps.setInt(7, user.getUserType());
				ps.setInt(8, user.getDelFlag());
				ps.setString(9, user.getPassword());
				ps.setInt(10, user.getDuration());
				ps.setTimestamp(11, new Timestamp(user.getPwdLastModTime().getTime()));
				ps.setTimestamp(12,new Timestamp( user.getLstErrPwdTime().getTime()));
				ps.setInt(13, user.getPwdErrCnt());
				ps.setTimestamp(14,new Timestamp( user.getCreateTime().getTime()));
				ps.setString(15,incType);
			}
		}) >0 ;
	}

	@Override
	public boolean insertIncOrg(final Org org, final String incType) {
		String sql="insert into uic_org_inc (org_id,org_code,org_name,p_org_id,org_grade,administrative_grade,org_code_seq,org_id_seq,org_name_seq,display_order,is_leaf,del_flag,org_memo,inc_type,occur_time) values(?,?,"+OraclePasswdUtil.getEncryptStr("?")+",?,?,?,?,?,"+OraclePasswdUtil.getEncryptStr("?")+",?,?,?,?,?,sysdate)";
		
		return  this.getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, org.getOrgId());
				ps.setString(2, org.getOrgCode());
				ps.setString(3, org.getOrgName());
				ps.setLong(4, org.getpOrgId());
				ps.setInt(5, org.getOrgGrade());
				ps.setInt(6, org.getAdministrativeGrade());
				ps.setString(7, org.getOrgCodeSeq());
				ps.setString(8,org.getOrgIdSeq());
				ps.setString(9, org.getOrgNameSeq());
				ps.setInt(10, org.getDisplayOrder());
				ps.setInt(11, org.getIsLeaf());
				ps.setInt(12, org.getDelFlag());
				ps.setString(13, org.getOrgMemo());
				ps.setString(14,incType);
			}
		}) >0 ;
	}
	@Override
	public List<UicOrgInc> getUicOrgIncByDate(String date) {
		String sql = "select  ORG_ID , ORG_CODE ,"+OraclePasswdUtil.getDecryptStr("ORG_NAME")+" AS ORG_NAME , "
				+ "P_ORG_ID , ORG_GRADE , ADMINISTRATIVE_GRADE , "
				+ "ORG_CODE_SEQ , ORG_ID_SEQ ,"+OraclePasswdUtil.getDecryptStr("ORG_NAME_SEQ")+" AS ORG_NAME_SEQ , "
				+ "DISPLAY_ORDER , IS_LEAF , DEL_FLAG ,ORG_MEMO,INC_TYPE,OCCUR_TIME FROM UIC_ORG_INC WHERE OCCUR_TIME <=? ORDER BY occur_time " ;
		List<UicOrgInc> list = this.getJdbcTemplate().query(sql, new Object[]{date},new RowMapper<UicOrgInc>(){
			@Override
			public UicOrgInc mapRow(ResultSet rs, int rowNum) throws SQLException {
				UicOrgInc org = new UicOrgInc();
				org.setOrgId(rs.getLong("ORG_ID"));
				org.setOrgCode(rs.getString("ORG_CODE"));
				org.setOrgName(rs.getString("ORG_NAME"));
				org.setpOrgId(rs.getLong("P_ORG_ID"));
				org.setOrgGrade(rs.getInt("ORG_GRADE"));
				org.setAdministrativeGrade(rs.getInt("ADMINISTRATIVE_GRADE"));
				org.setOrgCodeSeq(rs.getString("ORG_CODE_SEQ"));
				org.setOrgIdSeq(rs.getString("ORG_ID_SEQ"));
				org.setOrgNameSeq(rs.getString("ORG_NAME_SEQ"));
				org.setDisplayOrder(rs.getInt("DISPLAY_ORDER"));
				org.setIsLeaf(rs.getInt("IS_LEAF"));
				org.setDelFlag(rs.getInt("DEL_FLAG"));
				org.setOrgMemo(rs.getString("ORG_MEMO"));
				org.setIncType(rs.getString("INC_TYPE"));
				org.setOccurTime(new Date(rs.getTimestamp("OCCUR_TIME").getTime()));
				return org;
			}
		});
		return list;
	}

	@Override
	public int deleteUicOrgIncByDate(String date) {
		String sql = "delete from uic_org_inc WHERE OCCUR_TIME <=?";
		return this.getJdbcTemplate().update(sql, new Object[]{date});
	}

	@Override
	public List<UicUserInc> getUicUserIncByDate(String date) {
		String sql = "select ID,"
				+ "USER_ID,"
				+OraclePasswdUtil.getDecryptStr("USER_NAME")
				+ " as USER_NAME,"
				+ "LOGIN_ID,"
				+ "GENDER,"
				+OraclePasswdUtil.getDecryptStr("MOBILE_NO")
				+ " as MOBILE_NO,"
				+ "USER_TYPE,"
				+ "DEL_FLAG,"
				+ "PASSWORD,"
				+ "DURATION,"
				+ "PWD_LAST_MOD_TIME,"
				+ "LST_ERR_PWD_TIME,"
				+ "PWD_ERR_CNT,"
				+ "CREATE_TIME,"
				+ "INC_TYPE,OCCUR_TIME from uic_user_inc WHERE OCCUR_TIME <=? ORDER BY occur_time  " ; 
		List<UicUserInc>	users =	this.getJdbcTemplate().query(sql,new Object[]{date}, new RowMapper<UicUserInc>(){
			@Override
			public UicUserInc mapRow(ResultSet rs, int rowNum) throws SQLException {
				UicUserInc  user= new UicUserInc();
				user.setId(rs.getLong("id"));
				user.setUserId(rs.getString("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setLoginId(rs.getString("login_id"));
				user.setGender(rs.getInt("gender"));
				user.setMobileNo(rs.getString("mobile_no"));
				user.setUserType(rs.getInt("user_type"));
				user.setDelFlag(rs.getInt("del_flag"));
				user.setPassword(rs.getString("password"));
				user.setDuration(rs.getInt("duration"));
				user.setPwdLastModTime(new Date(rs.getTimestamp("pwd_last_mod_time").getTime()));
				user.setLstErrPwdTime(new Date(rs.getTimestamp("lst_err_pwd_time").getTime()));
				user.setPwdErrCnt(rs.getInt("pwd_err_cnt"));
				user.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
				user.setIncType(rs.getString("INC_TYPE"));
				user.setOccurTime(new Date(rs.getTimestamp("OCCUR_TIME").getTime()));
				return user;
			}});
		return users;
	}

	@Override
	public int deleteUicUserIncByDate(String date) {
		String sql = "delete from uic_user_inc WHERE OCCUR_TIME <=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		return this.getJdbcTemplate().update(sql, new Object[]{date});
	}

	
}
