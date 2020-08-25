package usi.dbdp.uic.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用注册信息实体
 * @author nie.zhengqian
 * 创建时间：2015年3月10日 下午4:44:30
 */

public class AppRegister implements Serializable{
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	
	/**应用code*/
	private String appCode;
	
	/**应用名称*/
	private String appName;
	
	/**应用图标路径（相对）*/
	private String appImgPath;
	
	/**入口URL*/
	private String appUrl;
	
	/**待办通知URL（HTTP+json接口形式）*/
	private String todoUrl;
	
	/**校验码*/
	private String appChecksum;
	
	/**状态（1：在用，0：停用*/
	private int state;
	
	/**备注*/
	private String appMmemo;
	
	/**注册时间*/
	private Date regTime;
	
	/**应用用户手册路径*/
	private String userGuidePath;
	
	/**应用类型(0:系统应用;1普通应用,默认 0)*/
	private int appType;
	
	/**显示顺序*/
	private int displayOrder;
	

	/**
	 * 获取状态（1：在用，0：停用）
	 * @return
	 */
	public int getState() {
		return state;
	}

	/**
	 * 设置状态（1：在用，0：停用）
	 * @param state 状态（1：在用，0：停用）
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 获取 应用code
	 * @return 应用code
	 */
	public String getAppCode() {
		return appCode;
	}
	
	/**
	 * 设置应用code
	 * @param appCode 应用code
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	/**
	 * 获取 应用名称
	 * @return 应用名称
	 */
	public String getAppName() {
		return appName;
	}
	
	/**
	 * 设置 应用名称
	 * @param appName 应用名称
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * 获取 入口URL
	 * @return 入口URL
	 */
	public String getAppUrl() {
		return appUrl;
	}
	
	/**
	 * 设置 入口URL
	 * @param appUrl 入口URL
	 */
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	
	/**
	 * 获取 校验码
	 * @return 校验码
	 */
	public String getAppChecksum() {
		return appChecksum;
	}
	
	/**
	 * 设置 校验码
	 * @param appChecksum 校验码
	 */
	public void setAppChecksum(String appChecksum) {
		this.appChecksum = appChecksum;
	}
	
	
	/**
	 * 获取 备注
	 * @return 备注
	 */
	public String getAppMmemo() {
		return appMmemo;
	}
	
	/**
	 * 设置 备注
	 * @param appMmemo 备注
	 */
	public void setAppMmemo(String appMmemo) {
		this.appMmemo = appMmemo;
	}
	
	/**
	 * 获取 注册时间
	 * @return 注册时间
	 */
	public Date getRegTime() {
		return regTime;
	}
	
	/**
	 * 设置 注册时间
	 * @param regTime 注册时间
	 */
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	
	/**
	 * 获取 应用图标路径（相对）
	 * @return 应用图标路径（相对）
	 */
	public String getAppImgPath() {
		return appImgPath;
	}
	
	/**
	 * 设置 应用图标路径（相对）
	 * @param appImgPath 应用图标路径（相对）
	 */
	public void setAppImgPath(String appImgPath) {
		this.appImgPath = appImgPath;
	}
	
	/**
	 * 获取 待办通知URL（HTTP+json接口形式）
	 * @return 待办通知URL（HTTP+json接口形式）
	 */
	public String getTodoUrl() {
		return todoUrl;
	}
	
	/**
	 * 设置 待办通知URL（HTTP+json接口形式）
	 * @param todoUrl 待办通知URL（HTTP+json接口形式）
	 */
	public void setTodoUrl(String todoUrl) {
		this.todoUrl = todoUrl;
	}
	
	/**
	 * 获取  应用用户手册路径
	 * @return 应用用户手册路径
	 */
	public String getUserGuidePath() {
		return userGuidePath;
	}
	
	/**
	 * 设置 应用用户手册路径
	 * @param userGuidePath 应用用户手册路径
	 */
	public void setUserGuidePath(String userGuidePath) {
		this.userGuidePath = userGuidePath;
	}
	
	/**
	 * 获取  应用类型(0:系统应用;1普通应用,默认 0)
	 * @return 应用类型(0:系统应用;1普通应用,默认 0)
	 */
	public int getAppType() {
		return appType;
	}
	
	/**
	 * 设置 应用类型(0:系统应用;1普通应用,默认 0)
	 * @param appType 应用类型(0:系统应用;1普通应用,默认 0)
	 */
	public void setAppType(int appType) {
		this.appType = appType;
	}
	
	/**
	 * 获取显示顺序
	 * @return 显示顺序
	 */
	public int getDisplayOrder() {
		return displayOrder;
	}
	
	/**
	 * 设置显示顺序
	 * @param displayOrder 显示顺序
	 */
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	

}
