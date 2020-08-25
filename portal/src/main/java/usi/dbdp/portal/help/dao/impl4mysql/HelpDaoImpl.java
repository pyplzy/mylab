package usi.dbdp.portal.help.dao.impl4mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.dto.ContactUsInfo;
import usi.dbdp.portal.dto.FeedbackDto;
import usi.dbdp.portal.entity.PtlFile;
import usi.dbdp.portal.help.dao.HelpDao;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.dto.PageObj;

@MysqlDb
@Repository
public class HelpDaoImpl extends JdbcDaoSupport4mysql implements HelpDao{
	
	@Override
	public ContactUsInfo getContactUsInfo() {
		String sql = "select * from ptl_contact_us";
		List<ContactUsInfo> list = this.getJdbcTemplate().query(sql, new RowMapper<ContactUsInfo>() {
					@Override
					public ContactUsInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
						ContactUsInfo item = new ContactUsInfo();
						item.setId(rs.getLong("id"));
						item.setCoInfo(rs.getString("co_info"));
						item.setMail(rs.getString("mail"));
						item.setTel(rs.getString("tel"));
						return item;
					}
				});
		return list.size()>0 ? list.get(0) : null;
	}
	
	@Override
	public Long saveFeedback(final FeedbackDto feedbackDto) {
		final String sql = "insert into ptl_feedback(feedback_title,feedback_info,user_id,user_name,org_id,org_name,contact_tel) values(?,?,?,?,?,?,?)";
		
		KeyHolder holder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, new String[]{"feedback_id"});
				ps.setString(1,feedbackDto.getFeedbackTitle());
				ps.setString(2,feedbackDto.getFeedbackInfo());
				ps.setString(3,feedbackDto.getUserId());
				ps.setString(4,feedbackDto.getUserName());
				ps.setLong(5,feedbackDto.getOrgId());
				ps.setString(6,feedbackDto.getOrgName());
				ps.setString(7,feedbackDto.getContactTel());
				return ps;
			}
		}, holder);
		Long feedbackId = holder.getKey().longValue();
		return feedbackId;
	}
	
	@Override
	public List<FeedbackDto> getFeedbackList(FeedbackDto feedbackDto, PageObj pageObj) {
		String sql = "select * from ptl_feedback t where 1=1 ";
		List<Object> param = new ArrayList<Object>();
		if(StringUtils.hasText(feedbackDto.getFeedbackTitle())){
			sql += "and t.feedback_title like  CONCAT('%',?,'%')  ";
			param.add(feedbackDto.getFeedbackTitle());
		}
		if(StringUtils.hasText(feedbackDto.getUserName())){
			sql += "and t.user_name like CONCAT('%',?,'%')  ";
			param.add(feedbackDto.getUserName());
		}
		if(feedbackDto.getStartTime()!=null){
			sql += "and t.feedback_time >= ?  ";
			param.add(feedbackDto.getStartTime());
		}
		if(feedbackDto.getEndTime()!=null){
			sql += "and t.feedback_time <= ?  ";
			param.add(feedbackDto.getEndTime());
		}
		sql += "order by t.feedback_time desc";
		return this.queryByPage(sql,param.toArray(),new RowMapper<FeedbackDto>() {
					@Override
					public FeedbackDto mapRow(ResultSet rs, int rowNum) throws SQLException {
						FeedbackDto item = new FeedbackDto();
						item.setFeedbackId(rs.getLong("feedback_id"));
						item.setFeedbackTitle(rs.getString("feedback_title"));
						item.setFeedbackInfo(rs.getString("feedback_info"));
						item.setUserName(rs.getString("user_name"));
						item.setOrgName(rs.getString("org_name"));
						item.setContactTel(rs.getString("contact_tel"));
						item.setFeedbackTime(rs.getTimestamp("feedback_time"));
						item.setState(rs.getInt("state"));
//						item.setQuestionType(rs.getInt("question_type"));
//						item.setReplyContent(rs.getString("reply_content"));
//						item.setReplyTime(rs.getTimestamp("reply_time"));
						return item;
					}
				}, pageObj);
	}
	
	@Override
	public void updateFeedbackFile(final Long feedbackId, final Long[] fileIds) {
		String sql = "update ptl_file t set t.relation_id=? where t.file_id=?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, feedbackId);
				ps.setLong(2, fileIds[i]);
			}
			
			@Override
			public int getBatchSize() {
				return fileIds.length;
			}
		});
	}
	
	@Override
	public FeedbackDto getFeedbackDetail(Long feedbackId) {
		String sql = "select * from ptl_feedback t where feedback_id=? ";
		List<FeedbackDto> list = this.getJdbcTemplate().query(sql,new RowMapper<FeedbackDto>() {
					@Override
					public FeedbackDto mapRow(ResultSet rs, int rowNum) throws SQLException {
						FeedbackDto item = new FeedbackDto();
						item.setFeedbackId(rs.getLong("feedback_id"));
						item.setFeedbackTitle(rs.getString("feedback_title"));
						item.setFeedbackInfo(rs.getString("feedback_info"));
						item.setQuestionType(rs.getString("question_type") != null ? rs.getInt("question_type") : null);
						item.setReplyContent(rs.getString("reply_content"));
						item.setReplyTime(rs.getTimestamp("reply_time"));
						item.setState(rs.getInt("state"));
						return item;
					}
				}, feedbackId);
		return list.size()>0 ? list.get(0) : null;
	}
	
	@Override
	public List<PtlFile> getFeedbackFiles(String groupCode, Long feedbackId) {
			String sql = "select file_id,file_name,absolutepath from ptl_file "
					+ "where group_code=? and relation_id=? and is_del=0 order by file_time";
			return this.getJdbcTemplate().query(sql, new Object[]{groupCode,feedbackId},new RowMapper<PtlFile>() { 
					public PtlFile mapRow(ResultSet rs, int rowNum) throws SQLException {
						PtlFile file = new PtlFile();
						file.setFileId(rs.getLong("file_id"));
						file.setFileName(rs.getString("file_name"));
						file.setAbsolutepath(rs.getString("absolutepath"));
						return file;
					}
				});
	}
	
	@Override
	public void saveHandleInfo(FeedbackDto feedbackDto) {
		String sql = "update ptl_feedback t set t.question_type=?, t.reply_content=?, t.reply_time=?, t.state=1 where t.feedback_id=? and t.state=0";
		this.getJdbcTemplate().update(sql, feedbackDto.getQuestionType(),feedbackDto.getReplyContent(),new Date(),feedbackDto.getFeedbackId());
	}
	
}
