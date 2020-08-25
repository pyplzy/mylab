package usi.dbdp.portal.util;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.dbdp.uic.dto.PageObj;





/**
 * mysql dao的父类
 * @author chen.kui
 * @date 2014年9月28日15:26:57
 */
@Repository
public class JdbcDaoSupport4mysql{
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * 为当前的DAO返回 JdbcTemplate
	 */
	public final JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public List<Map<String, Object>> queryByPageForList(String sql, PageObj pageObj, Object... params) {
		int startIndex = (pageObj.getPage()-1) * pageObj.getRows();
		pageObj.setTotal(this.getJdbcTemplate().queryForObject("SELECT COUNT(*) TOTAL FROM ("+sql+")PAGE_", params, Integer.class));
		String pageSql = "SELECT PAGE_.* FROM("+sql+" )PAGE_ LIMIT ?,? ";
		Object[] newParams = java.util.Arrays.copyOf(params,params.length+2);
		newParams[newParams.length-1]= pageObj.getRows();
		newParams[newParams.length-2]= startIndex;
		return this.jdbcTemplate.queryForList(pageSql, newParams);
	}
	
	public List<Map<String, Object>> queryByPageForList(String sql, PageObj pageObj) {
		int startIndex = (pageObj.getPage()-1) * pageObj.getRows();
		pageObj.setTotal(this.getJdbcTemplate().queryForObject("SELECT COUNT(*) TOTAL FROM ("+sql+")PAGE_", Integer.class));
		String pageSql = "SELECT PAGE_.* FROM("+sql+" )PAGE_ LIMIT ?,? ";
		Object[] params = {startIndex,pageObj.getRows()};
		return this.jdbcTemplate.queryForList(pageSql, params);
	}
	
	/**
	 * 通用的分页查询
	 * @param sql
	 * @param rowMapper
	 * @param pageObj
	 * @return
	 */
	public <T> List<T> queryByPage(String sql, Object[] params, RowMapper<T> rowMapper, PageObj pageObj) {
		int startIndex = (pageObj.getPage()-1) * pageObj.getRows();
		pageObj.setTotal(this.getJdbcTemplate().queryForObject("SELECT COUNT(*) TOTAL FROM ("+sql+") PAGE_", params, Integer.class));
		String pageSql = "SELECT PAGE_.* FROM("+sql+" )PAGE_ LIMIT ?,? ";
		Object[] newParams = java.util.Arrays.copyOf(params,params.length+2);
		newParams[newParams.length-2]= startIndex;
		newParams[newParams.length-1]= pageObj.getRows();
		return this.jdbcTemplate.query(pageSql, newParams, rowMapper);
	}
	
	/**
	 * 通用的分页查询
	 * @param sql
	 * @param params
	 * @param rowMapper
	 * @param pageObj
	 * @return
	 */
	public <T> List<T> queryByPage(String sql, RowMapper<T> rowMapper, PageObj pageObj) {
		int startIndex = (pageObj.getPage()-1) * pageObj.getRows();
		pageObj.setTotal(this.getJdbcTemplate().queryForObject("SELECT COUNT(*) TOTAL FROM ("+sql+")PAGE_", Integer.class));
		String pageSql = "SELECT PAGE_.* FROM("+sql+" )PAGE_ LIMIT ?,? ";
		Object[] params = {startIndex,pageObj.getRows()};
		return this.jdbcTemplate.query(pageSql,params, rowMapper);
	}
	
}
