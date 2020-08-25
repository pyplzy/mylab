package usi.dbdp.uic.base.dao.impl4oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;
import usi.dbdp.uic.base.dao.AppRegisterDao;
import usi.dbdp.uic.dto.AppRegisterInfo;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.entity.AppRegister;

/**
 * 应用注册服务dao
 * @author nie.zhengqian
 * 创建时间：2015年3月10日 下午5:15:07
 */
@OracleDb
@Repository
public class AppRegisterDaoImpl extends JdbcDaoSupport4oracle implements AppRegisterDao{
	
	/**
	 * 应用code 个数
	 * @param appCode
	 * @return 应用code 个数
	 */
	public int judgeAppByAppCode(String appCode){
		String sql = "select count(*) from uic_app where APP_CODE = ?";
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{appCode}, Integer.class);
	}
	
	/**
	 * 增加一个应用
	 * @param AppRegister 应用信息对象
	 * @return 1表示操作成功，0表示操作失败
	 */
	public int addApp(final AppRegister appRegister){
		String sql = " INSERT INTO uic_app ("
				 +" APP_CODE,"
				 +" APP_NAME,"
				 +" APP_IMG_PATH,"
				 +" APP_URL,"
				 +" TODO_URL,"
				 +" APP_CHECKSUM,"
				 +" STATE,"
				 +" APP_MEMO,"
				 +" REG_TIME,"
				 +" USER_GUIDE_PATH,"
				 +" APP_TYPE,"
				 +"display_order"
				 +" )"
				 +" VALUES"
				 +"(?,?,?,?,?,?,?,?,sysdate,?,?,?)";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, appRegister.getAppCode());
				ps.setString(2, appRegister.getAppName());
				ps.setString(3, appRegister.getAppImgPath());
				ps.setString(4, appRegister.getAppUrl());
				ps.setString(5, appRegister.getTodoUrl());
				ps.setString(6, appRegister.getAppChecksum());
				ps.setInt(7, appRegister.getState());
				ps.setString(8, appRegister.getAppMmemo());
				ps.setString(9, appRegister.getUserGuidePath());
				ps.setInt(10, appRegister.getAppType());
				ps.setInt(11, appRegister.getDisplayOrder());
			}
			
		});
	}
	
	/**
	 * 更新某应用信息
	 * @param AppRegister 应用信息对象
	 * @return 1表示操作成功，0表示操作失败
	 */
	public int updateApp(final AppRegister appRegister) {
		String sql = " UPDATE "
					+" uic_app "
					+" SET"
					+" APP_NAME = ?,"
					+" APP_IMG_PATH = ?,"
					+" APP_URL = ?,"
					+" TODO_URL = ?,"
					+" APP_CHECKSUM = ?,"
					+" STATE = ?,"
					+" APP_MEMO = ?,"
					+" REG_TIME = sysdate, "
					+" USER_GUIDE_PATH = ?, "
					+" APP_TYPE = ? ,  "
					+ " display_order =? "
					+" WHERE APP_CODE = ? ";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, appRegister.getAppName());
				ps.setString(2, appRegister.getAppImgPath());
				ps.setString(3, appRegister.getAppUrl());
				ps.setString(4, appRegister.getTodoUrl());
				ps.setString(5, appRegister.getAppChecksum());
				ps.setInt(6, appRegister.getState());
				ps.setString(7, appRegister.getAppMmemo());
				ps.setString(8, appRegister.getUserGuidePath());
				ps.setInt(9, appRegister.getAppType());
				ps.setInt(10, appRegister.getDisplayOrder());
				ps.setString(11, appRegister.getAppCode());
			}
		});
	}
	/**
	 * 根据应用appCode查应用信息
	 * @param appCode 应用code
	 * @return 应用信息对象
	 */
	public List<AppRegister> queryAppByAppCode(String appCode) {
		String sql =  " SELECT "
					 +" APP_CODE,"
					 +" APP_NAME,"
					 +" APP_IMG_PATH,"
					 +" APP_URL,"
					 +" TODO_URL,"
					 +" APP_CHECKSUM,"
					 +" STATE,"
					 +" APP_MEMO,"
					 +" REG_TIME, "
					 +" USER_GUIDE_PATH ,"
					 +" app_type, "
					 +" display_order "
					 +" FROM"
					 +" uic_app "
					 +" WHERE APP_CODE = ?";
		return this.getJdbcTemplate().query(sql,new Object[]{appCode}, new RowMapper<AppRegister>(){
			@Override
			public AppRegister mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AppRegister appRegister = new AppRegister();
				appRegister.setAppCode(rs.getString("APP_CODE"));
				appRegister.setAppName(rs.getString("APP_NAME"));
				appRegister.setAppImgPath(rs.getString("APP_IMG_PATH"));
				appRegister.setAppUrl(rs.getString("APP_URL"));
				appRegister.setTodoUrl(rs.getString("TODO_URL"));
				appRegister.setAppChecksum(rs.getString("APP_CHECKSUM"));
				appRegister.setState(rs.getInt("STATE"));
				appRegister.setAppMmemo(rs.getString("APP_MEMO"));
				appRegister.setRegTime(rs.getDate("REG_TIME"));
				appRegister.setUserGuidePath(rs.getString("USER_GUIDE_PATH"));
				appRegister.setAppType(rs.getInt("app_type"));
				appRegister.setDisplayOrder(rs.getInt("display_order"));
				return appRegister;
			}
		});
	}
	/**
	 * 删除某应用（逻辑删除，设置应用信息状态为失效）
	 * @param appCode 应用code
	 * @return 1表示操作成功，0表示操作失败
	 */
	public int deleteAppByAppCode(String appCode) {
		String sql = "UPDATE UIC_APP SET STATE = 0 WHERE  APP_CODE=? ";
		return this.getJdbcTemplate().update(sql, new Object[]{appCode});
	}
	
	/**
	 * 激活某应用（将应用信息状态从失效状态改为生效）
	 * @param appCode 应用code
	 * @return 1表示操作成功，0表示操作失败
	 */
	public int activateAppByAppCode(String appCode) {
		String sql = "UPDATE UIC_APP SET STATE = 1 WHERE  APP_CODE=? ";
		return this.getJdbcTemplate().update(sql, new Object[]{appCode});
	}
	
	/**
	 * 查询所有激活的应用
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAppByState() {
		String sql =  " SELECT "
					 +" APP_CODE,"
					 +" APP_NAME,"
					 +" APP_IMG_PATH,"
					 +" APP_URL,"
					 +" TODO_URL,"
					 +" APP_CHECKSUM,"
					 +" STATE,"
					 +" APP_MEMO,"
					 +" REG_TIME, "
					 +" USER_GUIDE_PATH, "
					 +" app_type, "
					 +" display_order "
					 +" FROM"
					 +" uic_app "
					 +" WHERE STATE = 1 order by display_order ";
		return this.getJdbcTemplate().query(sql, new RowMapper<AppRegister>(){
			@Override
			public AppRegister mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AppRegister appRegister = new AppRegister();
				appRegister.setAppCode(rs.getString("APP_CODE"));
				appRegister.setAppName(rs.getString("APP_NAME"));
				appRegister.setAppImgPath(rs.getString("APP_IMG_PATH"));
				appRegister.setAppUrl(rs.getString("APP_URL"));
				appRegister.setTodoUrl(rs.getString("TODO_URL"));
				appRegister.setAppChecksum(rs.getString("APP_CHECKSUM"));
				appRegister.setState(rs.getInt("STATE"));
				appRegister.setAppMmemo(rs.getString("APP_MEMO"));
				appRegister.setRegTime(rs.getDate("REG_TIME"));
				appRegister.setUserGuidePath(rs.getString("USER_GUIDE_PATH"));
				appRegister.setAppType(rs.getInt("app_type"));
				appRegister.setDisplayOrder(rs.getInt("display_order"));
				return appRegister;
			}
		});
	}
	
	/**
	 * 查询所有的应用
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAllAppList() {
		String sql =  " SELECT "
					 +" APP_CODE,"
					 +" APP_NAME,"
					 +" APP_IMG_PATH,"
					 +" APP_URL,"
					 +" TODO_URL,"
					 +" APP_CHECKSUM,"
					 +" STATE,"
					 +" APP_MEMO,"
					 +" REG_TIME, "
					 +" USER_GUIDE_PATH ,"
					 +" app_type, "
					 +" display_order  "
					 +" FROM"
					 +" uic_app where app_type>0 order by display_order  ";
		return this.getJdbcTemplate().query(sql, new RowMapper<AppRegister>(){
			@Override
			public AppRegister mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AppRegister appRegister = new AppRegister();
				appRegister.setAppCode(rs.getString("APP_CODE"));
				appRegister.setAppName(rs.getString("APP_NAME"));
				appRegister.setAppImgPath(rs.getString("APP_IMG_PATH"));
				appRegister.setAppUrl(rs.getString("APP_URL"));
				appRegister.setTodoUrl(rs.getString("TODO_URL"));
				appRegister.setAppChecksum(rs.getString("APP_CHECKSUM"));
				appRegister.setState(rs.getInt("STATE"));
				appRegister.setAppMmemo(rs.getString("APP_MEMO"));
				appRegister.setRegTime(rs.getDate("REG_TIME"));
				appRegister.setUserGuidePath(rs.getString("USER_GUIDE_PATH"));
				appRegister.setAppType(rs.getInt("app_type"));
				appRegister.setDisplayOrder(rs.getInt("display_order"));
				return appRegister;
			}
		});
	}
	
	/**
	 * @author  ma.guangming
	 * 查询应用级应用
	 * @return List<AppRegister>
	 */
	public List<AppRegister> getNomalAppsByState() {
		String sql =  " select app_code,app_name,app_img_path,app_url,todo_url,app_checksum,state,app_memo,reg_time,user_guide_path ,app_type , display_order  "
					 +" from uic_app  "
					 +" where state = 1 and app_type =1 order by display_order ";
		return this.getJdbcTemplate().query(sql, new RowMapper<AppRegister>(){
			@Override
			public AppRegister mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AppRegister appRegister = new AppRegister();
				appRegister.setAppCode(rs.getString("app_code"));
				appRegister.setAppName(rs.getString("app_name"));
				appRegister.setAppImgPath(rs.getString("app_img_path"));
				appRegister.setAppUrl(rs.getString("app_url"));
				appRegister.setTodoUrl(rs.getString("todo_url"));
				appRegister.setAppChecksum(rs.getString("app_checksum"));
				appRegister.setState(rs.getInt("state"));
				appRegister.setAppMmemo(rs.getString("app_memo"));
				appRegister.setRegTime(rs.getDate("reg_time"));
				appRegister.setUserGuidePath(rs.getString("user_guide_path"));
				appRegister.setAppType(rs.getInt("app_type"));
				appRegister.setDisplayOrder(rs.getInt("display_order"));
				return appRegister;
			}
		});
	}
	
	/**
	 * 获取某用户拥有菜单的非系统级应用(分页)
	 * @param id 用户id
	 * @return 应用dto
	 */

	/*public List<AppRegisterInfo> getAllAppsByUserIdWithPage(PageObj pageObj , String  userId){
		String sql = 
				"SELECT DISTINCT " +
						"  ar.app_name, " + 
						"  ar.app_checksum, " + 
						"  ar.app_code, " + 
						"  ar.app_img_path, " + 
						"  ar.app_memo, " + 
						"  ar.app_type, " + 
						"  ar.app_url, " + 
						"  ar.reg_time, " + 
						"  ar.state, " + 
						"  ar.display_order, " + 
						"  ar.todo_url, " + 
						"  ar.user_guide_path " + 
						"FROM " + 
						"  uic_user u, " + 
						"  uic_user_has_role ur, " + 
						"  uic_role r, " + 
						"  uic_role_has_menu rm, " + 
						"  uic_menu m, " + 
						"  uic_app ar " + 
						"WHERE u.id = ur.id " + 
						"  AND ur.role_id = r.role_id " + 
						"  AND r.role_id = rm.role_id " + 
						"  AND rm.menu_id = m.menu_id " + 
						"  AND m.app_code = ar.app_code " + 
						"  AND m.del_flag = 0 " + 
						"  AND r.del_flag = 0 " + 
						"  AND ar.state = 1 " + 
						"  AND ar.app_type = 1 " + 
						"  AND u.user_id = ? " + 
						"ORDER BY ar.display_order";
 
		return this.queryByPage(sql, new Object[]{userId}, new RowMapper<AppRegisterInfo>(){
			@Override
			public AppRegisterInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AppRegisterInfo appRegister = new AppRegisterInfo();
				appRegister.setAppCode(rs.getString("APP_CODE"));
				appRegister.setAppName(rs.getString("APP_NAME"));
				appRegister.setAppImgPath(rs.getString("APP_IMG_PATH"));
				appRegister.setAppUrl(rs.getString("APP_URL"));
				appRegister.setTodoUrl(rs.getString("TODO_URL"));
				appRegister.setAppChecksum(rs.getString("APP_CHECKSUM"));
				appRegister.setState(rs.getInt("STATE"));
				appRegister.setAppMmemo(rs.getString("APP_MEMO"));
				appRegister.setRegTime(rs.getDate("REG_TIME"));
				appRegister.setUserGuidePath(rs.getString("USER_GUIDE_PATH"));
				return appRegister;
			}
		},pageObj);
	}	*/
	public List<AppRegisterInfo> getAllAppsByUserIdWithPage(PageObj pageObj , String  userId){
		String sql = 
				"SELECT DISTINCT " +
						"  ar.app_name, " + 
						"  ar.app_checksum, " + 
						"  ar.app_code, " + 
						"  ar.app_img_path, " + 
						"  ar.app_memo, " + 
						"  ar.app_type, " + 
						"  ar.app_url, " + 
						"  ar.reg_time, " + 
						"  ar.state, " + 
						"  ar.display_order, " + 
						"  ar.todo_url, " + 
						"  ar.user_guide_path " + 
						"FROM " + 
						"  uic_user u, " + 
						"  uic_user_has_role ur, " + 
						"  uic_role r, " + 
						"  uic_role_has_menu rm, " + 
						"  uic_menu m, " + 
						"  uic_app ar " + 
						"WHERE u.id = ur.id " + 
						"  AND ur.role_id = r.role_id " + 
						"  AND r.role_id = rm.role_id " + 
						"  AND rm.menu_id = m.menu_id " + 
						"  AND m.app_code = ar.app_code " + 
						"  AND m.del_flag = 0 " + 
						"  AND r.del_flag = 0 " + 
						"  AND ar.state = 1 " + 
						"  AND ar.app_type = 1 " + 
						"  AND u.user_id = ? " + 
						"ORDER BY ar.display_order";
 
		return this.queryByPage(sql, new Object[]{userId}, new RowMapper<AppRegisterInfo>(){
			@Override
			public AppRegisterInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AppRegisterInfo appRegister = new AppRegisterInfo();
				appRegister.setAppCode(rs.getString("APP_CODE"));
				appRegister.setAppName(rs.getString("APP_NAME"));
				appRegister.setAppImgPath(rs.getString("APP_IMG_PATH"));
				appRegister.setAppUrl(rs.getString("APP_URL"));
				appRegister.setTodoUrl(rs.getString("TODO_URL"));
				appRegister.setAppChecksum(rs.getString("APP_CHECKSUM"));
				appRegister.setState(rs.getInt("STATE"));
				appRegister.setAppMmemo(rs.getString("APP_MEMO"));
				appRegister.setRegTime(rs.getDate("REG_TIME"));
				appRegister.setUserGuidePath(rs.getString("USER_GUIDE_PATH"));
				return appRegister;
			}
		},pageObj);
	}
	/**
	 * 查询所有的应用
	 * @param id 用户id 
	 * @return List<AppRegister>
	 */
	public List<AppRegister> queryAllAppList(long id) {
		ArrayList<Object> list = new ArrayList<Object>();
		String sql =  " SELECT a.app_checksum,a.app_code,a.app_img_path,a.app_memo,a.app_name,a.app_type,a.app_url,a.display_order,a.reg_time,  "
					 +" a.todo_url,a.user_guide_path,a.state"
					 +" FROM uic_app a    ";
		//如果是系统管理员直接查出所有应用
		if(1!=id){
			list.add(id);
			sql+="JOIN  (SELECT DISTINCT c.app_code FROM uic_user_has_role b ,uic_role c WHERE b.id=? AND b.role_id=c.role_id )	t   ON a.app_code =t.app_code ";
		}			 
		sql+="   where 1=? and  a.state=1 and a.app_type > 0   ORDER BY a.DISPLAY_ORDER" ;
		list.add(1);
		return this.getJdbcTemplate().query(sql,  list.toArray(),new RowMapper<AppRegister>(){
			@Override
			public AppRegister mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AppRegister appRegister = new AppRegister();
				appRegister.setAppCode(rs.getString("APP_CODE"));
				appRegister.setAppName(rs.getString("APP_NAME"));
				appRegister.setAppImgPath(rs.getString("APP_IMG_PATH"));
				appRegister.setAppUrl(rs.getString("APP_URL"));
				appRegister.setTodoUrl(rs.getString("TODO_URL"));
				appRegister.setAppChecksum(rs.getString("APP_CHECKSUM"));
				appRegister.setState(rs.getInt("STATE"));
				appRegister.setAppMmemo(rs.getString("APP_MEMO"));
				appRegister.setRegTime(rs.getDate("REG_TIME"));
				appRegister.setUserGuidePath(rs.getString("USER_GUIDE_PATH"));
				appRegister.setAppType(rs.getInt("app_type"));
				appRegister.setDisplayOrder(rs.getInt("display_order"));
				return appRegister;
			}
		});
	}

	@Override
	public List<AppRegister> getAppCodes(long province) {
		String sql =" SELECT DISTINCT n.app_code ,n.app_name , n.DISPLAY_ORDER  FROM uic_data_pri m , uic_app_register n  WHERE n.state = 1 AND " +
					" m.PRIVILEGE_VALUE = n.APP_CODE AND  m.user_id IN (SELECT a.user_id FROM uic_user a,uic_role b,uic_user_role_rel c ,uic_org d " + 
					" WHERE a.id = c.id AND b.role_id = c.role_id AND a.state =1 AND b.state=1 AND a.org_id = d.org_id " + 
					" AND b.role_code = 'role_appadmin'   ";
		if(0!=province){
			sql += " AND (d.org_id_seq LIKE CONCAT((SELECT f.org_id_seq FROM uic_org f WHERE f.org_id = ?),'%') )  ";
		}
		sql += "  ) ORDER BY n.DISPLAY_ORDER ";
		if(0!=province){
			return this.getJdbcTemplate().query(sql, new Object[]{province}, new RowMapper<AppRegister>(){
				
				@Override
				public AppRegister mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					AppRegister app = new AppRegister();
					app.setAppCode(rs.getString("app_code"));
					app.setAppName(rs.getString("app_name"));
					return app;
				}
			});
		}else{
			return this.getJdbcTemplate().query(sql, new RowMapper<AppRegister>(){
				
				@Override
				public AppRegister mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					AppRegister app = new AppRegister();
					app.setAppCode(rs.getString("app_code"));
					app.setAppName(rs.getString("app_name"));
					return app;
				}
			}); 
		}
	}
	
	
}
