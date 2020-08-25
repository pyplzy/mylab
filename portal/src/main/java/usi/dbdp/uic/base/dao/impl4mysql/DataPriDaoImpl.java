package usi.dbdp.uic.base.dao.impl4mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.base.dao.DataPriDao;
import usi.dbdp.uic.entity.DataPri;
import usi.dbdp.uic.entity.User;
import usi.dbdp.uic.util.CommonUtil;

/**
 * 数据权限
 * @author ma.guangming
 *
 */
@MysqlDb
@Repository
public class DataPriDaoImpl  extends JdbcDaoSupport4mysql implements  DataPriDao{
	/**
	 * @author ma.guangming
	  * 批量保存数据权限
	 * @param dataPris 数据权限列表
	 */
	public boolean  batchAddDataPris(final List<DataPri> dataPris ){
		String sql="insert into uic_app_data_pri (user_id ,app_code)  values(?,?) ";
		return this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setString(1, dataPris.get(i).getUserId());
				ps.setString(2, dataPris.get(i).getPrivilegeValue());
				
			}
			@Override
			public int getBatchSize() {
				return dataPris.size();
			}
		}).length>0;
	}

	
	/**
	 * @author ma.guangming
	  * 删除应用权限
	 * @param userId 登录帐号
	 */
	public boolean  delDataPri(final String userId ){
		String sql="delete from  uic_app_data_pri where user_id = ?   and app_code not in  (select b.app_code from uic_app b where b.app_type=0) ";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
		})>0;
	}
	
	/**
	 * @author ma.guangming
	 * 查询登录帐号拥有的权限
	 * @param userId 登录帐号
	 * @param privilegeType 权限类型（1：应用数据权限）
	 * @return 数据权限列表
	 */
	public List<DataPri> getDataPrisByUserIdAndPrivilegeType(String userId , final int privilegeType) {
		String sql = "";
		if(1==privilegeType){   //查应用数据权限
			sql = "SELECT a.user_id,a.app_code privilege_value , b.app_name privilege_memo,b.display_order  FROM UIC_APP_DATA_PRI  a  ,uic_app b "
					+ " WHERE a.app_code = b.app_code AND a.user_id = ? AND b.state = 1 ORDER BY b.display_order ";
		} 
		return this.getJdbcTemplate().query(sql,new Object[]{userId},  new RowMapper<DataPri>(){
			@Override
			public DataPri mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				DataPri dataPri=new DataPri();
				dataPri.setUserId(rs.getString("user_id"));
				dataPri.setPrivilegeValue(rs.getString("privilege_value"));
				dataPri.setPrivilegeType(privilegeType);  //应用数据权限 type 1
				dataPri.setPrivilegeMemo(rs.getString("privilege_memo"));
				return dataPri;
			}
		});
	}
	/**
	 * @author ma.guangming
	 * 根据登录帐号或用户姓名查询管理员角色的用户
	 * @param value 查询字符串 
	 * @return 用户列表
	 */
	public List<User> getAdminsByUserIdOrUserNameFromRole(String value,Long province,String userId){
		String sql=" select " +
				" a.create_time,a.duration,a.gender,a.id,a.lst_err_pwd_time,a.mobile_no,b.org_id,a.password,a.pwd_err_cnt,a.pwd_last_mod_time,a.del_flag,a.user_id,a.user_name,a.user_type " + 
				" from uic_user a,uic_user_has_role ur,uic_role r,uic_user_has_org uo,uic_org b" + 
			" where a.id = ur.id and ur.role_id = r.role_id  and a.del_flag = 0  and r.del_flag=0 and r.role_type = 0    and a.id = uo.id and b.org_id= uo.org_id and uo.type=1 and b.del_flag = 0 ";
		
		if("sysadmin".equals(userId)){
			sql+=" and r.role_code in('appadmin', 'sysadmin') ";
		}else{
			sql+= " and r.role_code in('appadmin', 'sysadmin') ";
		}
		if(0!=province){
			sql += " and (b.org_id_seq like concat((select tt.org_id_seq from uic_org tt where org_id = "+province+" ),'%'))";
		}
		if(CommonUtil.hasValue(value)){
			sql +=" and ( a.user_id like '%"+value
					+ "%' or a.user_name like '%"+value
					+ "%'  "
					+ ") ";
		}
		return this.getJdbcTemplate().query(sql,new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
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
				user.setUserType(rs.getInt("user_type"));
				return user;
			}
		});
	} 
	
	
	
}
