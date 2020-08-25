package usi.dbdp.portal.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 信息变更日志dto
 * @author ma.guangming
 *
 */
public class ChangeLogInfo  implements Serializable {
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	private long changeId;//变更ID
	private String userId;//操作人
	private String optObj;//操作对象
	private String optType;//操作类型update、delete、insert
	private String optIp;//操作人IP
	private String optContent;//操作内容记录
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date optTime;//操作时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date optBeginTime;//操作开始时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date optEndTime;//操作结束时间
	//用户表
	private String userName;//用户姓名
	//机构表
	private String orgName;//机构名称
	
	public long getChangeId() {
		return changeId;
	}
	public void setChangeId(long changeId) {
		this.changeId = changeId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOptObj() {
		return optObj;
	}
	public void setOptObj(String optObj) {
		this.optObj = optObj;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getOptIp() {
		return optIp;
	}
	public void setOptIp(String optIp) {
		this.optIp = optIp;
	}
	public String getOptContent() {
		return optContent;
	}
	public void setOptContent(String optContent) {
		this.optContent = optContent;
	}
	public Date getOptTime() {
		return optTime;
	}
	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}	
	public Date getOptBeginTime() {
		return optBeginTime;
	}
	public void setOptBeginTime(Date optBeginTime) {
		this.optBeginTime = optBeginTime;
	}
	public Date getOptEndTime() {
		return optEndTime;
	}
	public void setOptEndTime(Date optEndTime) {
		this.optEndTime = optEndTime;
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
}
