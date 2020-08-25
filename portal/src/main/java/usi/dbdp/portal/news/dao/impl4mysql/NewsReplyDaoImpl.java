package usi.dbdp.portal.news.dao.impl4mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.dto.BulletinReplyDto;
import usi.dbdp.portal.dto.NewsReplyDto;
import usi.dbdp.portal.news.dao.NewsReplyDao;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.dto.PageObj;

/**
 * 公告回复Dao
 * @author nie.zhengqian
 * 创建时间：2015年3月26日 上午11:06:55
 */
@MysqlDb
@Repository
public class NewsReplyDaoImpl extends JdbcDaoSupport4mysql implements NewsReplyDao{
	/**
	 * 插入一条新闻回复
	 * @return 
	 */
	public long insertReply(final NewsReplyDto reply){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator(){

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = " INSERT INTO ptl_news_comment ( NEWS_ID, USER_ID, CONTENT, REPLY_TIME, ORG_ID, ORG_NAME, USER_NAME) "+
							" VALUES (?, ?, ?, now(), ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sql,new String[]{"COMMENT_ID"});
				ps.setLong(1, reply.getNewsId());
				ps.setString(2, reply.getUserId());
				ps.setString(3, reply.getContent());
				ps.setLong(4, reply.getOrgId());
				ps.setString(5, reply.getOrgName());
				ps.setString(6, reply.getUserName());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
		
	}
	
	/**
	 * 查出某个公告的回复
	 * @param noticeId
	 * @return
	 */
	public List<BulletinReplyDto> queryReplysByBulletinId(long bulletinId){
		String sql = "SELECT T1.COMMENT_ID," +
				"       T1.CONTENT," + 
				"       DATE_FORMAT(T1.OPT_TIME, '%Y-%m-%d %h:%i:%s') OPT_TIME," + 
				"       T1.staff_id," + 
				"       IFNULL(T2.USER_NAME,'未知') OPERATOR_NAME, " + 
				"       T3.ORG_NAME " + 
				"  FROM PTL_BULLETIN_COMMENT T1 LEFT JOIN UIC_USER T2 ON T1.STAFF_ID = T2.ID "
				+ "INNER JOIN UIC_ORG T3 ON T2.ORG_ID = T3.ORG_ID" + 
				"   WHERE T1.BULLETIN_ID = ?"+  
				"   ORDER BY T1.OPT_TIME";

		return this.getJdbcTemplate().query(sql, new RowMapper<BulletinReplyDto>() {

			public BulletinReplyDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BulletinReplyDto reply = new BulletinReplyDto();
				reply.setCommentId(rs.getLong("comment_id"));
				reply.setReplyContent(rs.getString("content"));
				reply.setReplyTime(rs.getString("opt_time"));
				reply.setStaffId(rs.getLong("staff_id"));
				reply.setUserName(rs.getString("operator_name"));
				reply.setOrgName(rs.getString("org_name"));
				return reply;
			}
		}, bulletinId);
	}
	
	/**
	 * 分页查出某个新闻的回复
	 * @param noticeId
	 * @return
	 */
	public List<NewsReplyDto> queryReplysByNewsIdWithPage(long bulletinId , PageObj pageObj){
		String sql ="SELECT t1.COMMENT_ID,t1.CONTENT,t1.REPLY_TIME, "+
				" t1.USER_NAME,t1.ORG_NAME "+
				 " FROM ptl_news_comment t1 "+
				 " WHERE t1.NEWS_ID=? "+
				"   ORDER BY T1.REPLY_TIME ";

		return this.queryByPage(sql , new Object[]{bulletinId }  , new RowMapper<NewsReplyDto>() {

			public NewsReplyDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				NewsReplyDto reply = new NewsReplyDto();
				reply.setCommentId(rs.getLong("COMMENT_ID"));
				reply.setContent(rs.getString("CONTENT"));
				reply.setReplyTime(rs.getTimestamp("REPLY_TIME"));
				reply.setUserName(rs.getString("USER_NAME"));
				reply.setOrgName(rs.getString("ORG_NAME"));
				return reply;
			}
		}, pageObj);
	}
	
}
