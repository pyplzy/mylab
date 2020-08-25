package usi.dbdp.portal.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NewsDto {
	
	private Integer replyNum;		//回复次数
	
	private String coverPhoto;		//封面图片url
	
	private String digest;			//新闻摘要
	
	private Long newsId;     		//ID
	
	private String title;       	//标题
	
	private String content;  		//内容
	
	private Integer readCnt;		//阅读次数
	
	private Integer state;			//状态0：草稿，1：发布，2：下架，3：删除
	
	private String userName;   		//创建人姓名
	
	private String userId;			//创建人工号
	
	private Long orgId;				////创建人所在机构
	private String orgName;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;		//创建时间
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date releaseTime;		//发布时间
	
	private Integer replyFlag;		//回复启用标识（0：关闭回复1：开启回复）
	
	private String startTime;//查询条件中的开始时间
	
	private String endTime;//查询条件中的结束时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date stTime;//date类型的开始时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date edTime;//date类型的结束时间
	

	public Date getStTime() {
		return stTime;
	}

	public void setStTime(Date stTime) {
		this.stTime = stTime;
	}

	public Date getEdTime() {
		return edTime;
	}

	public void setEdTime(Date edTime) {
		this.edTime = edTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(Integer replyNum) {
		this.replyNum = replyNum;
	}

	public String getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(Integer readCnt) {
		this.readCnt = readCnt;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Integer getReplyFlag() {
		return replyFlag;
	}

	public void setReplyFlag(Integer replyFlag) {
		this.replyFlag = replyFlag;
	}

}
