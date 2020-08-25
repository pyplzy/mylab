package usi.dbdp.portal.dto;

import java.util.List;

import usi.dbdp.portal.entity.PtlBulletin;
import usi.dbdp.portal.entity.PtlFile;

/**
 * 公告管理dto
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 上午9:17:16
 */

public class BulletinDto extends PtlBulletin{
	
	private int fileNum;		//附件数量
	
	private String offline; 	//下线标识("y"：下线，"n"：在线)
	
	private int replyNum;		//回复次数
	
	private String createName;   //发布人姓名
	
	private List<PtlFile> files;
	
	
	public List<PtlFile> getFiles() {
		return files;
	}

	public void setFiles(List<PtlFile> files) {
		this.files = files;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public String getOffline() {
		return offline;
	}

	public void setOffline(String offline) {
		this.offline = offline;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}
	
}
