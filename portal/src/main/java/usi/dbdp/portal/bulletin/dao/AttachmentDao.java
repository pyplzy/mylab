package usi.dbdp.portal.bulletin.dao;

import java.util.List;

import usi.dbdp.portal.entity.PtlFile;
import usi.dbdp.portal.entity.PtlFileOpLog;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午10:57:14
 * 说明
 */
public interface AttachmentDao {

	/**
	 * 保存附件信息
	 * @param ptlFile 待保存的附件信息对象
	 * @return 附件主键
	 */
	public long saveAttachment(PtlFile ptlFile);

	/**
	 * 根据主键查询附件的路径
	 * @param fileId 附件主键
	 * @return 文件的路径
	 */
	public String getAbsolutepathById(long fileId);

	/**
	 * 根据主键查询附件的路径，文件名
	 * @param fileId 附件主键
	 * @return 
	 */
	public PtlFile getPtlFileById(long fileId);

	/**
	 * 根据主键更新附件删除标志位为删除状态1
	 * @param fileId 附件主键
	 * @return 更新记录个数
	 */
	public int delFileByID(long fileId);

	/**
	 * 保存操作附件
	 * @param PtlFileOpLog 操作附件对象实体
	 * @return 保存记录个数
	 */
	public int savePtlFileOpLog(PtlFileOpLog ptlFileOpLog);

	/**
	 * 根据所属分组和关联主键取附件列表
	 * @param groupCode 附件所属分组
	 * @param relationId 关联主键（业务表的）
	 * @return 附件列表
	 */
	public List<PtlFile> getUploadedFiles(String groupCode,
			long relationId);

	/**
	 * 根据主键更新下载次数
	 * @param fileId 附件主键
	 * @return 更新的记录个数
	 */
	public int incDownloadTimesById(long fileId);

	/**
	 * 根据所属分组和关联主键数组取附件列表
	 * @param groupCode 附件所属分组
	 * @param relationIds 关联主键（业务表的）
	 * @return 附件列表
	 */
	public List<PtlFile> queryUploadedFiles(String groupCode,
			long[] relationIds);

	/**
	 * 删除附件
	 * @param fileIds 附件主键
	 * @return 更新记录个数
	 */
	public int delFileByIds(long[] fileIds);

	/**
	 * 修改附件操作日志表
	 * @param ptlFileOpLog
	 */
	public void batchSavePtlFileOpLog(PtlFileOpLog[] ptlFileOpLog);

}