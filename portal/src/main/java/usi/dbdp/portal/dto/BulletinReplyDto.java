package usi.dbdp.portal.dto;

import java.util.Date;

/**
 * 公告回复dto
 * @author nie.zhengqian
 * 创建时间：2015年3月26日 上午11:19:30
 */

public class BulletinReplyDto {
	
	private long commentId;		//公告回复主键
	
	private long bulletinId;	//公告主键
	
	private long staffId;		//回复人ID
	
	private String userName;	//回复人姓名

	private String orgName;		//机构名
	
	private String replyContent;	//回复内容
	
	private String replyTime;		//发表时间

	private Date replyDate;			//发表时间(转化用)
	
	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public long getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(long bulletinId) {
		this.bulletinId = bulletinId;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

	
}
