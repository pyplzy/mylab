package usi.dbdp.uic.a2a.dao.impl4mysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.a2a.dao.AccountingDao;
import usi.dbdp.uic.dto.UserInfo4Session;
import usi.dbdp.uic.entity.User;
/**
 * 用户管理dao
 * @author ma.guangming
 */
@MysqlDb
@Repository
public class AccountingDaoImpl  extends JdbcDaoSupport4mysql implements AccountingDao{
	/**
	 * 设置用户状态为停用状态
	 * @param userId 工号
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean setUserStopByUserId(final String userId){
		String sql="update uic_user set del_flag=1   where user_id = ? ";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
		}) >0 ;
	}
	
	/**
	 * 增加用户
	 * @param user 用户信息模型
	 * @return true表示操作成功，false表示操作失败
	 */
	public long addUser(final User user){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String sql="insert into "
						+ "uic_user(user_id,user_name,gender,mobile_no,user_type,del_flag,password,duration,pwd_last_mod_time,pwd_err_cnt,create_time,login_id) "
						+ "values(?,?,?,?,?,?,?,90,now(),0,now(),?) ";
				PreparedStatement ps = con.prepareStatement(sql,
						new String[] { "id" });
				ps.setString(1, user.getUserId());
				ps.setString(2, user.getUserName());
				ps.setInt(3, user.getGender());
				ps.setString(4, user.getMobileNo());
				ps.setInt(5, user.getUserType());
				ps.setInt(6, user.getDelFlag());
				ps.setString(7, user.getPassword());
				ps.setString(8, user.getLoginId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}
	/**
	 * 更新用户
	 * @param user 用户信息模型
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateUser(final User user){
		String sql="update uic_user "
				+ "set user_name=?,gender=?,del_flag=?,password=?,pwd_last_mod_time=now(),lst_err_pwd_time=?,pwd_err_cnt=?,mobile_no = ? ,user_type= ? ,create_time = ?,login_id=?   "
				+ "WHERE  user_id =? ";
		return this.getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getUserName());
				ps.setInt(2, user.getGender());
				ps.setInt(3, user.getDelFlag());
				ps.setString(4, user.getPassword());
				ps.setTimestamp(5, new java.sql.Timestamp(user.getLstErrPwdTime().getTime()));
				ps.setInt(6, user.getPwdErrCnt());
				ps.setString(7, user.getMobileNo());
				ps.setInt(8, user.getUserType());
				ps.setTimestamp(9, new java.sql.Timestamp(user.getCreateTime().getTime()));
				ps.setString(10, user.getLoginId());
				ps.setString(11, user.getUserId());
			}
		}) >0 ;
	}
	
	
	/**
	 * 根据原密码更新用户密码
	 * @param userId 工号
	 * @param oldPassword 原密码
	 * @param newPassword 新密码
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	public boolean updateUserPasswordByLoginId(final String loginId,final String oldPassword, final String newPassword) {
		String sql="update uic_user set password=? , pwd_err_cnt = 0 ,  pwd_last_mod_time =now()  WHERE login_id=? and password=?";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, newPassword);
				ps.setString(2, loginId);
				ps.setString(3, oldPassword);
			}
		}) >0  ;
	}	
	
	/**
	 * 根据工号获取用户信息
	 * @param userId 工号
	 * @return 用户信息模型
	 */
	@Override
	public User getUserByUserId(String userId){
		String sql="select " +
				"  a.create_time,a.duration,a.gender,a.id,a.lst_err_pwd_time,a.mobile_no,b.org_id,a.password,a.pwd_err_cnt,a.pwd_last_mod_time,a.del_flag, " + 
				"  a.user_id,a.user_name,a.login_id,a.user_type   ,b.org_name " + 
				"  from uic_user a  ,uic_org b,uic_user_has_org uo  " + 
				"  where  a.del_flag = 0 and a.id = uo.id and b.org_id=uo.org_id and uo.type=1 and b.del_flag = 0 and  a.user_id =? ";
		List<User>	users =	this.getJdbcTemplate().query(sql, new Object[]{userId}, new UserRowMapper() );
		User user=null;
		if(users.size()>0){
			user=users.get(0);
		}
		return user; 
	}
	
	/**
	 * 根据登录名获取用户信息
	 * @param loginId 登录名
	 * @return 用户信息模型
	 */
	@Override
	public User getUserByLoginId(String loginId) {
		String sql="select " +
				"  a.create_time,a.duration,a.gender,a.id,a.lst_err_pwd_time,a.mobile_no,b.org_id,a.password,a.pwd_err_cnt,a.pwd_last_mod_time,a.del_flag, " + 
				"  a.user_id,a.user_name,a.login_id,a.user_type   ,b.org_name " + 
				"  from uic_user a  ,uic_org b,uic_user_has_org uo  " + 
				"  where  a.del_flag = 0  and a.id = uo.id  and b.org_id=uo.org_id   and b.del_flag = 0   and uo.type=1 and  a.login_id=? ";
		List<User>	users =	this.getJdbcTemplate().query(sql, new Object[]{loginId}, new UserRowMapper() );
		User user=null;
		if(users.size()>0){
			user=users.get(0);
		}
		return user; 
	}	
	
	
	/**
	 * 重置密码
	 * @param loginId 登录号
	 * @param password 初始密码
	 * @return true 成功 false 失败
	 */
	@Override
	public boolean resetPasswordByLoginId(final String loginId,final String password) {
		String sql =" update  uic_user set password = ? where login_id = ?  ";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, password);
				ps.setString(2, loginId);
			}
		}) >0  ;
	}	
	
	/**
	 * 根据登录号获取用户信息，具体见UserInfo4Session
	 * @param loginId 登录号
	 * @return UserInfo4Session 包含用户信息
	 */
	@Override
	public UserInfo4Session getUserInfo4SessionByLoginId(String loginId) {
		String sql = "select u.id,u.user_id,u.user_name,u.gender,u.mobile_no,u.user_type,o.org_id,o.org_name,o.org_id_seq,o.org_name_seq,u.login_id,u.del_flag"
				+" from uic_user u,uic_user_has_org uo,uic_org o "
				+" where u.id = uo.id  and o.org_id=uo.org_id  and uo.type=1  and u.login_id=?" ;
		List<UserInfo4Session> tmpLst = this.getJdbcTemplate().query(sql, new Object[]{loginId}, new UserInfo4SessionRowMapper());
		return tmpLst.size()>0?tmpLst.get(0):null;
	}
	/**
	 * 封装User对象的内部类
	 */
	private static class UserRowMapper implements RowMapper<User>{
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			//对类进行封装
			User  user= new User();
			user.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
			user.setDuration(rs.getInt("duration"));
			user.setGender(rs.getInt("gender"));
			user.setId(rs.getLong("id"));
			user.setLstErrPwdTime(new Date(rs.getTimestamp("lst_err_pwd_time").getTime()));
			user.setMobileNo(rs.getString("mobile_no"));
			user.setOrgId(rs.getLong("org_id"));
			user.setPassword(rs.getString("password"));
			user.setPwdErrCnt(rs.getInt("pwd_err_cnt"));
			user.setPwdLastModTime(new Date(rs.getTimestamp("pwd_last_mod_time").getTime()));
			user.setDelFlag(rs.getInt("del_flag"));
			user.setUserId(rs.getString("user_id"));
			user.setUserName(rs.getString("user_name"));
			user.setLoginId(rs.getString("login_id"));
			user.setUserType(rs.getInt("user_type"));
			user.setOrgName(rs.getString("org_name"));
			return user;
		}
	}
	
	/**
	 * 封装UserInfo4Session对象的内部类
	 */
	private static class UserInfo4SessionRowMapper implements RowMapper<UserInfo4Session>{
		@Override
		public UserInfo4Session mapRow(ResultSet rs, int rowNum) throws SQLException {
			//对类进行封装
			UserInfo4Session obj= new UserInfo4Session();
			obj.setId(rs.getLong("id"));
			obj.setUserId(rs.getString("user_id"));
			obj.setUserName(rs.getString("user_name"));
			obj.setGender(rs.getInt("gender"));
			obj.setMobileNo(rs.getString("mobile_no"));
			obj.setUserType(rs.getInt("user_type"));
			obj.setOrgId(rs.getLong("org_id"));
			obj.setOrgName(rs.getString("org_name"));
			obj.setOrgIdSeq(rs.getString("org_id_seq"));
			obj.setOrgNameSeq(rs.getString("org_name_seq"));
			obj.setLoginId(rs.getString("login_id"));
			obj.setDelFlag(rs.getInt("del_flag"));
			return obj;
		}
	}	
	/**
	 * 判断userId是否可用
	 * @param userId 工号
	 * @return 1表示有，0表示没有
	 */
	@Override
	public int checkUserIdInUse(String userId){
		String sql = "select count(*) from uic_user where user_id = ?";
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{userId},Integer.class);
	}
	/**
	 * 判断loginId是否可用
	 * @param loginId 登录帐号
	 * @return 1表示有，0表示没有
	 */
	@Override
	public int checkLoginIdInUse(String loginId) {
		String sql = "select count(*) from uic_user where login_id = ?";
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{loginId},Integer.class);
	}
	/**
	 * 删除角色用户管理表
	 * @param userId 工号
	 */
	public void delUserRoleRel(String userId) {
		String sql ="DELETE FROM uic_user_has_role  WHERE id in (SELECT id FROM uic_user WHERE user_id = ?)";
		this.getJdbcTemplate().update(sql, userId);
	}
	/**
	 * 用户表userId 变为 id+userid
	 * @param userId 工号
	 */
	public void updateUserIdByUserId(String userId) {
		String sql = "update uic_user a set a.user_id =concat(a.id,a.user_id),a.login_id=concat(a.id,a.login_id) where a.user_id=?";
		this.getJdbcTemplate().update(sql, userId);
	}
	
	/**
	 * 修改用户机构
	 */
	@Override
	public void updateUserHasOrg(User user, long orgId) {
		String sql = " update uic_user_has_org set ORG_ID =? where org_id = ? and id =? ";
		this.getJdbcTemplate().update(sql, new Object[]{
				user.getOrgId(),
				orgId,user.getId()
		});
	}
	@Override
	public void delUserOrgRel(String userId, int type) {
		
	}
	/**
	 * uic_user_has_org删除 by id 和org_id
	 */
	@Override
	public boolean delUserHasOrg(long id, long orgId) {
		String sql="delete from uic_user_has_org where id = ? and org_id = ?  ";
		return this.getJdbcTemplate().update(sql, new Object[]{id,orgId}) >0 ;
	}
	/**
	 * 新建用户uic_user_has_org插入 type 1
	 */
	@Override
	public boolean insertUserHaOrg(long id, long orgId) {
		String sql = " insert into uic_user_has_org(org_id ,id, type) values (?,?,1)";
		return this.getJdbcTemplate().update(sql, new Object[]{orgId,id}) >0 ;
	}
	/**
	 * 新增角色默认角色设置
	 */
	@Override
	public void setUserHadRole(long id) {
		String sql = " insert into uic_user_has_role(id ,role_id) values (?,15)";
		this.getJdbcTemplate().update(sql, new Object[]{id});
	}

	@Override
	public List<User> getAllUser() {
		String sql = "select ID,"
				+ "USER_ID,"
				+ "USER_NAME,"
				+ "LOGIN_ID,"
				+ "GENDER,"
				+ "MOBILE_NO,"
				+ "USER_TYPE,"
				+ "DEL_FLAG,"
				+ "PASSWORD,"
				+ "DURATION,"
				+ "PWD_LAST_MOD_TIME,"
				+ "LST_ERR_PWD_TIME,"
				+ "PWD_ERR_CNT,"
				+ "CREATE_TIME from uic_user" ; 
		List<User>	users =	this.getJdbcTemplate().query(sql, new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User  user= new User();
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
				return user;
			}});
		return users;
	}

	/**
	 * 更新用户状态（锁定:2/解锁:0）
	 */
	@Override
	public boolean updateUserStatus(String loginId, String status) {
		String sql="UPDATE UIC_USER SET status=? WHERE LOGIN_ID=?";
		return this.getJdbcTemplate().update(sql, status,loginId)>0;
	}
	
}
