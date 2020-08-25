package usi.dbdp.portal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 信息变更日志
 * @author ma.guangming
 *
 */
public class ChangeLog  implements Serializable  {
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	private long changeId;//变更ID
	private String userId;//操作人
	private String optObj;//操作对象，菜单表，角色表，结构表
	private String optType;//操作类型update、delete、insert
	private String optIp;//操作人IP
	private String optContent;//操作内容记录
	private Date optTime;//操作时间
	/**
	 * 获取变更日志id
	 * @return  变更日志id
	 */
	public long getChangeId() {
		return changeId;
	}
	/**
	 * 设置变更日志id
	 * @param changeId
	 */
	public void setChangeId(long changeId) {
		this.changeId = changeId;
	}
	/**
	 * 获取用户id 
	 * @return 用户id
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置用户id
	 * @param 用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getOptObj() {
		return optObj;
	}
	public void setOptObj(String optObj) {
		this.optObj = optObj;
	}
	/**
	 * 获取操作类型
	 * @return 操作类型(update、delete、insert)
	 */
	public String getOptType() {
		return optType;
	}
	/**
	 * 设置操作类型
	 * @param optType 操作类型(update、delete、insert)
	 */
	public void setOptType(String optType) {
		this.optType = optType;
	}
	/**
	 * 获取操作人ip
	 * @return 操作人ip 
	 */
	public String getOptIp() {
		return optIp;
	}
	/**
	 * 设置操作人ip
	 * @param optIp 操作人ip
	 */
	public void setOptIp(String optIp) {
		this.optIp = optIp;
	}
	/**
	 * 获取操作内容记录
	 * @return 操作内容记录
	 */
	public String getOptContent() {
		return optContent;
	}
	/**
	 * 设置操作内容记录 
	 * @param optContent 操作内容记录
	 */
	public void setOptContent(String optContent) {
		this.optContent = optContent;
	}
	/**
	 * 获取操作时间
	 * @return 操作时间
	 */
	public Date getOptTime() {
		return optTime;
	}
	/**
	 * 设置操作时间
	 * @param optTime 操作时间
	 */
	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}
}
