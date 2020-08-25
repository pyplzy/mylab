package usi.dbdp.portal.bulletin.dao.impl4mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.app.util.OraclePasswdUtil;
import usi.dbdp.portal.bulletin.dao.BulletinDao;
import usi.dbdp.portal.dto.BulletinDto;
import usi.dbdp.portal.entity.PtlBulletin;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.dto.PageObj;

/**
 * 公告管理Dao
 * @author nie.zhengqian
 * 创建时间：2015年3月24日 下午1:38:21
 */
@MysqlDb
@Repository
public class BulletinDaoImpl extends JdbcDaoSupport4mysql implements BulletinDao{
	
	/**
	 * 插入公告
	 * @param ptlBulletin
	 * @return 主键
	 */
	public long insertNotice(final PtlBulletin ptlBulletin){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String sql = " INSERT INTO ptl_bulletin (TITLE, CONTENT, STATE, READ_CNT, "
						+ "CREATE_STAFF, CREATE_TIME, LOSE_TIME, RELEASE_TIME, REPLY_FLAG) "
						+ " VALUES (?, ?, ?, 0, ?, NOW(), ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sql,
						new String[] { "BULLETIN_ID" });
				ps.setString(1, ptlBulletin.getTitle());
				ps.setString(2, ptlBulletin.getContent());
				ps.setInt(3, ptlBulletin.getState());
				ps.setLong(4, ptlBulletin.getCreateStaff());
				ps.setTimestamp(5, ptlBulletin.getLoseDate() == null ? null
						: new Timestamp(ptlBulletin.getLoseDate().getTime()));
				ps.setTimestamp(6, ptlBulletin.getReleaseTime() == null ? null
						: new Timestamp(ptlBulletin.getReleaseTime().getTime()));
				ps.setInt(7, ptlBulletin.getReplyFlag());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * 修改公告
	 * @param ptlBulletin
	 */
	public int updateNotice(final PtlBulletin ptlBulletin){
		String sql =  "UPDATE ptl_bulletin SET TITLE = ?, CONTENT = ?, STATE = ?, CREATE_TIME = NOW(), "
				+ "LOSE_TIME = ?, RELEASE_TIME = ?, REPLY_FLAG = ? WHERE BULLETIN_ID = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{
					ptlBulletin.getTitle(),ptlBulletin.getContent(),
					ptlBulletin.getState(),ptlBulletin.getLoseDate(),
					ptlBulletin.getReleaseTime(),ptlBulletin.getReplyFlag(),
					ptlBulletin.getBulletinId()
				});
	}
	/**
	 * 查出草稿
	 * @param staffId
	 * @return
	 */
	public List<BulletinDto> queryNoticeDraftsByCreateStaff(Long createStaff, PageObj pageObj){
		String sql =  "SELECT T.BULLETIN_ID,"
				+ "       T.TITLE,"
				+ "       DATE_FORMAT(T.CREATE_TIME, '%Y-%m-%d %H:%i:%S') CREATE_TIME,"
				+ "       (SELECT COUNT(*) FROM PTL_FILE T1 "
				+ "         WHERE T1.GROUP_CODE = 'NOTICE' "
				+ "           AND T1.RELATION_ID = T.BULLETIN_ID "
				+ "           AND T1.IS_DEL = 0) FILE_NUM "
				+ "  FROM PTL_BULLETIN T WHERE T.CREATE_STAFF = ? "
				+ "   AND T.STATE = 0 ORDER BY T.CREATE_TIME DESC";
		return this.queryByPage(sql, new Object[]{createStaff}, new RowMapper<BulletinDto>(){

			public BulletinDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BulletinDto bulletinDto = new BulletinDto();
				bulletinDto.setBulletinId(rs.getLong("bulletin_id"));
				bulletinDto.setTitle(rs.getString("title"));
				bulletinDto.setFileNum(rs.getInt("file_num"));
				bulletinDto.setCreateDate(rs.getString("create_time")); 
				return bulletinDto;
			}
		}, pageObj);
	}
	/**
	 * 查出公告
	 * @param createStaff 创建人id
	 * @param isLine  是否下线
	 * @return
	 */
	public List<BulletinDto> queryNoticePublishedByStaffId(long createStaff, String offline, PageObj pageObj ){
		String sql = "SELECT *"
				+ "  FROM (SELECT T.BULLETIN_ID,"
				+ "               T.TITLE,"
				+ "				T.STICK_UP,"
				+ "               DATE_FORMAT(T.CREATE_TIME, '%Y-%m-%d %H:%i:%S') CREATE_TIME,"
				+ "               DATE_FORMAT(T.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME,"
				+ "               DATE_FORMAT(T.LOSE_TIME, '%Y-%m-%d %H:%i:%S') LOSE_TIME,"
				+ "               T.READ_CNT,"
				+ "               T.STATE,"
				+ "               T2.USER_NAME,"
				+ "               CASE T.STATE"
				+ "                 WHEN 2 THEN 'y' "
				+ "                 ELSE "
				+ "                  (CASE WHEN T.LOSE_TIME < now() THEN 'y' "
				+ "                    ELSE 'n' "
				+ "                  END) "
				+ "               END OFF_LINE, "
				+ "               (SELECT COUNT(*) "
				+ "                  FROM ptl_file T1"
				+ "                 WHERE T1.GROUP_CODE = 'NOTICE' "
				+ "                   AND T1.RELATION_ID = T.BULLETIN_ID "
				+ "                   AND T1.IS_DEL = 0) FILE_NUM,"
				+ "				(SELECT COUNT(*)"
				+ "   				FROM PTL_BULLETIN_COMMENT R"
				+ "  				  WHERE R.BULLETIN_ID = T.BULLETIN_ID) REPLY_NUM,"
				+ " 				T.REPLY_FLAG "
				+ "          FROM ptl_bulletin T LEFT JOIN uic_user T2 ON T.CREATE_STAFF = T2.ID WHERE 1=1 ";
		if (createStaff != -1) {
			sql += "           AND T.CREATE_STAFF = " + createStaff + " ";
		}
		sql += "           AND T.STATE BETWEEN 1 AND 2) T1 ";
		if (CommonUtil.hasValue(offline)) {
			sql += "WHERE T1.OFF_LINE ='" + offline + "' ";
		}
		sql += "ORDER BY T1.STICK_UP DESC, T1.CREATE_TIME DESC";
		return this.queryByPage(sql,new RowMapper<BulletinDto>(){

			public BulletinDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BulletinDto bulletinDto = new BulletinDto();
				bulletinDto.setBulletinId(rs.getLong("bulletin_id"));
				bulletinDto.setTitle(rs.getString("title"));
				bulletinDto.setStickUp(rs.getInt("stick_up"));
				bulletinDto.setFileNum(rs.getInt("file_num"));
				bulletinDto.setReplyNum(rs.getInt("reply_num"));
				bulletinDto.setCreateDate(rs.getString("create_time"));
				bulletinDto.setReleaseDate(rs.getString("RELEASE_TIME"));
				bulletinDto.setLoseTime(rs.getString("lose_time"));
				bulletinDto.setReadCnt(rs.getInt("read_cnt"));
				bulletinDto.setState(rs.getInt("STATE"));
				bulletinDto.setOffline(rs.getString("off_line"));
				bulletinDto.setCreateName(rs.getString("USER_NAME"));
				bulletinDto.setReplyFlag(rs.getInt("reply_flag"));
				return bulletinDto;
			}
		}, pageObj);
		
	}
	
	/**
	 * 批量删除公告
	 * @param bulletinIds
	 * @return
	 */
	public void batchDeleteNotice(final long[] bulletinIds){
		String sql = "DELETE FROM ptl_bulletin WHERE BULLETIN_ID = ?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, bulletinIds[i]);
			}
			
			public int getBatchSize() {
				return bulletinIds.length;
			}
		});
		
	}
	
	/**
	 * 批量修改公告至删除状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToDeletedStatus(final long[] bulletinIds){
		String sql = "UPDATE PTL_BULLETIN SET STATE = 3 WHERE BULLETIN_ID = ?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, bulletinIds[i]);
			}
			
			public int getBatchSize() {
				return bulletinIds.length;
			}
		});
	}
	
	/**
	 * 批量修改公告至发布状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToPublishedStatus(final long[] bulletinIds){
		String sql = "UPDATE PTL_BULLETIN SET STATE = 1, RELEASE_TIME = NOW() WHERE BULLETIN_ID = ?";
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
	 * 根据ID查询公告信息
	 * @param bulletinId
	 * @return
	 */
	public BulletinDto queryNoticeById(Long bulletinId){
		String sql = "SELECT 	t1.read_cnt,	"
				+ "	T1.BULLETIN_ID,"
				+ "       T1.CREATE_STAFF,"
				+ "       T1.TITLE,"
				+ "       T1.CONTENT,"
				+ "       T1.REPLY_FLAG,"
				+ "       DATE_FORMAT(T1.LOSE_TIME, '%Y-%m-%d %H:%i:%S') LOSE_TIME,"
				+ "       DATE_FORMAT(T1.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME,"
				+ "       T1.REPLY_FLAG,"
				+OraclePasswdUtil.getDecryptStr("T2.USER_NAME")+" CREATE_NAME"
				+ "  FROM PTL_BULLETIN T1 LEFT JOIN UIC_USER T2 ON T1.CREATE_STAFF = T2.ID"
				+ " WHERE T1.BULLETIN_ID = ?";
		List<BulletinDto> bulletinDto = this.getJdbcTemplate().query(sql, new Object[]{bulletinId}, new RowMapper<BulletinDto>(){

			public BulletinDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BulletinDto bulletinDto = new BulletinDto();
				bulletinDto.setBulletinId(rs.getLong("BULLETIN_ID"));
				bulletinDto.setCreateStaff(rs.getLong("CREATE_STAFF"));
				bulletinDto.setTitle(rs.getString("TITLE"));
				bulletinDto.setContent(rs.getString("CONTENT"));
				bulletinDto.setReplyFlag(rs.getInt("REPLY_FLAG"));
				bulletinDto.setLoseTime(rs.getString("LOSE_TIME"));
				bulletinDto.setReleaseDate(rs.getString("RELEASE_TIME"));
				bulletinDto.setReplyFlag(rs.getInt("reply_flag"));
				bulletinDto.setCreateName(rs.getString("CREATE_NAME"));
				bulletinDto.setReadCnt(rs.getLong("read_cnt"));
				return bulletinDto;
			}
		});
		return bulletinDto.size() > 0 ? bulletinDto.get(0) : null;
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
	public int updateVisitNum(Long bulletinId){
		String sql = "UPDATE PTL_BULLETIN SET READ_CNT = READ_CNT+1 WHERE BULLETIN_ID = ?";
		return this.getJdbcTemplate().update(sql, bulletinId);
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
	public int updateReplyById(long bulletinId, int replyFlag){
		String sql = "UPDATE PTL_BULLETIN SET REPLY_FLAG = ? WHERE BULLETIN_ID = ?";
		return this.getJdbcTemplate().update(sql, replyFlag, bulletinId);
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
					+ "       DATE_FORMAT(T.RELEASE_TIME, '%m-%d') RELEASE_TIME "
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
							"       DATE_FORMAT(T.RELEASE_TIME, '%m-%d') RELEASE_TIME " + 
							" FROM PTL_BULLETIN t ,uic_user u ,uic_org a , uic_user_has_org b " + 
							" WHERE u.id = t.create_staff AND u.id = b.id AND  b.org_id= a.org_id  AND u.del_flag = 0 AND a.del_flag=0 AND T.STATE = 1 and b.type = 1 " + 
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
	public List<BulletinDto> queryNoticePublishedByPage(PageObj pageObj,Long province){
		String sql = "";
		if(province == null || province == 0){
			sql = "SELECT *"
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
					+ " WHERE T.create_staff=u.id and  u.del_flag = 0 and "
					+ " T.STATE=1 AND (T.LOSE_TIME > now() OR T.LOSE_TIME IS NULL)  ) T1 ";
		}else{
		sql = "SELECT * " +
						"  FROM (SELECT  u.user_name, " + 
						"  T.BULLETIN_ID, " + 
						"               T.TITLE, " + 
						"    T.STICK_UP, " + 
						"               DATE_FORMAT(T.CREATE_TIME, '%Y-%m-%d %H:%i:%S') CREATE_TIME, " + 
						"               DATE_FORMAT(T.RELEASE_TIME, '%Y-%m-%d %H:%i:%S') RELEASE_TIME, " + 
						"               DATE_FORMAT(T.LOSE_TIME, '%Y-%m-%d %H:%i:%S') LOSE_TIME, " + 
						"               T.READ_CNT, " + 
						"               (SELECT COUNT(*) " + 
						"                  FROM ptl_file T1 " + 
						"                 WHERE T1.GROUP_CODE = 'NOTICE' " + 
						"                   AND T1.RELATION_ID = T.BULLETIN_ID " + 
						"                   AND T1.IS_DEL = 0) FILE_NUM, " + 
						"        (SELECT COUNT(*) " + 
						"           FROM PTL_BULLETIN_COMMENT R " + 
						"            WHERE R.BULLETIN_ID = T.BULLETIN_ID) REPLY_NUM, " + 
						"         T.REPLY_FLAG " + 
						"          FROM ptl_bulletin T  , uic_user u  ,uic_org a, uic_user_has_org b " + 
						" WHERE T.create_staff=u.id AND  u.id = b.id AND b.org_id = a.org_id AND u.del_flag = 0 AND a.del_flag = 0 AND b.type = 1 AND " + 
						" a.org_id_seq LIKE CONCAT((SELECT o.org_id_seq FROM uic_org o WHERE o.org_id = "+province+"),'%')  AND " + 
						" T.STATE=1 AND (T.LOSE_TIME > NOW() OR T.LOSE_TIME IS NULL)  ) T1 ";
		}
		sql += " ORDER BY T1.STICK_UP DESC, T1.CREATE_TIME DESC";
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
				bulletinDto.setReleaseDate(rs.getString("RELEASE_TIME") == null ? ""
						: rs.getString("RELEASE_TIME"));
				bulletinDto.setLoseTime(rs.getString("lose_time") == null ? ""
						: rs.getString("lose_time"));
				bulletinDto.setReadCnt(rs.getInt("read_cnt"));
				bulletinDto.setReplyFlag(rs.getInt("reply_flag"));
				bulletinDto.setCreateName(rs.getString("user_name"));
				return bulletinDto;
			}
		}, pageObj);
	}
	
}
