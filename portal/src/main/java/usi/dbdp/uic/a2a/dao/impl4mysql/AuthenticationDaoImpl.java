package usi.dbdp.uic.a2a.dao.impl4mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.a2a.dao.AuthenticationDao;
import usi.dbdp.uic.dto.AuthMenu;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.RoleGrantInfo;
import usi.dbdp.uic.dto.RoleMenuRel;
import usi.dbdp.uic.dto.UserRoleRel;

/**
 * 授权服务Dao
 * @author ma.guangming
 */
@MysqlDb
@Repository
public class AuthenticationDaoImpl extends JdbcDaoSupport4mysql implements AuthenticationDao{
	
	/**
	 * 给角色赋予多个菜单
	 * @param roleMenuRel 角色菜单传输对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean grantMenus2Role(final RoleMenuRel roleMenuRel){
		String sql="insert into uic_role_has_menu (menu_id,role_id) values(?,?)";
		return this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setLong(1, roleMenuRel.getMenuIds().get(i));
				ps.setLong(2, roleMenuRel.getRoleId());
			}
			@Override
			public int getBatchSize() {
				return roleMenuRel.getMenuIds().size();
			}
		}).length >0  ;
	}
	/**
	 * 获取某用户在某应用的所有菜单
	 * @param appCode 应用code
	 * @param id 用户id
	 * @return 某用户在某应用的所有菜单
	 */
	public List<AuthMenu> getAllMenusByUserIdInApp(String appCode,long id){
		String sql = "select distinct d.menu_id, d.menu_name, d.menu_action, d.p_menu_id,d.is_leaf ,d.display_order" +
						"  from uic_user_has_role b, uic_role_has_menu c, uic_menu d " + 
						" where b.role_id = c.role_id " + 
						"   and c.menu_id = d.menu_id " + 
						"   and d.del_flag = 0 " + 
						"   and b.id = ? " + 
						"   and d.app_code = ? order by d.display_order ";
		return this.getJdbcTemplate().query(sql, new Object[]{id,appCode}, new AuthMenuRowMapper());
	}	
	/**
	 * @author ma.guangming
	 * 依据应用code, 用户id, 角色code, 角色名称, 是否授权查询应用级角色列表
	 * @param pageObj 分页对象
	 * @param roleGrantInfo 角色授权dto:应用code, 用户id, 角色code, 角色名称
	 * @return 角色授权dto列表
	 */
	public List<RoleGrantInfo> getRoleGrantInfos(PageObj pageObj,
			final RoleGrantInfo roleGrantInfo) {
		String sql =" SELECT a.role_id  , a.APP_CODE , a.ROLE_CODE , a.ROLE_NAME , a.ROLE_MEMO, b.org_name, " +
					" (CASE WHEN a.role_id IN ( SELECT b.role_id FROM  uic_user_has_role t LEFT JOIN uic_role b ON t.ROLE_ID = b.ROLE_ID " + 
					" WHERE  t.id= ? )  THEN 1  ELSE  0 END )  isgranted ,a.province" + 
					" FROM uic_role a ,uic_org b ,uic_app t  " + 
					" WHERE a.province = b.org_id AND a.del_flag = 0 AND t.app_code = a.app_code AND t.state = 1 AND b.del_flag =0 " + 
					" AND a.app_code = ?  AND (  (t.APP_TYPE = 0) OR (t.app_type =1 AND a.province = ?) ) ";
		if(roleGrantInfo.getRoleType()==1){
			sql += " and a.role_type= 1 ";
		}
		sql +=" order by isgranted DESC,ROLE_NAME DESC ";
		return this.queryByPage(sql,new Object[] { roleGrantInfo.getId() ,roleGrantInfo.getAppCode(), roleGrantInfo.getOrgId()},
				new RowMapper<RoleGrantInfo>() {
					@Override
					public RoleGrantInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
						RoleGrantInfo roleGrantInfo0 = new RoleGrantInfo();
						roleGrantInfo0.setRoleId(rs.getLong("role_id"));
						roleGrantInfo0.setRoleCode(rs.getString("role_code"));
						roleGrantInfo0.setRoleName(rs.getString("role_name"));
						roleGrantInfo0.setRoleDesc(rs.getString("role_memo"));
						roleGrantInfo0.setIsGranted(rs.getInt("isgranted"));
						roleGrantInfo0.setId(roleGrantInfo.getId());
						roleGrantInfo0.setOrgName(rs.getString("org_name"));
						return roleGrantInfo0;
					}
				}, pageObj);
	}
	/**
	 * 取消授权
	 * @param roleMenuRel 用户角色数据传输对象实体
	 */
	public boolean delUserRolesRel(final UserRoleRel userRoleRel){
		String sql="delete  from  uic_user_role_rel where role_id =? and id = ?  ";
		return this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setLong(1, userRoleRel.getRoleIds().get(i));
				ps.setLong(2, userRoleRel.getId());
			}
			@Override
			public int getBatchSize() {
				return userRoleRel.getRoleIds().size();
			}
		}).length >0 ;
	}
	/**
	 * 删除角色的菜单权限 
	 * @param menuId 菜单id
	 * @return true 成功  false 失败
	 */
	public boolean deleteMenuRole(long menuId) {
		String sql = "delete from uic_role_menu_rel where menu_id = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{menuId} )>0;
	}


	/**
	 * 封装授权菜单的内部类
	 * @author lmwang
	 *
	 */
	private static class AuthMenuRowMapper implements RowMapper<AuthMenu>{
		@Override
		public AuthMenu mapRow(ResultSet rs, int rowNum) throws SQLException {
			AuthMenu obj = new AuthMenu();
			obj.setMenuId(rs.getLong("menu_id"));
			obj.setMenuName(rs.getString("menu_name"));
			obj.setMenuAction(rs.getString("menu_action"));
			obj.setParentId(rs.getLong("p_menu_id"));
			obj.setIsLeaf(rs.getInt("is_leaf"));
			return obj;
		}
	}
	/**
	 * 给用户取消授权
	 * @param id  用户Id
	 * @param roleId  角色Id
	 * @return 1 成功 0 失败
	 */
	public int delPermissionByIdAndRoleId(long id, long roleId){
		String sql="delete  from  uic_user_has_role where role_id =? and id = ?  ";
		return this.getJdbcTemplate().update(sql, new Object[]{roleId, id });
	}
	/**
	 * 给用户授权
	 * @param id  用户Id
	 * @param roleId  角色Id
	 * @return 1 成功 0 失败
	 */
	public int givePermissionByIdAndRoleId(long id, long roleId){
		String sql="insert into  uic_user_has_role (role_id, id ) values(?, ?) ";
		return this.getJdbcTemplate().update(sql, new Object[]{roleId, id });
	}
}
