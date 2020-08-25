package usi.dbdp.portal.entity;

import java.util.Date;

/**
 * 公告实体类
 * @author nie.zhengqian
 * 创建时间：2015年3月24日 下午12:01:23
 */

public class PtlBulletin {
	
	private long bulletinId;     //公告ID
	
	private String title;       //公告标题
	
	private  String loseTime;    	//失效时间
	
	private  Date loseDate;	//失效时间(String,转化用)
	
	private int stickUp;     	//置顶标识0：非置顶（默认），1：置顶
	
	private String content;  	//公告内容
	
	private long readCnt;		//阅读次数
	
	private int state;			//公告状态0：草稿，1：发布，2：下架，3：删除
	
	private long createStaff;	//创建人
	
	private Date createTime;	//创建时间
	
	private String createDate;	//创建时间(转化用)
	
	private Date releaseTime;	//发布时间
	
	private String releaseDate;	//发布时间(转化用)
	
	private int replyFlag;		//回复启用标识（0：关闭回复1：开启回复）

	public long getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(long bulletinId) {
		this.bulletinId = bulletinId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(String loseTime) {
		this.loseTime = loseTime;
	}

	public Date getLoseDate() {
		return loseDate;
	}

	public void setLoseDate(Date loseDate) {
		this.loseDate = loseDate;
	}

	public int getStickUp() {
		return stickUp;
	}

	public void setStickUp(int stickUp) {
		this.stickUp = stickUp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(long readCnt) {
		this.readCnt = readCnt;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getCreateStaff() {
		return createStaff;
	}

	public void setCreateStaff(long createStaff) {
		this.createStaff = createStaff;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getReplyFlag() {
		return replyFlag;
	}

	public void setReplyFlag(int replyFlag) {
		this.replyFlag = replyFlag;
	}
	
	
	

	
	
	
	
}
