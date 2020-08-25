package usi.dbdp.portal.bulletin.dao.impl4mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.bulletin.dao.AttachmentDao;
import usi.dbdp.portal.entity.PtlFile;
import usi.dbdp.portal.entity.PtlFileOpLog;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
/**
 * 附件操作Dao
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 下午3:44:39
 */
@MysqlDb
@Repository
public class AttachmentDaoImpl extends JdbcDaoSupport4mysql implements AttachmentDao{
	
	/**
	 * 保存附件信息
	 * @param ptlFile 待保存的附件信息对象
	 * @return 附件主键
	 */
	public long saveAttachment(final PtlFile ptlFile){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {

				String sql = "INSERT INTO ptl_file(FILE_NAME,ABSOLUTEPATH,FILE_SIZE,FILE_TYPE,"
						+ "STAFF_ID,FILE_TIME,GROUP_CODE,RELATION_ID,DOWNLOAD_TIMES,IS_DEL)"
						+ " VALUES(?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql,
						new String[] { "FILE_ID" });
				ps.setString(1, ptlFile.getFileName());
				ps.setString(2, ptlFile.getAbsolutepath());
				ps.setLong(3, ptlFile.getFileSize());
				ps.setString(4, ptlFile.getFileType());
				ps.setLong(5, ptlFile.getStaffId());
				ps.setTimestamp(6, new java.sql.Timestamp(ptlFile.getFileTime()
						.getTime()));
				ps.setString(7, ptlFile.getGroupCode());
				ps.setLong(8, ptlFile.getRelationId());
				ps.setLong(9, ptlFile.getDownloadTimes());
				ps.setInt(10, ptlFile.getIsDel());
				return ps;
			}

		}, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * 根据主键查询附件的路径
	 * @param fileId 附件主键
	 * @return 文件的路径
	 */
	public String getAbsolutepathById(long fileId){
		String sql = "SELECT ABSOLUTEPATH FROM ptl_file WHERE FILE_ID=?";
		String tmpPath = null;
		try {
			tmpPath = this.getJdbcTemplate().queryForObject(sql, String.class,
					new Object[] { fileId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmpPath;
	}
	
	/**
	 * 根据主键查询附件的路径，文件名
	 * @param fileId 附件主键
	 * @return 
	 */
	public PtlFile getPtlFileById(long fileId){
		String sql = "SELECT FILE_NAME,ABSOLUTEPATH FROM PTL_FILE WHERE FILE_ID=?";
		List<PtlFile> listFile = this.getJdbcTemplate().query(sql,
				new Object[] { fileId }, new RowMapper<PtlFile>() {
					public PtlFile mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						PtlFile file = new PtlFile();
						file.setFileName(rs.getString("FILE_NAME"));
						file.setAbsolutepath(rs.getString("ABSOLUTEPATH"));
						return file;
					}
				});
		return listFile.size() == 0 ? null : listFile.get(0);
	}
	
	/**
	 * 根据主键更新附件删除标志位为删除状态1
	 * @param fileId 附件主键
	 * @return 更新记录个数
	 */
	public int delFileByID(long fileId){
		String sql = "UPDATE PTL_FILE SET IS_DEL=1 WHERE FILE_ID=?";
		return this.getJdbcTemplate().update(sql, new Object[] { fileId });
		
	}
	
	/**
	 * 保存操作附件
	 * @param PtlFileOpLog 操作附件对象实体
	 * @return 保存记录个数
	 */
	public int savePtlFileOpLog(PtlFileOpLog ptlFileOpLog){
		String sql = "INSERT INTO PTL_FILE_OP_LOG(FILE_ID,"
				+ "STAFF_ID,OPERATOR_NAME,ORG_ID,ORG_NAME,"
				+ "OP_TIME,OP_TYPE) "
				+ "VALUES(?,?,?,?,?,?,?)";
		return this.getJdbcTemplate().update(sql,new Object[] {
				ptlFileOpLog.getFileId(),
				ptlFileOpLog.getStaffId(),
				ptlFileOpLog.getOperatorName(),
				ptlFileOpLog.getOrgId(),
				ptlFileOpLog.getOrgName(),
				ptlFileOpLog.getOpTime(),
				ptlFileOpLog.getOpType()
		});
	}
	
	/**
	 * 根据所属分组和关联主键取附件列表
	 * @param groupCode 附件所属分组
	 * @param relationId 关联主键（业务表的）
	 * @return 附件列表
	 */
	public List<PtlFile> getUploadedFiles(String groupCode,long relationId){
		String sql = "SELECT FILE_ID,FILE_NAME,ROUND(FILE_SIZE/1024,2) FILE_SIZE,"
				+ "FILE_TYPE,ABSOLUTEPATH,DOWNLOAD_TIMES "
				+ "FROM PTL_FILE "
				+ "WHERE GROUP_CODE=? AND RELATION_ID=? AND IS_DEL=0 ORDER BY FILE_TIME";
		return this.getJdbcTemplate().query(sql, new Object[]{groupCode,relationId},new RowMapper<PtlFile>() { 
				public PtlFile mapRow(ResultSet rs, int rowNum) throws SQLException {
					PtlFile file = new PtlFile();
					file.setFileId(rs.getLong("file_id"));
					file.setFileName(rs.getString("file_name"));
					file.setFileSize(rs.getLong("file_size"));
					file.setFileType(rs.getString("file_type"));
					file.setAbsolutepath(rs.getString("absolutepath"));
					file.setDownloadTimes(rs.getLong("download_times"));
					return file;
				}
			} );
		
	}
	
	/**
	 * 根据主键更新下载次数
	 * @param fileId 附件主键
	 * @return 更新的记录个数
	 */
	public int incDownloadTimesById(long fileId){
		String sql = "UPDATE PTL_FILE SET DOWNLOAD_TIMES=DOWNLOAD_TIMES+1 WHERE FILE_ID=?";
		return this.getJdbcTemplate().update(sql, new Object[]{fileId});
		
	}
	
	/**
	 * 根据所属分组和关联主键数组取附件列表
	 * @param groupCode 附件所属分组
	 * @param relationIds 关联主键（业务表的）
	 * @return 附件列表
	 */
	public List<PtlFile> queryUploadedFiles(String groupCode,long[] relationIds){
		String sql = "SELECT FILE_ID, ABSOLUTEPATH "
				+ "FROM PTL_FILE "
				+ "WHERE GROUP_CODE = ? AND RELATION_ID IN " 
				+ Arrays.toString(relationIds).replace("[", "(").replace("]", ")") + " AND IS_DEL = 0";
		return this.getJdbcTemplate().query(sql, new RowMapper<PtlFile>() { 
				public PtlFile mapRow(ResultSet rs, int rowNum) throws SQLException {
					PtlFile file = new PtlFile();
					file.setFileId(rs.getLong("FILE_ID"));
					file.setAbsolutepath(rs.getString("ABSOLUTEPATH"));
					return file;
				}
			}, groupCode);
		
	}
	
	/**
	 * 删除附件
	 * @param fileIds 附件主键
	 * @return 更新记录个数
	 */
	public int delFileByIds(long[] fileIds){
		String sql = "UPDATE PTL_FILE SET IS_DEL = 1 WHERE FILE_ID IN" 
				+  Arrays.toString(fileIds).replace("[", "(").replace("]", ")");
		return this.getJdbcTemplate().update(sql);
		
	}
	/**
	 * 修改附件操作日志表
	 * @param ptlFileOpLog
	 */
	public void batchSavePtlFileOpLog(final PtlFileOpLog[] ptlFileOpLog){
		String sql = "INSERT INTO PTL_FILE_OP_LOG(FILE_ID,STAFF_ID,OPERATOR_NAME,ORG_ID,ORG_NAME,OP_TIME,OP_TYPE) "
				+ "VALUES(?,?,?,?,?,?,?)";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PtlFileOpLog sysFileOpLog = ptlFileOpLog[i];
				ps.setLong(1, sysFileOpLog.getFileId());
				ps.setLong(2, sysFileOpLog.getStaffId());
				ps.setString(3, sysFileOpLog.getOperatorName());
				ps.setLong(4, sysFileOpLog.getOrgId());
				ps.setString(5, sysFileOpLog.getOrgName());
				ps.setTimestamp(6, new java.sql.Timestamp(sysFileOpLog.getOpTime().getTime()));
				ps.setInt(7, sysFileOpLog.getOpType());
			}
			
			public int getBatchSize() {
				return ptlFileOpLog.length;
			}
		});
	}
	
}
