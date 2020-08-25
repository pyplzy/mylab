package usi.dbdp.portal.sysmgr.dao.impl4mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.dto.ChangeLogInfo;
import usi.dbdp.portal.dto.LoginLogInfo;
import usi.dbdp.portal.entity.ChangeLog;
import usi.dbdp.portal.entity.LoginLog;
import usi.dbdp.portal.sysmgr.dao.LogDao;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.dto.PageObj;



/**
 * 操作日志dao
 * @author ma.guangming
 *
 */
@MysqlDb
@Repository
public class LogDaoImpl extends JdbcDaoSupport4mysql implements LogDao{
	
	/**
	 * 登录日志查询
	 * @param uicLoginLog 登录日志
	 * @param pageObj 分页对象
	 * @return 登录日志dto
	 */
	public List<LoginLogInfo> getUicLoginLogInfo(LoginLog uicLoginLog ,PageObj pageObj ){
		String sql="select  ll.login_id , ll.user_id , ll.login_ip , ll.login_time , ll.is_success , u.user_name , t.org_name   "
				+ " from uic_login_log ll left join  uic_user u on ll.user_id =u.user_id left join (select o.org_name,uo.id from uic_org o ,uic_user_has_org uo where uo.org_id=o.org_id and uo.type=1 ) t   on u.id = t.id "
				+ " where  login_time>=date_format(concat(?,' 00:00:00'),'%Y-%m-%d %H:%i:%s') and login_time<=date_format(concat(?,' 23:59:59'),'%Y-%m-%d %H:%i:%s')  ";
		ArrayList<Object> parameters = new ArrayList<Object>();
		//用到两次
		String loginTime = CommonUtil.format(uicLoginLog.getLoginTime(),"yyyy-MM-dd");
		parameters.add(loginTime);
		parameters.add(loginTime);
		if(CommonUtil.hasValue(uicLoginLog.getUserId() ) ){
			//只支持后面的模糊查询
			sql += " and ll.user_id  like concat(?,'%')";
			parameters.add(uicLoginLog.getUserId());
		}
		sql+=" order by  ll.login_time desc   ";
		return this.queryByPage(sql,parameters.toArray(),new RowMapper<LoginLogInfo>() {

					@Override
					public LoginLogInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
						LoginLogInfo uicLoginLogInfo = new LoginLogInfo();
						uicLoginLogInfo.setLoginId(rs.getLong("login_id"));
						uicLoginLogInfo.setUserId(rs.getString("user_id"));
						uicLoginLogInfo.setLoginIp(rs.getString("login_ip"));
						uicLoginLogInfo.setLoginTime(new Date(rs.getTimestamp("login_time").getTime()));
						uicLoginLogInfo.setIsSuccess(rs.getInt("is_success"));
						//人员表
						uicLoginLogInfo.setUserName(rs.getString("user_name"));
						//机构表
						uicLoginLogInfo.setOrgName(rs.getString("org_name"));
						return uicLoginLogInfo;
					}
				}, pageObj);
	}
	/**
	 * 信息变更日志查询
	 * @param uicChangeLogInfo 信息变更日志dto
	 * @param pageObj 分页对象
	 * @return 登录日志dto
	 */
	public List<ChangeLogInfo> getUicChangeLogInfo(ChangeLogInfo uicChangeLogInfo ,PageObj pageObj ){
		String sql="select  cl.change_id , cl.user_id ,cl.opt_obj, cl.opt_type , cl.opt_ip , cl.opt_content , cl.opt_time "
				+ " from uic_change_log cl left join  uic_user u on cl.user_id =u.user_id"
				+ " where  cl.opt_time>=date_format(?,'%Y-%m-%d %H:%i:%s') and cl.opt_time<=date_format(?,'%Y-%m-%d %H:%i:%s') ";
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(CommonUtil.format(uicChangeLogInfo.getOptBeginTime(),"yyyy-MM-dd HH:mm:ss"));
		list.add(CommonUtil.format(uicChangeLogInfo.getOptEndTime(),"yyyy-MM-dd HH:mm:ss"));	
				if(uicChangeLogInfo.getOptObj()!=null && !"".equals(uicChangeLogInfo.getOptObj())){
					list.add(uicChangeLogInfo.getOptObj());
					sql+=" and cl.opt_obj=? ";
				}
				sql+=" order by  cl.opt_time desc   ";
		
		return this.queryByPage(sql,list.toArray(),new RowMapper<ChangeLogInfo>() {
					@Override
					public ChangeLogInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
						ChangeLogInfo uicChangeLogInfo = new ChangeLogInfo();
						uicChangeLogInfo.setChangeId(rs.getLong("change_id"));
						uicChangeLogInfo.setUserId(rs.getString("user_id"));
						uicChangeLogInfo.setOptObj(rs.getString("opt_obj"));
						uicChangeLogInfo.setOptType(rs.getString("opt_type"));
						uicChangeLogInfo.setOptIp(rs.getString("opt_ip"));
						uicChangeLogInfo.setOptContent(rs.getString("opt_content"));
						uicChangeLogInfo.setOptTime(new Date(rs.getTimestamp("opt_time").getTime()));
						return uicChangeLogInfo;
					}
				}, pageObj);
	}
	/**
	 * 保存登录日志
	 * @param loginLog 登录日志对象实体
	 * @return true 成功 false 失败
	 */
	public boolean saveLoginLogInfo(final LoginLog loginLog){
		String sql="insert into uic_login_log(user_id,login_ip,session_id,is_success)"
				+ " values (?,?,?,?) ";
		return  this.getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, loginLog.getUserId());
				ps.setString(2, loginLog.getLoginIp());
				ps.setString(3, loginLog.getSessionId());
				ps.setInt(4, loginLog.getIsSuccess());
			}
		}) >0 ;
	}
	/**
	 * 保存信息变更日志
	 * @param changeLog 信息变更日志对象实体
	 * @return true 成功 false 失败
	 */
	public boolean saveChangeLogInfo(final ChangeLog changeLog){
		String sql="insert into uic_change_log (user_id,opt_obj,opt_type,opt_ip,opt_content,opt_time) "
				+ " VALUES(?,?,?,?,?,now()) ";
		return  this.getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, changeLog.getUserId());
				ps.setString(2, changeLog.getOptObj());
				ps.setString(3, changeLog.getOptType());
				ps.setString(4, changeLog.getOptIp());
				ps.setString(5, changeLog.getOptContent());
			}
		}) >0 ;
	}
	/**
	 * 保存登出信息
	 */
	public boolean saveLogoutInfo(String userId) {
		String sql="SELECT LOGIN_ID FROM( SELECT * FROM UIC_LOGIN_LOG WHERE USER_ID=? AND IS_SUCCESS=1 ORDER BY LOGIN_TIME DESC) limit 1";
		Long loginId=this.getJdbcTemplate().queryForObject(sql, Long.class, userId);
		String updateSql="UPDATE UIC_LOGIN_LOG SET LOGOUT_TIME=now() WHERE LOGIN_ID=?";
		return this.getJdbcTemplate().update(updateSql, loginId) >0;
	}
	
	
}
