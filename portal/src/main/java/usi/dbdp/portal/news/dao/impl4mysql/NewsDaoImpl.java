package usi.dbdp.portal.news.dao.impl4mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.dto.BulletinDto;
import usi.dbdp.portal.dto.NewsDto;
import usi.dbdp.portal.news.dao.NewsDao;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.util.CommonUtil;

/**
 * 公告管理Dao
 * @author nie.zhengqian
 * 创建时间：2015年3月24日 下午1:38:21
 */
@MysqlDb
@Repository
public class NewsDaoImpl extends JdbcDaoSupport4mysql implements NewsDao{
	
	/**
	 * 插入公告
	 * @param ptlBulletin
	 * @return 主键
	 */
	public long insertNews(final NewsDto news){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "insert into ptl_news (title, cover_summary,cover_pic_url,content, state, "
						+ "user_id, release_time, reply_flag,user_name) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?,?)";
				PreparedStatement ps = con.prepareStatement(sql, new String[] { "news_id" });
				ps.setString(1, news.getTitle());
				ps.setString(2, news.getDigest());
				ps.setString(3, news.getCoverPhoto());
				ps.setString(4, news.getContent());
				ps.setInt(5, news.getState());
				ps.setString(6, news.getUserId());
				ps.setTimestamp(7, news.getReleaseTime() == null ? null : new Timestamp(news.getReleaseTime().getTime()));
				ps.setInt(8, news.getReplyFlag());
				ps.setString(9, news.getUserName());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * 修改公告
	 * @param ptlBulletin
	 */
	public int updateNews(final NewsDto news){
		String sql =  "update ptl_news set title=?, content=?, state=?, create_time=now(), "
				+ "release_time=?, reply_flag=?,cover_summary=?, cover_pic_url=? where news_id=?";
		return this.getJdbcTemplate().update(sql, new Object[]{
				news.getTitle(),news.getContent(),news.getState(),
				news.getReleaseTime(),news.getReplyFlag(),
				news.getDigest(),news.getCoverPhoto(),
				news.getNewsId()});
	}
	/**
	 * 查出草稿
	 * @param staffId
	 * @return
	 */
	public List<NewsDto> queryDraftsByUserId(String userId, PageObj pageObj){
		String sql =  "SELECT T.NEWS_ID, T.TITLE, T.CREATE_TIME "
				+ "  FROM PTL_NEWS T WHERE T.USER_ID = ? "
				+ "   AND T.STATE = 0 ORDER BY T.CREATE_TIME DESC";
		return this.queryByPage(sql, new Object[]{userId}, new RowMapper<NewsDto>(){

			public NewsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				NewsDto item = new NewsDto();
				item.setNewsId(rs.getLong("NEWS_ID"));
				item.setTitle(rs.getString("TITLE"));
				item.setCreateTime(rs.getTimestamp("CREATE_TIME"));
				return item;
			}
		}, pageObj);
	}
	
	/**
	 * 查出新闻
	 * @param userId 创建人id
	 * @return
	 * @throws ParseException 
	 */
	/*public List<NewsDto> queryPublishedByUserId(NewsDto newsDto, PageObj pageObj ) throws ParseException{
		String sql = "SELECT *"
				+ "  FROM (SELECT T.USER_ID,T.NEWS_ID,T.TITLE,T.COVER_SUMMARY,T.COVER_PIC_URL,"
				+ "  DATE_FORMAT(T.CREATE_TIME, '%Y-%m-%d %H:%i:%S') CREATE_TIME,"
				+ "	 DATE_FORMAT(T.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME,"
				+ "  T.READ_CNT,T.USER_NAME,"
				+ "  (SELECT COUNT(*) FROM PTL_NEWS_COMMENT R WHERE R.NEWS_ID = T.NEWS_ID) REPLY_NUM,T.REPLY_FLAG "
				+ "   FROM ptl_news T WHERE 1=1 ";
		sql += "     AND T.STATE = 1) T1 WHERE 1 = 1 ";
		if (!"-1".equals(newsDto.getUserId()) || !CommonUtil.hasValue(newsDto.getUserId())) {
			sql += " AND T1.USER_ID = " + newsDto.getUserId() + "\n";
		}
		if(CommonUtil.hasValue(newsDto.getTitle())){
			sql += " AND T1.TITLE LIKE '%" + newsDto.getTitle() +"%'";
		}
		if(CommonUtil.hasValue(newsDto.getStartTime())){//mysql中字符串转为date是str_to_date
			sql += "AND T1.RELEASE_TIME > STR_TO_DATE('"+newsDto.getStartTime()+"','%Y-%m-%d')";
		}
		if(CommonUtil.hasValue(newsDto.getEndTime())){//oracle是to_date
			sql += "AND T1.RELEASE_TIME < STR_TO_DATE('"+newsDto.getEndTime()+"','%Y-%m-%d')";
		}
		sql += " ORDER BY T1.RELEASE_TIME DESC";
		return this.queryByPage(sql,new RowMapper<NewsDto>(){

			public NewsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				NewsDto newsDto = new NewsDto();
				newsDto.setNewsId(rs.getLong("news_id"));
				newsDto.setTitle(rs.getString("title"));
				newsDto.setDigest(rs.getString("cover_summary"));
				newsDto.setCoverPhoto(rs.getString("cover_pic_url"));
				newsDto.setCreateTime(rs.getDate("create_time"));
				newsDto.setReleaseTime(rs.getDate("release_time"));
				newsDto.setReadCnt(rs.getInt("read_cnt"));
				newsDto.setUserId(rs.getString("user_id"));
				newsDto.setUserName(rs.getString("user_name"));
				newsDto.setReplyNum(rs.getInt("reply_num"));
				newsDto.setReplyFlag(rs.getInt("reply_flag"));
				return newsDto;
			}
		}, pageObj);
		
	}*/
	
