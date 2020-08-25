package usi.dbdp.uic.base.dao.impl4mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.base.dao.OrgDao;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.util.CommonUtil;


/**
 * 机构服务dao
 * @author nie.zhengqian
 * 创建时间：2015年3月3日 下午6:27:11
 */
@MysqlDb
@Repository
public class OrgDaoImpl extends JdbcDaoSupport4mysql implements OrgDao{
	
	/**
	 * 增加一个机构(返回主键id)
	 * @param org
	 * @return
	 */
	public long saveSubOrg(final Org org){
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String hsql = " INSERT INTO uic_org ("
						  +" org_code,ORG_NAME,"
						  +" P_ORG_ID,"
						  +" ORG_GRADE,"
						  +" DISPLAY_ORDER,"
						  +" del_flag,"
						  +" ORG_MEMO,"
						  +" ORG_ID_SEQ,"
						  +" ORG_NAME_SEQ," 
						  +" IS_LEAF,"  
						  +" ADMINISTRATIVE_GRADE ,"  
						  +" ORG_CODE_SEQ "  
						  +" ) "
						  +" VALUES"
						  +" ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?) ";
				PreparedStatement ps = con.prepareStatement(hsql,
						new String[] { "org_id" });
					ps.setString(1, org.getOrgCode());
					ps.setString(2, org.getOrgName());
					ps.setLong(3, org.getpOrgId());
					ps.setInt(4, org.getOrgGrade());
					ps.setInt(5, org.getDisplayOrder());
					ps.setInt(6, org.getDelFlag());
					ps.setString(7, org.getOrgMemo());
					ps.setString(8, org.getOrgIdSeq());
					ps.setString(9, org.getOrgNameSeq());
					ps.setInt(10, org.getIsLeaf());
					ps.setInt(11, org.getAdministrativeGrade());
					ps.setString(12, org.getOrgCodeSeq());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}
	/**
	 * 更新机构 id_seq和name_seq
	 * @param seq
	 * @param orgId
	 */
	public int saveOrgSeq(final String orgIdSeq, final String orgNameSeq, final long orgId) {
		String sql = "update uic_org  set ORG_ID_SEQ = ? , ORG_NAME_SEQ = ? where ORG_ID = ? ";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, orgIdSeq);
				ps.setString(2, orgNameSeq);
				ps.setLong(3, orgId);
			}
		});
	}
	/**
	 * 更新机构
	 * @param org
	 * @return
	 */
	public int updateOrg(final Org org) {
		String sql =" update uic_org set"
				  +" ORG_NAME = ?,"
				  +" P_ORG_ID = ?,"
				  +" ORG_GRADE = ?,"
				  +" DISPLAY_ORDER = ?,"
				  +" del_flag = ?,"
				  +" ORG_MEMO = ?,"
				  +" ORG_ID_SEQ = ?,"
				  +" ORG_NAME_SEQ = ?," 
				  +" IS_LEAF = ?,"  
				  +" ADMINISTRATIVE_GRADE = ?,"  
				  +" ORG_CODE = ? ,"  
				  +" ORG_CODE_SEQ = ? "  
				  +" where ORG_ID = ? ";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, org.getOrgName());
				ps.setLong(2, org.getpOrgId());
				ps.setInt(3, org.getOrgGrade());
				ps.setInt(4, org.getDisplayOrder());
				ps.setInt(5, org.getDelFlag());
				ps.setString(6, org.getOrgMemo());
				ps.setString(7, org.getOrgIdSeq());
				ps.setString(8, org.getOrgNameSeq());
				ps.setInt(9, org.getIsLeaf());
				ps.setInt(10, org.getAdministrativeGrade());
				ps.setString(11, org.getOrgCode());
				ps.setString(12, org.getOrgCodeSeq());
				ps.setLong(13, org.getOrgId());
			}
		});
	}
	/**
	 * 查询某机构信息
	 * @param orgId
	 * @return
	 */
	public List<Org> queryOrgById(long orgId){
		String sql = "SELECT "
				  +" ORG_ID,"
				  +" ORG_CODE,"
				  +" ORG_CODE_SEQ,"
				  +" ORG_NAME,"
				  +" P_ORG_ID,"
				  +" ORG_GRADE,"
				  +" DISPLAY_ORDER,"
				  +" DEL_FLAG,"
				  +" ORG_MEMO,"
				  +" ORG_ID_SEQ,"
				  +" ORG_NAME_SEQ," 
				  +" ADMINISTRATIVE_GRADE," 
				  +" (case IS_LEAF when 0 then 'true' else 'false' end ) is_parent ,"  
				  +" IS_LEAF"  
				  +" FROM UIC_ORG"
				  +" WHERE ORG_ID = ? ";
		return this.getJdbcTemplate().query(sql, new Object[]{orgId}, new RowMapper<Org>(){
			@Override
			public Org mapRow(ResultSet rs, int rowNum) throws SQLException {
				Org org = new Org();
				org.setOrgId(rs.getLong("ORG_ID"));
				org.setOrgName(rs.getString("ORG_NAME"));
				org.setpOrgId(rs.getLong("P_ORG_ID"));
				org.setOrgGrade(rs.getInt("ORG_GRADE"));
				org.setDisplayOrder(rs.getInt("DISPLAY_ORDER"));
				org.setDelFlag(rs.getInt("del_flag"));
				org.setOrgMemo(rs.getString("ORG_MEMO"));
				org.setOrgIdSeq(rs.getString("ORG_ID_SEQ"));
				org.setOrgNameSeq(rs.getString("ORG_NAME_SEQ"));
				org.setIsLeaf(rs.getInt("IS_LEAF"));
				org.setAdministrativeGrade(rs.getInt("ADMINISTRATIVE_GRADE"));
				org.setIsParent(rs.getString("is_parent"));
				org.setOrgCode(rs.getString("ORG_CODE"));
				org.setOrgCodeSeq(rs.getString("ORG_CODE_SEQ"));
				return org;
			}
		});
	}
	/**
	 * 删除机构（逻辑删除，设置机构状态为失效） 是否需要级联删除用户？？？
	 * @param orgId
	 * @return
	 */
	public int deleteOrgById(final Long orgId) {
		String sql = "update uic_org set del_flag = 1 where org_id = ? ";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, orgId);
			}
		});
	}

	/**
	 * 获取机构的直接子机构
	 * @param orgId
	 * @return
	 */
	public List<Org> getDirectSubOrgsById(long orgId){
		String sql = "SELECT "
				  +" ORG_ID,"
				  +" ORG_CODE,"
				  +" ORG_CODE_SEQ,"
				  +" ORG_NAME,"
				  +" P_ORG_ID,"
				  +" ORG_GRADE,"
				  +" DISPLAY_ORDER,"
				  +" del_flag ,"
				  +" ORG_MEMO,"
				  +" ORG_ID_SEQ,"
				  +" ORG_NAME_SEQ," 
				  +" IS_LEAF,"  
				  +" ADMINISTRATIVE_GRADE,"  
				  +" (case IS_LEAF when 0 then 'true' else 'false' end ) is_parent"  
				  +" FROM UIC_ORG"
				  +" WHERE P_ORG_ID = ? AND  del_flag = 0"
				  +" order by DISPLAY_ORDER ";
		return this.getJdbcTemplate().query(sql, new Object[]{orgId}, new RowMapper<Org>(){
			@Override
			public Org mapRow(ResultSet rs, int rowNum) throws SQLException {
				Org org = new Org();
				org.setOrgId(rs.getLong("ORG_ID"));
				org.setOrgName(rs.getString("ORG_NAME"));
				org.setpOrgId(rs.getLong("P_ORG_ID"));
				org.setOrgGrade(rs.getInt("ORG_GRADE"));
				org.setDisplayOrder(rs.getInt("DISPLAY_ORDER"));
				org.setDelFlag(rs.getInt("del_flag"));
				org.setOrgMemo(rs.getString("ORG_MEMO"));
				org.setOrgIdSeq(rs.getString("ORG_ID_SEQ"));
				org.setOrgNameSeq(rs.getString("ORG_NAME_SEQ"));
				org.setIsLeaf(rs.getInt("IS_LEAF"));
				org.setAdministrativeGrade(rs.getInt("ADMINISTRATIVE_GRADE"));
				org.setIsParent(rs.getString("is_parent"));
				org.setOrgCode(rs.getString("ORG_CODE"));
				org.setOrgCodeSeq(rs.getString("ORG_CODE_SEQ"));
				return org;
			}
		});
	}
	/**
	 * 获取当前当前机构ID的机构ID序列
	 * @param orgId
	 * @return orgIdSeq 
	 */
	public List<Org> getOrgIdSeqByOrgId(long orgId){
		String sql="SELECT c.org_id_seq FROM  uic_org  c  WHERE c.org_id = ?";
		return this.getJdbcTemplate().query(sql, new Object[]{orgId}, new RowMapper<Org>(){
			@Override
			public Org mapRow(ResultSet rs, int rowNum) throws SQLException {
				Org org = new Org();
				org.setOrgIdSeq(rs.getString("ORG_ID_SEQ"));
				return org;
			}
		});
	}
	/**
	 * 获取当前登录人机构下用户列表（分页）
	 * @param orgId
	 * @param pageObj
	 * @return
	 */
	public List<UserInfo> getAllUsersByOrgIdWithPage(String orgIdSeq, String userId, String userName,PageObj pageObj){
		String sql =    " SELECT a.ID," +
						"         a.USER_ID," + 
						"         a.USER_NAME," + 
						"         a.LOGIN_ID," + 
						"         a.GENDER," + 
						"         a.MOBILE_NO," + 
						"         a.USER_TYPE," + 
						"         b.ORG_ID," + 
						"         a.del_flag ," + 
						"         a.STATUS,    "+
						"         a.DURATION," + 
						"         a.PWD_ERR_CNT," + 
						"         DATE_FORMAT(a.CREATE_TIME, '%Y-%m-%d %H:%i:%S') CREATE_TIME," + 
						"         b.ORG_NAME," +
						"         uo.type ," +
						"         b.ORG_NAME_SEQ" + 
						"    FROM UIC_USER a , uic_org b, uic_user_has_org  uo  WHERE a.id = uo.id and uo.org_id=b.org_id  " + 
						"   and  a.del_flag=0  AND b.del_flag = 0 " + 
						"   AND  b.org_id_seq LIKE '"+orgIdSeq+"' ";
		if(CommonUtil.hasValue(userId)){
			sql +=" and a.USER_ID like '%" + userId + "%' ";
		}
		if(CommonUtil.hasValue(userName)){
			sql +=" and a.USER_NAME like '%" + userName + "%' ";
		}
		//mysql汉字排序（按照汉字首字母拼音排序）
		sql += " ORDER BY  CONVERT( a.USER_NAME USING gbk )  ";
		return this.queryByPage(sql, new RowMapper<UserInfo>(){
			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo user = new UserInfo();
				user.setId(rs.getLong("ID"));
				user.setUserId(rs.getString("USER_ID"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setLoginId(rs.getString("LOGIN_ID"));
				user.setGender(rs.getInt("GENDER"));
				user.setMobileNo(rs.getString("MOBILE_NO"));
				user.setUserType(rs.getInt("USER_TYPE"));
				user.setOrgId(rs.getLong("ORG_ID"));
				user.setDelFlag(rs.getInt("del_flag"));
				user.setType(rs.getInt("type"));
				user.setDuration(rs.getInt("DURATION"));
				user.setPwdErrCnt(rs.getInt("PWD_ERR_CNT"));
				user.setCreateTime(rs.getString("CREATE_TIME"));
				user.setOrgName(rs.getString("ORG_NAME"));
				user.setOrgNameSeq(rs.getString("ORG_NAME_SEQ"));
				user.setStatus(rs.getString("STATUS"));
				return user;
			}
		}, pageObj);
	}
	/**
	 * 获取机构下用户列表（分页）
	 * @param orgId
	 * @param pageObj
	 * @return
	 */
	public List<UserInfo> getUsersByOrgIdWithPage(long orgId, String userId, String userName,PageObj pageObj){
		String sql = "     SELECT a.ID, " +
				"      a.USER_ID, " + 
				"      a.LOGIN_ID, " + 
				"      a.USER_NAME, " + 
				"      a.GENDER, " + 
				"      a.MOBILE_NO, " + 
				"      a.USER_TYPE, " + 
				"      b.ORG_ID, " +
				"      a.STATUS,    "+
				"      b.ORG_NAME, " + 
				"      a.del_flag , " + 
				"      uo.type , " + 
				"      DATE_FORMAT(a.CREATE_TIME, '%Y-%m-%d %H:%i:%S') CREATE_TIME," + 
				"      b.org_name_seq " + 
				" FROM UIC_USER a , uic_org b,uic_user_has_org uo   WHERE a.id = uo.id and uo.org_id=b.org_id " + 
				" and b.ORG_ID = ? " + 
				"  AND a.del_flag = 0  AND b.del_flag = 0  ";
		if(CommonUtil.hasValue(userId)){
			sql +=" and USER_ID like '%" + userId + "%' ";
		}
		if(CommonUtil.hasValue(userName)){
			sql +=" and USER_NAME like '%" + userName + "%' ";
		}
		sql += " ORDER BY  CONVERT( a.USER_NAME USING gbk )  ";
		return this.queryByPage(sql, new Object[]{orgId}, new RowMapper<UserInfo>(){
			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo user = new UserInfo();
				user.setId(rs.getLong("ID"));
				user.setUserId(rs.getString("USER_ID"));
				user.setLoginId(rs.getString("LOGIN_ID"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setGender(rs.getInt("GENDER"));
				user.setMobileNo(rs.getString("MOBILE_NO"));
				user.setUserType(rs.getInt("USER_TYPE"));
				user.setOrgId(rs.getLong("ORG_ID"));
				user.setOrgName(rs.getString("ORG_NAME"));
				user.setDelFlag(rs.getInt("del_flag"));
				user.setType(rs.getInt("type"));
				user.setCreateTime(rs.getString("CREATE_TIME"));
				user.setOrgNameSeq(rs.getString("org_name_seq"));
				user.setStatus(rs.getString("STATUS"));
				return user;
			}
		}, pageObj);
	}

	
	/**
	 * 根据orgIdSeq返回所有父机构（包括自己）
	 * @param orgIdSeq 机构id序列
	 * @return 返回所有父机构（包括自己）
	 */
	public List<Org> getParentOrgsByOrgIdSeq(String orgIdSeq){
		String sql = "select o.org_id,o.org_name,o.org_grade,o.administrative_grade from uic_org o where ? like concat(org_id_seq,'%') AND o.del_flag=0 order by o.org_grade";
		return this.getJdbcTemplate().query(sql, new Object[] {orgIdSeq}, new OrgSilmpleRowMapper());
	}

	
	/**
	 * 封装Org对象的内部类(简单使用)
	 * @author ma.guangming
	 *
	 */
	private static class OrgSilmpleRowMapper  implements RowMapper<Org>{
		@Override
		public Org mapRow(ResultSet rs, int rowNum) throws SQLException {
			Org org = new Org();
			org.setOrgId(rs.getLong("org_id"));
			org.setOrgName(rs.getString("org_name"));
			org.setOrgGrade(rs.getInt("org_grade"));
			org.setAdministrativeGrade(rs.getInt("administrative_grade"));
			return org;
		}
	}
	/**
	 * 查询用户所在机构信息
	 * @param loginId 登录账号
	 * @return
	 */
	public Org getOrgByLoginId(String loginId){
		String sql = "SELECT " +
					"  a.ORG_ID, " + 
					"  a.ORG_ID_SEQ, " + 
					"  a.ORG_GRADE " + 
					" FROM " + 
					"  uic_org a, " + 
					"  uic_user b, " + 
					"  uic_user_has_org uo " + 
					" WHERE a.org_id = uo.org_id " + 
					"  AND b.login_id = ? " + 
					"  AND a.org_id = uo.org_id " + 
					"  AND b.id = uo.id " + 
					"  AND uo.type = 1";

		List<Org> list = this.getJdbcTemplate().query(sql, new Object[]{loginId}, new RowMapper<Org>(){

			@Override
			public Org mapRow(ResultSet rs, int rowNum) throws SQLException {
				Org org = new Org();
				org.setOrgId(rs.getLong("ORG_ID"));
				org.setOrgIdSeq(rs.getString("ORG_ID_SEQ"));
				org.setOrgGrade(rs.getInt("ORG_GRADE"));
				return org;
			}
		});
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 更新org_id_seq
	 */
	@Override
	public void updateOrgIdSeq(long orgId, String newOrgIdSeq) {
		String sql = " update uic_org set org_id_seq = ? where org_id = ? ";
		this.getJdbcTemplate().update(sql, new Object[]{newOrgIdSeq,orgId});
	}
	@Override
	public List<Org> getAllOrg() {
		String sql = "select  ORG_ID , ORG_CODE , ORG_NAME , "
				+ "P_ORG_ID , ORG_GRADE , ADMINISTRATIVE_GRADE , "
				+ "ORG_CODE_SEQ , ORG_ID_SEQ , ORG_NAME_SEQ , "
				+ "DISPLAY_ORDER , IS_LEAF , DEL_FLAG ,ORG_MEMO FROM UIC_ORG " ;
		List<Org> list = this.getJdbcTemplate().query(sql, new RowMapper<Org>(){
			@Override
			public Org mapRow(ResultSet rs, int rowNum) throws SQLException {
				Org org = new Org();
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
				return org;
			}
		});
		return list;
	}
	/**
	 * 封装Org对象的内部类
	 * @author liu.tan
	 *
	 */
	private static class OrgRowMapper  implements RowMapper<Org>{
		@Override
		public Org mapRow(ResultSet rs, int rowNum) throws SQLException {
			Org org = new Org();
			org.setOrgId(rs.getLong("org_id"));
			org.setOrgName(rs.getString("org_name"));
			org.setpOrgId(rs.getLong("p_org_id"));
			org.setOrgGrade(rs.getInt("org_grade"));
			org.setDisplayOrder(rs.getInt("display_order"));
			//org.setState(rs.getInt("state"));
			org.setOrgMemo(rs.getString("org_memo"));
			org.setOrgIdSeq(rs.getString("org_id_seq"));
			org.setOrgNameSeq(rs.getString("org_name_seq"));
			org.setIsLeaf(rs.getInt("is_leaf"));
			//org.setIsAdministrative(rs.getInt("IS_ADMINISTRATIVE"));
			return org;
		}
	}
	/**
	 * @author liu.tan
	 * 查询用户所在机构及其子机构
	 * @param userId 登录帐号
	 * @return 机构列表
	 */
	public List<Org> getAllOrgsByUserId(String userId){
		String sql="select o.org_id,o.org_name,o.p_org_id,o.org_grade,o.display_order,o.org_memo,o.org_id_seq,o.org_name_seq,o.is_leaf"+
						" from uic_org o"+
						" where o.org_id_seq like ("+  
						" select concat(o1.org_id_seq,'%') from uic_user u,uic_org o1,uic_user_has_org a"+ 
						" where u.user_id =?"+
						" and u.id = a.id "+
						" and a.org_id=o1.org_id)";
		return this.getJdbcTemplate().query(sql,new Object[]{userId}, new OrgRowMapper());
	}
	@Override
	public void updateChildOrgSeq(String orgIdSeq, String newOrgSeqName, String oldOrgSeqname) {
		int oldSeqLenth=oldOrgSeqname.length()+1;
		String sql="SELECT UO.ORG_ID,"
				+ "("+newOrgSeqName+"|| SUBSTR(UO.ORG_NAME_SEQ,"+oldSeqLenth+",LENGTH(UO.ORG_NAME_SEQ))) ORGNAMESEQ"
				+ " FROM UIC_ORG UO WHERE UO.ORG_ID_SEQ LIKE '"+orgIdSeq+"%'";
		final List<Org> orgList=this.getJdbcTemplate().query(sql, new RowMapper<Org>(){
			@Override
			public Org mapRow(ResultSet rs, int arg1) throws SQLException {
				Org org = new Org();
				org.setOrgId(rs.getLong("ORG_ID"));
				org.setOrgNameSeq(rs.getString("ORGNAMESEQ"));
				return org;
			}
		});
		String updateSql="update UIC_ORG set ORG_NAME_SEQ=? where ORG_ID=?";
		this.getJdbcTemplate().batchUpdate(updateSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				Org org = orgList.get(i);
				ps.setString(1, org.getOrgNameSeq());
				ps.setLong(2, org.getOrgId());
			}
			public int getBatchSize() {
				return orgList.size();
			}
		});
	}
}