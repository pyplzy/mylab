package usi.dbdp.uic.base.dao.impl4oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;
import usi.dbdp.uic.base.dao.MenuDao;
import usi.dbdp.uic.entity.Menu;

/**
 * 菜单服务dao
 * @author nie.zhengqian
 * 创建时间：2015年3月3日 下午6:26:52
 */
@OracleDb
@Repository
public class MenuDaoImpl extends JdbcDaoSupport4oracle implements MenuDao{
	/**
	 * 给某应用增加一个菜单
	 * @param appCode
	 * @param menu
	 * @return
	 */
	public Long insertMenu(final String appCode, final Menu menu ){
		String seq = menu.getMenuSeq();
		Long menuId;
		final String sql =" INSERT INTO uic_menu ("
						+" menu_id,"
						+" APP_CODE,"
						+" MENU_NAME,"
						+" MENU_LEVEL,"
						+" MENU_ACTION,"
						+" P_MENU_ID,"
						+" DISPLAY_ORDER,"
						+" DEL_FLAG,"
						+" MENU_MEMO,"
						+" MENU_SEQ,"
						+" IS_LEAF"
						+" ) "
						+" VALUES(uic_menu_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String updateSql = "UPDATE UIC_MENU SET MENU_SEQ =? WHERE MENU_ID = ?";
		KeyHolder holder = new GeneratedKeyHolder();
		if(seq == null){
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, new String[]{"MENU_ID"});
					ps.setString(1, appCode);
					ps.setString(2, menu.getMenuName());
					ps.setInt(3, menu.getMenuLevel());
					ps.setString(4, menu.getMenuAction());
					ps.setLong(5, menu.getpMenuId());
					ps.setInt(6, menu.getDisplayOrder());
					ps.setInt(7, menu.getDelFlag());
					ps.setString(8, menu.getMenuMemo());
					ps.setString(9, menu.getMenuSeq());
					ps.setInt(10,menu.getIsLeaf());
					return ps;
					}
			} , holder );
			menuId = holder.getKey().longValue();
			seq = menuId+".";
			this.getJdbcTemplate().update(updateSql, new Object[]{seq,menuId});
			return menuId;
		}else{
			this.getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, new String[]{"MENU_ID"});
					ps.setString(1, appCode);
					ps.setString(2, menu.getMenuName());
					ps.setInt(3, menu.getMenuLevel());
					ps.setString(4, menu.getMenuAction());
					ps.setLong(5, menu.getpMenuId());
					ps.setInt(6, menu.getDisplayOrder());
					ps.setInt(7, menu.getDelFlag());
					ps.setString(8, menu.getMenuMemo());
					ps.setString(9, menu.getMenuSeq());
					ps.setInt(10,menu.getIsLeaf());
					return ps;
				}
			}, holder);
			menuId = holder.getKey().longValue();
			seq = seq+menuId+".";
			this.getJdbcTemplate().update(updateSql, new Object[]{seq,menuId});
			return menuId;
		}
	}
	
	/**
	 * 更新某应用菜单
	 * @param appCode
	 * @param menu
	 * @return
	 */
	public int updateMenu(final String appCode, final Menu menu){
		String sql = "UPDATE uic_menu SET"
					+" APP_CODE = ?,"
					+" MENU_NAME = ?,"
					+" MENU_LEVEL = ?,"
					+" MENU_ACTION = ?,"
					+" P_MENU_ID = ?,"
					+" DISPLAY_ORDER = ?,"
					+" DEL_FLAG = ?,"
					+" MENU_MEMO = ?,"
					+" MENU_SEQ = ?,"
					+" IS_LEAF = ?"
					+" WHERE MENU_ID = ? and APP_CODE = ?";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, menu.getAppCode());
				ps.setString(2, menu.getMenuName());
				ps.setInt(3, menu.getMenuLevel());
				ps.setString(4, menu.getMenuAction());
				ps.setLong(5, menu.getpMenuId());
				ps.setInt(6, menu.getDisplayOrder());
				ps.setInt(7, menu.getDelFlag());
				ps.setString(8, menu.getMenuMemo());
				ps.setString(9, menu.getMenuSeq());
				ps.setInt(10, menu.getIsLeaf());
				ps.setLong(11, menu.getMenuId());
				ps.setString(12, appCode);
			}});
	}
	
	/**
	 * 某应用根据菜单id查菜单信息
	 * @param appCode
	 * @param menuId
	 * @return
	 */
	public List<Menu> queryMenu(String appCode, long menuId){
		String sql = " SELECT MENU_ID,"
					+" APP_CODE,"
					+" MENU_NAME,"
					+" MENU_LEVEL,"
					+" MENU_ACTION,"
					+" P_MENU_ID,"
					+" DISPLAY_ORDER,"
					+" DEL_FLAG,"
					+" MENU_MEMO,"
					+" MENU_SEQ,"
					+" IS_LEAF"
					+" FROM UIC_MENU"
					+" WHERE APP_CODE = ? AND MENU_ID = ? order by DISPLAY_ORDER ";
			return this.getJdbcTemplate().query(sql, new Object[]{appCode, menuId}, new RowMapper<Menu>(){
				@Override
				public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
					Menu menu = new Menu();
					menu.setMenuId(rs.getLong("MENU_ID"));
					menu.setAppCode(rs.getString("APP_CODE"));
					menu.setMenuName(rs.getString("MENU_NAME"));
					menu.setMenuLevel(rs.getInt("MENU_LEVEL"));
					menu.setMenuAction(rs.getString("MENU_ACTION"));
					menu.setpMenuId(rs.getInt("P_MENU_ID"));
					menu.setDisplayOrder(rs.getInt("DISPLAY_ORDER"));
					menu.setDelFlag(rs.getInt("DEL_FLAG"));
					menu.setMenuMemo(rs.getString("MENU_MEMO"));
					menu.setMenuSeq(rs.getString("MENU_SEQ"));
					menu.setIsLeaf(rs.getInt("IS_LEAF"));
					return menu;
				}
			});
	}
	/**
	 * 删除某应用菜单（逻辑删除，设置菜单状态为失效）
	 * @param appCode
	 * @param menuId
	 * @return
	 */
	public int deleteMenuById(String appCode, long menuId){
		String sql = "update uic_menu set del_flag = 1 where APP_CODE = ? and MENU_ID = ? ";
		return this.getJdbcTemplate().update(sql, new Object[]{appCode, menuId} );
	}
	
	/**
	 * 获取菜单的直接子菜单列表
	 * @param pMenuId
	 * @return
	 */
	public List<Menu> getDirectMenusBypMenuId(long pMenuId){
		String sql = " SELECT"
					+" MENU_ID,"
					+" APP_CODE,"
					+" MENU_NAME,"
					+" MENU_LEVEL,"
					+" MENU_ACTION,"
					+" P_MENU_ID,"
					+" DISPLAY_ORDER,"
					+" DEL_FLAG,"
					+" MENU_MEMO,"
					+" MENU_SEQ,"
					+" IS_LEAF"
					+" FROM uic_menu"
					+" WHERE P_MENU_ID = ? "
					+ "and STATE = 1  order by DISPLAY_ORDER";
		return this.getJdbcTemplate().query(sql, new Object[]{pMenuId}, new RowMapper<Menu>(){
			@Override
			public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
				Menu menu = new Menu();
				menu.setMenuId(rs.getLong("MENU_ID"));
				menu.setAppCode(rs.getString("APP_CODE"));
				menu.setMenuName(rs.getString("MENU_NAME"));
				menu.setMenuLevel(rs.getInt("MENU_LEVEL"));
				menu.setMenuAction(rs.getString("MENU_ACTION"));
				menu.setpMenuId(rs.getInt("P_MENU_ID"));
				menu.setDisplayOrder(rs.getInt("DISPLAY_ORDER"));
				menu.setDelFlag(rs.getInt("DEL_FLAG"));
				menu.setMenuMemo(rs.getString("MENU_MEMO"));
				menu.setMenuSeq(rs.getString("MENU_SEQ"));
				menu.setIsLeaf(rs.getInt("IS_LEAF"));
				return menu;
			}
		});
	}
	/**
	 * @author ma.guangming
	 * 获取某应用所有激活的菜单
	 * @param appCode
	 * @return 菜单列表
	 */
	public List<Menu> getAllMenusByAppCode(String appCode){
		String sql = " select menu_id,app_code,menu_name,menu_level,menu_action,p_menu_id,display_order,del_flag,menu_memo,menu_seq,is_leaf,  "
					+" (CASE IS_LEAF WHEN 0 THEN 'true' ELSE 'false' END) is_Parent "
					+" from uic_menu  "
					+" where app_code = ? and del_flag = 0  order by DISPLAY_ORDER";
		return this.getJdbcTemplate().query(sql, new Object[]{appCode}, new RowMapper<Menu>(){
			@Override
			public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
				Menu menu = new Menu();
				menu.setMenuId(rs.getLong("MENU_ID"));
				menu.setAppCode(rs.getString("APP_CODE"));
				menu.setMenuName(rs.getString("MENU_NAME"));
				menu.setMenuLevel(rs.getInt("MENU_LEVEL"));
				menu.setMenuAction(rs.getString("MENU_ACTION"));
				menu.setpMenuId(rs.getInt("P_MENU_ID"));
				menu.setDisplayOrder(rs.getInt("DISPLAY_ORDER"));
				menu.setDelFlag(rs.getInt("del_flag"));
				menu.setMenuMemo(rs.getString("MENU_MEMO"));
				menu.setMenuSeq(rs.getString("MENU_SEQ"));
				menu.setIsLeaf(rs.getInt("IS_LEAF"));
				menu.setIsParent(rs.getString("is_Parent"));
				return menu;
			}
		});
	}

	/**
	 * 根据菜单id删除角色菜单关联表
	 * @param menuId 菜单id
	 * @return
	 */
	public int deleteMenuRoleRel(long menuId) {
		String sql = " delete from uic_role_has_menu where menu_id = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{menuId});
	}
}
