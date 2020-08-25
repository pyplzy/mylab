package usi.dbdp.uic.base.dao.impl4oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.app.util.OraclePasswdUtil;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;
import usi.dbdp.uic.base.dao.RoleDao;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleInfo;
import usi.dbdp.uic.dto.RoleUser;
import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.dto.UserInfo4Session;
import usi.dbdp.uic.entity.Menu;
import usi.dbdp.uic.entity.Role;
import usi.dbdp.uic.entity.User;
import usi.dbdp.uic.util.CommonUtil;

/**
 * 
 * @author nie.zhengqian
 * 创建时间：2015年3月3日 下午6:27:31
 */
@OracleDb
@Repository
public class RoleDaoImpl extends JdbcDaoSupport4oracle implements RoleDao{
	/**
	 * 给某应用增加一个角色
	 * @param appCode
	 * @param role
	 * @return
	 */
	public int addRole(final Role role){
		return this.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String sql = " INSERT INTO uic_role("
						+" role_id,APP_CODE,"
						+" province,"
						+" ROLE_CODE,"
						+" ROLE_NAME,"
						+" ROLE_MEMO,"
						+" ROLE_TYPE"
						+" ) VALUES(uic_role_seq.nextval,?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql, new String[] { "ROLE_ID" });
				ps.setString(1, role.getAppCode());
				ps.setLong(2,role.getProvince());
				ps.setString(3, role.getRoleCode());
				ps.setString(4, role.getRoleName());
				ps.setString(5, role.getRoleMemo());
				ps.setInt(6,role.getRoleType());
				return ps;
			}
		});
	}
	/**
	 * 更新某应用角色
	 * @param appCode
	 * @param role
	 * @return
	 */
	public int updateRole( final Role role) {
		String sql = " update uic_role set"
				+" ROLE_NAME=?,"
				+" ROLE_MEMO=?,"
				+" ROLE_TYPE=?"
				+" where ROLE_ID = ? and APP_CODE=? ";

	return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps) throws SQLException {
			ps.setString(1, role.getRoleName());
			ps.setString(2, role.getRoleMemo());
			ps.setInt(3, role.getRoleType());
			ps.setLong(4, role.getRoleId());
			ps.setString(5, role.getAppCode());
		}
	});
	}
	/**
	 * 查询某应用某角色信息
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public List<Role> queryRoleById(String appCode, long roleId){
		String sql = " SELECT"
				+" ROLE_ID,"
				+" APP_CODE,"
				+" ROLE_CODE,"
				+" ROLE_NAME,"
				+" ROLE_MEMO,"
				+" del_flag,"
				+" ROLE_TYPE"
				+" FROM UIC_ROLE"
				+" WHERE ROLE_ID = ? AND APP_CODE=? ";
	return this.getJdbcTemplate().query(sql, new Object[]{roleId, appCode}, new RowMapper<Role>(){

		@Override
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setRoleId(rs.getLong("ROLE_ID"));
			role.setAppCode(rs.getString("APP_CODE"));
			role.setRoleCode(rs.getString("ROLE_CODE"));
			role.setRoleName(rs.getString("ROLE_NAME"));
			role.setRoleMemo(rs.getString("ROLE_MEMO"));
			role.setDelFlag(rs.getInt("DEL_FLAG"));
			role.setRoleType(rs.getInt("ROLE_TYPE"));
			return role;
		}
	});
	}
	/**
	 * 角色与菜单之间的关系删除
	 * @param roleId
	 */
	public boolean deleteRoleMenus(long roleId){
		String sql = "delete from uic_role_has_menu where role_id = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{roleId})>0;
	}
	/**
	 *角色与用户之间的关系删除
	 * @param roleId 角色id
	 */
	public void deleteRoleUsers(long roleId){
		String sql = "delete from uic_user_has_role where role_id = ?";
		this.getJdbcTemplate().update(sql, new Object[]{roleId});
	}
	/**
	 *删除某应用角色（逻辑删除，设置角色状态为失效）
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public int deleteRoleById(String appCode, long roleId){
		String sql = "UPDATE UIC_ROLE SET del_Flag = 1 WHERE ROLE_ID = ? AND APP_CODE = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{roleId, appCode});		
	}
	/**
	 * 激活某应用角色（将角色从失效状态改为生效）
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public int activateRoleById(String appCode, long roleId){
		String sql = "UPDATE UIC_ROLE SET del_Flag = 0 WHERE ROLE_ID = ? AND APP_CODE = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{roleId, appCode});		
	}
	/**
	 * 获取某应用的所有角色 分页
	 * @param appCode
	 * @return
	 */
	public List<Role> getAllRolesByAppCodePage(String appCode, PageObj pageObj) {
		String sql = " SELECT"
					+" ROLE_ID,"
					+" APP_CODE,"
					+" ROLE_CODE,"
					+" ROLE_NAME,"
					+" ROLE_MEMO,"
					+" DEL_FLAG,"
					+" ROLE_TYPE"
					+" FROM UIC_ROLE"
					+" WHERE APP_CODE=? and DEL_FLAG = 0";
	return this.queryByPage(sql, new Object[]{appCode}, new RowMapper<Role>(){

		@Override
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setRoleId(rs.getLong("ROLE_ID"));
			role.setAppCode(rs.getString("APP_CODE"));
			role.setRoleCode(rs.getString("ROLE_CODE"));
			role.setRoleName(rs.getString("ROLE_NAME"));
			role.setRoleMemo(rs.getString("ROLE_MEMO"));
			role.setDelFlag(rs.getInt("DEL_FLAG"));
			role.setRoleType(rs.getInt("ROLE_TYPE"));
			return role;
		}
	},pageObj);
	}
	
	/**
	 * 获取某应用的所有角色
	 * @param appCode
	 * @return
	 */
	public List<Role> getAllRolesByAppCode(String appCode) {
		String sql = " SELECT"
					+" ROLE_ID,"
					+" APP_CODE,"
					+" ROLE_CODE,"
					+" ROLE_NAME,"
					+" ROLE_MEMO,"
					+" DEL_FLAG,"
					+" ROLE_TYPE"
					+" FROM UIC_ROLE"
					+" WHERE APP_CODE=? and DEL_FLAG = 0";
	return this.getJdbcTemplate().query(sql, new Object[]{appCode}, new RowMapper<Role>(){

		@Override
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setRoleId(rs.getLong("ROLE_ID"));
			role.setAppCode(rs.getString("APP_CODE"));
			role.setRoleCode(rs.getString("ROLE_CODE"));
			role.setRoleName(rs.getString("ROLE_NAME"));
			role.setRoleMemo(rs.getString("ROLE_MEMO"));
			role.setDelFlag(rs.getInt("DEL_FLAG"));
			role.setRoleType(rs.getInt("ROLE_TYPE"));
			return role;
		}
	});
	}
	
	
	/**
	 * @author ma.guangming
	 * 获取某应用下某角色名称(模糊查询)的角色列表
	 * @param appCode 应用code
	 * @param roleName 角色名称
	 * @param roleType 角色类型（0：系统角色，1：应用角色）默认0 
	 * @param pageObj 分页对象
	 * @return 角色列表
	 */
	public List<Role> getRolesByAppCodeAndRoleNameWithPage(String appCode,
			String roleName , Long province,int roleType , PageObj pageObj) {
		String sql = " SELECT t.role_id,t.app_code,t.province,t.role_code,t.role_name,t.role_memo,t.del_flag ,t.role_type ,"+OraclePasswdUtil.getDecryptStr("u.org_name")+" AS org_name " +
					" FROM uic_role t  ,uic_org u ,uic_app a " + 
					" WHERE t.province = u.org_id AND a.app_code = t.app_code  AND  t.del_flag = 0 " + 
					" AND  t.app_code = ? AND (  (a.APP_TYPE = 0) OR (a.app_type =1 AND t.province = ?) )";
		if(roleType==1){
			sql +=" AND t.role_type = 1 ";
		}
		sql += " order by role_id ";
		return this.queryByPage(sql, new Object[]{appCode,province}, new RowMapper<Role>(){
			@Override
			public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
				Role role = new Role();
				role.setRoleId(rs.getLong("role_id"));
				role.setAppCode(rs.getString("app_code"));
				role.setProvince(rs.getLong("province"));
				role.setOrgName(rs.getString("org_name"));
				role.setRoleCode(rs.getString("role_code"));
				role.setRoleName(rs.getString("role_name")==null?"":rs.getString("role_name"));
				role.setRoleMemo(rs.getString("role_memo")==null?"":rs.getString("role_memo"));
				role.setDelFlag(rs.getInt("del_flag"));
				role.setRoleType(rs.getInt("role_type"));
				return role;
			}
		}, pageObj);
	}
	/**
	 * @author ma.guangming
	 * 获取某应用下某角色拥有的所有菜单
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @return 菜单列表
	 */
	public List<Menu> getMenusByRoleIdAndAppCode(String appCode,long roleId) {
		String sql = " select m.menu_id,m.app_code,m.menu_name,m.menu_level,m.menu_action,m.p_menu_id,m.display_order,"
				    +" m.del_flag,m.menu_memo,m.menu_seq,m.is_leaf  "
					+" from uic_role_has_menu rm ,uic_menu m  "
					+" where rm.menu_id=m.menu_id "
					+" and m.del_flag=0 "
					+" and  rm.role_id = ? and m.app_code=?  ";
		return this.getJdbcTemplate().query(sql, new Object[]{roleId,appCode}, new RowMapper<Menu>(){
			@Override
			public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
				Menu menu = new Menu();
				menu.setMenuId(rs.getLong("menu_id"));
				menu.setAppCode(rs.getString("app_code"));
				menu.setMenuName(rs.getString("menu_name"));
				menu.setMenuLevel(rs.getInt("menu_level"));
				menu.setMenuAction(rs.getString("menu_action"));
				menu.setpMenuId(rs.getInt("p_menu_id"));
				menu.setDisplayOrder(rs.getInt("display_order"));
				menu.setDelFlag(rs.getInt("DEL_FLAG"));
				menu.setMenuMemo(rs.getString("menu_memo"));
				menu.setMenuSeq(rs.getString("menu_seq"));
				menu.setIsLeaf(rs.getInt("is_leaf"));
				return menu;
			}
		});
	}
	
	
	/**
	 * @author ma.guangming
	 * 批量添加角色下的成员
	 * @param roleUser
	 */
	public boolean batchAddUsersIntoRole(final RoleUser roleUser) {
		String sql = "insert into uic_user_has_role (role_id , id) values(?,?) ";
		return this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1, roleUser.getRoleId());
						ps.setLong(2, roleUser.getIds().get(i));
					}
					public int getBatchSize() {
						return roleUser.getIds().size();
					}
				}).length>0;
	}
	/**
	 * @author ma.guangming
	 * 批量删除角色下的成员
	 * @param roleUser
	 */
	public boolean batchDeleteUsersFromRole(final RoleUser roleUser) {
		String sql = "delete from uic_user_has_role  where role_id = ? and id= ? ";
		return this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1, roleUser.getRoleId());
						ps.setLong(2, roleUser.getIds().get(i));
					}
					public int getBatchSize() {
						return roleUser.getIds().size();
					}
				}).length>0;
	}
	
	/**
	 * 根据应用code和角色类型查询用户列表
	 * @param appCode应用code
	 * @param roleType角色类型
	 * @return
	 */
	public List<Role> queryByAppCodeAndRoleType(String appCode, int roleType){
		String sql = " SELECT"
					+" ROLE_ID,"
					+" APP_CODE,"
					+" ROLE_CODE,"
					+" ROLE_NAME,"
					+" ROLE_MEMO,"
					+" DEL_FLAG,"
					+" ROLE_TYPE"
					+" FROM UIC_ROLE"
					+" WHERE APP_CODE=? "
					+" and ROLE_TYPE=? "
					+ "and DEL_FLAG = 0";
	return this.getJdbcTemplate().query(sql, new Object[]{appCode,roleType}, new RowMapper<Role>(){

		@Override
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setRoleId(rs.getLong("ROLE_ID"));
			role.setAppCode(rs.getString("APP_CODE"));
			role.setRoleCode(rs.getString("ROLE_CODE"));
			role.setRoleName(rs.getString("ROLE_NAME"));
			role.setRoleMemo(rs.getString("ROLE_MEMO"));
			role.setDelFlag(rs.getInt("DEL_FLAG"));
			role.setRoleType(rs.getInt("ROLE_TYPE"));
			return role;
		}});
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode 应用编码
	 * @param roleCode 角色编码
	 * @return
	 */
	public List<RoleInfo> queryRoleByCode(String appCode, String roleCode){
		String sql = " SELECT  ar.app_name, "
				+" r.ROLE_ID,"
				+" r.APP_CODE,"
				+" r.ROLE_CODE,"
				+" r.ROLE_NAME,"
				+" r.ROLE_MEMO,"
				+" r.del_flag,"
				+" r.ROLE_TYPE"
				+" FROM UIC_ROLE r,uic_app ar "
				+" WHERE r.app_code=ar.app_code and r.role_code = ? AND r.APP_CODE=? ";
		return this.getJdbcTemplate().query(sql, new Object[]{roleCode, appCode}, new RowMapper<RoleInfo>(){
	
			@Override
			public RoleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RoleInfo role = new RoleInfo();
				role.setAppName(rs.getString("app_name"));
				role.setRoleId(rs.getLong("ROLE_ID"));
				role.setAppCode(rs.getString("APP_CODE"));
				role.setRoleCode(rs.getString("ROLE_CODE"));
				role.setRoleName(rs.getString("ROLE_NAME"));
				role.setRoleMemo(rs.getString("ROLE_MEMO"));
				role.setDelFlag(rs.getInt("del_flag"));
				role.setRoleType(rs.getInt("ROLE_TYPE"));
				return role;
			}
		});
	}
	
	/**
	 * @author ma.guangming
	 * 依据登录帐号和用户姓名获取某应用下的某角色的用户列表（分页）
	 * @param appCode 应用code
	 * @param roleId 角色id
	 * @param user 用户对象  
	 * @param pageObj 分页对象
	 * @return 某应用下的某角色的用户列表
	 */
	public List<UserInfo4Session> getUsersByAppCodeAndRoleIdAndUserIdAndUserNameWithPage2(String appCode, User user ,long roleId,
			PageObj pageObj) {
		String sql = "SELECT a.create_time,a.duration,a.gender,a.id,a.lst_err_pwd_time,"+OraclePasswdUtil.getDecryptStr("a.mobile_no")+" as mobile_no,o.org_id,a.password,a.pwd_err_cnt,"
				+ " a.pwd_last_mod_time,a.del_flag,a.user_id,"+OraclePasswdUtil.getDecryptStr("a.user_name")+" as user_name,a.user_type,"+OraclePasswdUtil.getDecryptStr("o.org_name")+" AS org_name,"+OraclePasswdUtil.getDecryptStr("o.org_name_seq ")+" AS org_name_seq   "
				 +" from"
				 +" uic_user a  , uic_user_has_role ur  ,  uic_role r  ,uic_org o , uic_user_has_org og" 
				 +" WHERE  a.id = ur.id and ur.role_id = r.role_id and  a.id =og.id and  og.org_id = o.org_id and  og.type = 1 and "
				 +" r.app_code = ? and ur.role_id = ?  and a.del_flag = 0  ";
		if (user != null) {
			if (CommonUtil.hasValue(user.getUserId())){
				sql += " and a.user_id like '%" + user.getUserId() + "%' ";
			}	
			if (CommonUtil.hasValue(user.getUserName())){
				sql += " and "+OraclePasswdUtil.getDecryptStr("a.user_name")+"  like '%" + user.getUserName()+ "%' ";
			}	
		}
		sql += " order by a.pwd_last_mod_time desc ";
		return this.queryByPage(sql, new Object[]{appCode,roleId}, new RowMapper<UserInfo4Session>(){
			@Override
			public UserInfo4Session mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo4Session  user= new UserInfo4Session();
				user.setId(rs.getLong("id"));
				user.setMobileNo(rs.getString("mobile_no"));
				user.setOrgId(rs.getLong("org_id"));
				user.setUserId(rs.getString("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setUserType(rs.getInt("user_type"));
				user.setOrgName(rs.getString("org_name"));
				user.setOrgNameSeq(rs.getString("org_name_seq"));
				return user;
			}
		}, pageObj);
	}
	
	/**
	 * 查询某应用某角色信息
	 * @param appCode
	 * @param roleId
	 * @return
	 */
	public List<RoleInfo> queryRoleById2(String appCode, long roleId){
		String sql = " SELECT  ar.app_name , "
				+" r.ROLE_ID,"
				+" r.APP_CODE,"
				+OraclePasswdUtil.getDecryptStr("u.org_name")+" AS org_name,"
				+" r.ROLE_CODE,"
				+" r.ROLE_NAME,"
				+" r.ROLE_MEMO,"
				+" r.del_flag,"
				+" r.ROLE_TYPE"
				+" FROM UIC_ROLE r,uic_app ar ,uic_org u"
				+" WHERE r.app_code=ar.app_code and r.province = u.org_id and r.role_id = ? AND r.APP_CODE=? ";
		return this.getJdbcTemplate().query(sql, new Object[]{roleId, appCode}, new RowMapper<RoleInfo>(){
	
			@Override
			public RoleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RoleInfo role = new RoleInfo();
				role.setAppName(rs.getString("app_name"));
				role.setRoleId(rs.getLong("ROLE_ID"));
				role.setAppCode(rs.getString("APP_CODE"));
				role.setOrgName(rs.getString("org_name"));
				role.setRoleCode(rs.getString("ROLE_CODE"));
				role.setRoleName(rs.getString("ROLE_NAME"));
				role.setRoleMemo(rs.getString("ROLE_MEMO"));
				role.setDelFlag(rs.getInt("del_flag"));
				role.setRoleType(rs.getInt("ROLE_TYPE"));
				return role;
			}
		});
	}
	
	/**
	 * @author ma.guangming
	 * 根据角色ID查询出的未添加进该角色的员工,用于选择添加
	 * @param adminOrgId 当前登录人的机构id
	 * @param orgId 查询条件的机构id
	 * @param adminId 当前登录人的账号
	 * @param roleId 角色id
	 * @param userId 被查询员工的的登录帐号
	 * @param userName 被查询员工的用户名称
	 * @param orgName 被查询员工的机构名称
	 * @param pageObj 分页对象
	 * @return 未添加进该角色的员工
	 */
	public List<UserInfo> 	getAllOtherUsersWithoutRoleByPage2
		(Long adminOrgId,Long orgId  , String adminId , long roleId,String userId , String userName , String orgName  , PageObj pageObj){
		String sql="select u.id ,u.user_id ,"+OraclePasswdUtil.getDecryptStr("u.user_name")+" as user_name ,"+OraclePasswdUtil.getDecryptStr("o.org_name_seq")+" AS org_name_seq   "
				+ " from uic_user u , uic_org o , uic_user_has_org og "
				+ " where u.id=og.id and og.org_id = o.org_id and og.type = 1   "
				+ " and not exists(select 1 from uic_user_has_role ur where u.id=ur.id and ur.role_id =? ) "
				+ " and u.del_flag=0  ";
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
		if (CommonUtil.hasValue(userName)){
			sql += " and "+OraclePasswdUtil.getDecryptAsStr("u.user_name")+ " like '%" + userName+ "%' ";
		}
		if(CommonUtil.hasValue(orgName)){
			sql += " and "+OraclePasswdUtil.getDecryptAsStr("o.org_name")+" like '%" + orgName + "%' ";
		}
		return this.queryByPage(sql, new Object[] { roleId  }, new RowMapper<UserInfo>() {

			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(rs.getLong("id"));
				userInfo.setUserId(rs.getString("user_id"));
				userInfo.setUserName(rs.getString("user_name"));
				userInfo.setOrgNameSeq(rs.getString("org_name_seq"));
				return userInfo;
			}
		}, pageObj);
	}
	
	/**
	 * 解除role opt关联关系
	 */
	@Override
	public void deleteRoleOpts(long roleId) {
		String sql = " delete from uic_role_has_opt where role_id = ? ";
		this.getJdbcTemplate().update(sql,roleId);
	}
	
}
