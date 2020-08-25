package usi.dbdp.portal.entity;

import java.util.Date;

/**
 * 附件表
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 上午9:59:58
 */

public class PtlFile {

	private long fileId; // 附件主键

	private String fileName; // 文件名

	private String absolutepath; // 文件路径

	private Long fileSize; // 文件大小

	private String fileType; // 文件类型

	private long staffId; // 上传人

	private Date fileTime; // 上传时间

	private String groupCode; // 所属分组

	private long relationId; // 关联主键

	private long downloadTimes; // 下载次数

	private int isDel; // 是否删除

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAbsolutepath() {
		return absolutepath;
	}

	public void setAbsolutepath(String absolutepath) {
		this.absolutepath = absolutepath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public Date getFileTime() {
		return fileTime;
	}

	public void setFileTime(Date fileTime) {
		this.fileTime = fileTime;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public long getRelationId() {
		return relationId;
	}

	public void setRelationId(long relationId) {
		this.relationId = relationId;
	}

	public long getDownloadTimes() {
		return downloadTimes;
	}

	public void setDownloadTimes(long downloadTimes) {
		this.downloadTimes = downloadTimes;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

}