	public List<NewsDto> queryPublishedByUserId(NewsDto newsDto, PageObj pageObj ) throws ParseException{
		List<Object> list = new ArrayList<Object>();
		String sql = "SELECT *"
				+ "  FROM (SELECT T.USER_ID,T.NEWS_ID,T.TITLE,T.COVER_SUMMARY,T.COVER_PIC_URL,"
				+ "  DATE_FORMAT(T.CREATE_TIME, '%Y-%m-%d %H:%i:%S') CREATE_TIME,"
				+ "	 DATE_FORMAT(T.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME,"
				+ "  T.READ_CNT,T.USER_NAME,"
				+ "  (SELECT COUNT(*) FROM PTL_NEWS_COMMENT R WHERE R.NEWS_ID = T.NEWS_ID) REPLY_NUM,T.REPLY_FLAG "
				+ "   FROM ptl_news T WHERE 1=1 ";
		sql += "     AND T.STATE = 1) T1 WHERE 1 = 1 ";
		if (!"-1".equals(newsDto.getUserId()) || !CommonUtil.hasValue(newsDto.getUserId())) {
			sql += " AND T1.USER_ID = ?";
			list.add(newsDto.getUserId());
		}
		if(CommonUtil.hasValue(newsDto.getTitle())){
			sql += " AND T1.TITLE LIKE ?";
			list.add("%"+ newsDto.getTitle() +"%");
		}
		if(newsDto.getStTime() != null){
			sql += "AND T1.RELEASE_TIME > ?";
			list.add(newsDto.getStTime());
		}
		if(newsDto.getEdTime() != null){
			sql += "AND T1.RELEASE_TIME < ?";
			list.add(newsDto.getEdTime());
		}
		sql += " ORDER BY T1.RELEASE_TIME DESC";
		return this.queryByPage(sql,list.toArray(),new RowMapper<NewsDto>(){

			public NewsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				NewsDto newsDto = new NewsDto();
				newsDto.setNewsId(rs.getLong("news_id"));
				newsDto.setTitle(rs.getString("title"));
				newsDto.setDigest(rs.getString("cover_summary"));
				newsDto.setCoverPhoto(rs.getString("cover_pic_url"));
				newsDto.setCreateTime(rs.getDate("create_time"));
				newsDto.setReleaseTime(rs.getDate("release_time"));
				newsDto.setReadCnt(rs.getInt("read_cnt"));
				newsDto.setUserId(rs.getString("user_id"));
				newsDto.setUserName(rs.getString("user_name"));
				newsDto.setReplyNum(rs.getInt("reply_num"));
				newsDto.setReplyFlag(rs.getInt("reply_flag"));
				return newsDto;
			}
		}, pageObj);
		
	}
	
	/**
	 * 批量删除新闻
	 * @param newsIds
	 * @return
	 */
	public void batchDeleteNews(final long[] newsIds){
		String sql = "DELETE FROM ptl_news WHERE NEWS_ID = ?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, newsIds[i]);
			}
			
			public int getBatchSize() {
				return newsIds.length;
			}
		});
		
	}
	
	/**
	 * 批量修改新闻至删除状态
	 * @param newsIds
	 * @return 
	 */
	public void batchUpdateToDeletedStatus(final long[] newsIds){
		String sql = "UPDATE PTL_NEWS SET STATE = 3 WHERE NEWS_ID = ?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, newsIds[i]);
			}
			
			public int getBatchSize() {
				return newsIds.length;
			}
		});
	}
	
	/**
	 * 根据id删除该新闻
	 */
	public void deleteNewsById(long newsId){
		String sql = "DELETE FROM ptl_news WHERE NEWS_ID = "+newsId;
		this.getJdbcTemplate().update(sql);
	}
	
	/**
	 * 根据id修改该新闻为删除状态
	 * @param newsId
	 */
	public void updateDelStatusById(long newsId){
		String sql = "UPDATE PTL_NEWS SET STATE = 3 WHERE NEWS_ID = "+newsId;
		this.getJdbcTemplate().update(sql);
	}
	
	/**
	 * 批量修改公告至发布状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToPublishedStatus(final long[] bulletinIds){
		String sql = "UPDATE ptl_news SET STATE = 1, RELEASE_TIME = NOW() WHERE NEWS_ID = ?";
		this.getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setLong(1, bulletinIds[i]);
					}

					public int getBatchSize() {
						return bulletinIds.length;
					}
				});

	}
	
	/**
	 * 批量修改公告至草稿状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToDraftsStatus(final long[] bulletinIds){
		String sql = "UPDATE PTL_BULLETIN SET STATE = 0, RELEASE_TIME = NULL, CREATE_TIME = NOW(), "
				+ "READ_CNT = 0 WHERE BULLETIN_ID = ?";
		this.getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setLong(1, bulletinIds[i]);
					}

					public int getBatchSize() {
						return bulletinIds.length;
					}
				});
	}
	
	/**
	 * 根据ID查询新闻信息
	 * @param newsId
	 * @return
	 */
	public NewsDto queryNewsById(Long newsId){
		String sql = "select t1.news_id,t1.user_id,t1.title,t1.content,t1.reply_flag,t1.release_time,t1.cover_pic_url,"
				+ "t1.user_name,t1.read_cnt,t1.cover_summary,t1.org_id,t1.org_name,t1.create_time "
				+ "from ptl_news t1 where t1.news_id=? ";
		List<NewsDto> list = this.getJdbcTemplate().query(sql, new Object[]{newsId}, new RowMapper<NewsDto>(){

			public NewsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				NewsDto item = new NewsDto();
				item.setNewsId(rs.getLong("news_id"));
				item.setUserId(rs.getString("user_id"));
				item.setTitle(rs.getString("title"));
				item.setContent(rs.getString("content"));
				item.setReplyFlag(rs.getInt("reply_flag"));
				item.setCoverPhoto(rs.getString("cover_pic_url"));
				item.setReleaseTime(rs.getTimestamp("release_time"));
				item.setCreateTime(rs.getTimestamp("create_time"));
				item.setUserName(rs.getString("user_name"));
				item.setReadCnt(rs.getInt("read_cnt"));
				item.setDigest(rs.getString("cover_summary"));
				item.setOrgId(rs.getLong("org_id"));
				item.setOrgName(rs.getString("org_name"));
				return item;
			}
		});
		return list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 更改上线状态和下线状态
	 * @param bulletinId 
	 * @param state
	 * @param loseDate
	 * @return
	 */
	public int updateStatus(Long bulletinId, int state, Date loseDate){
		String sql = "UPDATE PTL_BULLETIN T SET T.STATE = ?, T.LOSE_TIME = ? WHERE T.BULLETIN_ID = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{state, loseDate, bulletinId});
	}
	
	/**
	 * 查出首页的公告
	 * @param createStaff
	 * @param date
	 * @return
	 */
	public List<BulletinDto> queryAllByCreateStaff(Long createStaff, Date date){
		String sql = "SELECT T.BULLETIN_ID,"
				+ "       T.TITLE,"
				+ "       T.STICK_UP,"
				+ "       (SELECT COUNT(*)"
				+ "          FROM PTL_FILE T1"
				+ "         WHERE T1.GROUP_CODE = 'NOTICE'"
				+ "           AND T1.RELATION_ID = T.BULLETIN_ID"
				+ "           AND T1.IS_DEL = 0) FILE_NUM,"
				+ "       DATE_FORMAT(T.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME "
				+ "  FROM PTL_BULLETIN T " + " WHERE T.STATE = 1 "
				+ "   AND (T.LOSE_TIME > ? OR T.LOSE_TIME IS NULL) "
				+ " ORDER BY T.STICK_UP DESC, T.RELEASE_TIME DESC";
		return this.getJdbcTemplate().query(sql, new RowMapper<BulletinDto>() {

			public BulletinDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BulletinDto bulletinDto = new BulletinDto();
				bulletinDto.setBulletinId(rs.getLong("bulletin_id"));
				bulletinDto.setTitle(rs.getString("title"));
				bulletinDto.setStickUp(rs.getInt("stick_up"));
				bulletinDto.setFileNum(rs.getInt("file_num"));
				bulletinDto.setReleaseDate(rs.getString("release_time"));
				return bulletinDto;
			}
		}, date);
		
	}
	
	/**
	 * 修改阅读次数
	 * @param bulletinId
	 * @return
	 */
	public int updateVisitNum(Long newsId){
		String sql = "UPDATE ptl_news SET READ_CNT = READ_CNT+1 WHERE NEWS_ID = ?";
		return this.getJdbcTemplate().update(sql, newsId);
	}
	
	/**
	 * 修改置顶标识
	 * @param bulletinId
	 * @param stickUp
	 * @return
	 */
	public int updateStickById(long bulletinId, int stickUp){
		String sql = "UPDATE PTL_BULLETIN SET STICK_UP = ? WHERE BULLETIN_ID = ?";
		return this.getJdbcTemplate().update(sql, stickUp, bulletinId);
		
	}
	
	/**
	 * 修改回复标识
	 * @param bulletinId
	 * @param replyFlag
	 * @return
	 */
	public int updateReplyById(long newsId, int replyFlag){
		String sql = "UPDATE PTL_NEWS SET REPLY_FLAG = ? WHERE NEWS_ID = ?";
		return this.getJdbcTemplate().update(sql, replyFlag, newsId);
	}
	/**
	 * 分页查询公告
	 * @param createStaff
	 * @param date
	 * @param pageObj
	 * @return
	 */
	public List<BulletinDto> queryNoticeByPage(Date date , PageObj pageObj, Long province){
		String sql = "";
		if(0==province){   //查询系统所以公告
			sql = "SELECT T.BULLETIN_ID, u.user_name, "
					+ "       T.TITLE,"
					+ "       T.STICK_UP,"
					+ "       (SELECT COUNT(*)"
					+ "          FROM PTL_FILE T1"
					+ "         WHERE T1.GROUP_CODE = 'NOTICE'"
					+ "           AND T1.RELATION_ID = T.BULLETIN_ID"
					+ "           AND T1.IS_DEL = 0) FILE_NUM,"
					+ "       DATE_FORMAT(T.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME "
					+ "  FROM PTL_BULLETIN T , uic_user u  " + " WHERE T.STATE = 1 and t.create_staff=u.id "
					+ "   AND (T.LOSE_TIME > ? OR T.LOSE_TIME IS NULL) "
					+ " ORDER BY T.STICK_UP DESC, T.RELEASE_TIME DESC";
		}else{  //查对应省份公告
			sql = 
					"SELECT  T.BULLETIN_ID, u.user_name, " +
							"       T.TITLE, " + 
							"       T.STICK_UP, " + 
							"       (SELECT COUNT(*) " + 
							"          FROM PTL_FILE T1 " + 
							"         WHERE T1.GROUP_CODE = 'NOTICE' " + 
							"           AND T1.RELATION_ID = T.BULLETIN_ID " + 
							"           AND T1.IS_DEL = 0) FILE_NUM, " + 
							"       DATE_FORMAT(T.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME " + 
							" FROM PTL_BULLETIN t ,uic_user u ,uic_org a " + 
							" WHERE u.id = t.create_staff AND u.org_id = a.org_id AND u.state = 1 AND a.state=1  AND T.STATE = 1 " + 
							" AND a.org_id_seq LIKE CONCAT((SELECT o.org_id_seq FROM uic_org o WHERE o.org_id = "+province+"),'%') " + 
							" AND (T.LOSE_TIME > ? OR T.LOSE_TIME IS NULL) " + 
							" ORDER BY T.STICK_UP DESC, T.RELEASE_TIME DESC";
		}
		
		return this.queryByPage(sql, new Object[]{date},new RowMapper<BulletinDto>() {

			public BulletinDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BulletinDto bulletinDto = new BulletinDto();
				bulletinDto.setBulletinId(rs.getLong("bulletin_id"));
				bulletinDto.setTitle(rs.getString("title"));
				bulletinDto.setStickUp(rs.getInt("stick_up"));
				bulletinDto.setFileNum(rs.getInt("file_num"));
				bulletinDto.setReleaseDate(rs.getString("release_time"));
				bulletinDto.setCreateName(rs.getString("user_name"));
				return bulletinDto;
			}
		}, pageObj);
		
	}
	/**
	 * 分页查出上线公告
	 * @return
	 */
	public List<BulletinDto> queryNoticePublishedByPage(PageObj pageObj){
		String sql = "SELECT *"
				+ "  FROM (SELECT  u.user_name,  "
				+ "	T.BULLETIN_ID,"
				+ "               T.TITLE,"
				+ "				T.STICK_UP,"
				+ "               DATE_FORMAT(T.CREATE_TIME, '%Y-%m-%d %H:%i:%S') CREATE_TIME,"
				+ "               DATE_FORMAT(T.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME,"
				+ "               DATE_FORMAT(T.LOSE_TIME, '%Y-%m-%d %H:%i:%S') LOSE_TIME,"
				+ "               T.READ_CNT,"
				+ "               (SELECT COUNT(*) "
				+ "                  FROM ptl_file T1"
				+ "                 WHERE T1.GROUP_CODE = 'NOTICE' "
				+ "                   AND T1.RELATION_ID = T.BULLETIN_ID "
				+ "                   AND T1.IS_DEL = 0) FILE_NUM,"
				+ "				(SELECT COUNT(*)"
				+ "   				FROM PTL_BULLETIN_COMMENT R"
				+ "  				  WHERE R.BULLETIN_ID = T.BULLETIN_ID) REPLY_NUM,"
				+ " 				T.REPLY_FLAG "
				+ "          FROM ptl_bulletin T  , uic_user u  "
				+ " WHERE T.create_staff=u.id and  "
				+ " T.STATE=1 AND (T.LOSE_TIME > now() OR T.LOSE_TIME IS NULL)  ) T1 ";
		
		sql += "ORDER BY T1.STICK_UP DESC, T1.CREATE_TIME DESC";
		return this.queryByPage(sql, new RowMapper<BulletinDto>(){

			public BulletinDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BulletinDto bulletinDto = new BulletinDto();
				bulletinDto.setBulletinId(rs.getLong("bulletin_id"));
				bulletinDto.setTitle(rs.getString("title"));
				bulletinDto.setStickUp(rs.getInt("stick_up"));
				bulletinDto.setFileNum(rs.getInt("file_num"));
				bulletinDto.setReplyNum(rs.getInt("reply_num"));
				bulletinDto.setCreateDate(rs.getString("create_time") == null ? ""
						: rs.getString("create_time"));
				bulletinDto.setReleaseDate(rs.getString("release_time") == null ? ""
						: rs.getString("release_time"));
				bulletinDto.setLoseTime(rs.getString("lose_time") == null ? ""
						: rs.getString("lose_time"));
				bulletinDto.setReadCnt(rs.getInt("read_cnt"));
				bulletinDto.setReplyFlag(rs.getInt("reply_flag"));
				bulletinDto.setCreateName(rs.getString("user_name"));
				return bulletinDto;
			}
		}, pageObj);
	}

	@Override
	public String getCoverPhotoUrl(String groupcode, Long newsId) {
		String sql = "select t.absolutepath from ptl_file t where t.group_code=? and t.relation_id=?";
		List<String> list = this.getJdbcTemplate().queryForList(sql, new Object[]{groupcode,newsId},String.class);
		return list.size()>0 ? list.get(0) : null;
	}

	@Override
	public List<NewsDto> getNewsForPortal(Long province) {
		String sql = "";
		Object[] params = null;
		if(province == null || province == 0){
			sql = "select t0.* from ptl_news t0 "+
					"where t0.state=1 order by t0.release_time desc limit 5";
			params = new Object[]{};
		} else {
			sql = "select t0.* from ptl_news t0,uic_org t1 "+
				"where t0.state=1 and t0.org_id=t1.org_id and t1.org_id_seq like concat((select o.org_id_seq from uic_org o where o.org_id = ?),'%') "+
				"order by t0.release_time desc limit 5";
			params = new Object[]{province};
		}
		return this.getJdbcTemplate().query(sql, params, new RowMapper<NewsDto>(){

			public NewsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				NewsDto newsDto = new NewsDto();
				newsDto.setNewsId(rs.getLong("news_id"));
				newsDto.setTitle(rs.getString("title"));
				newsDto.setDigest(rs.getString("cover_summary"));
				newsDto.setCoverPhoto(rs.getString("cover_pic_url"));
				return newsDto;
			}
		});
	}
	
}
