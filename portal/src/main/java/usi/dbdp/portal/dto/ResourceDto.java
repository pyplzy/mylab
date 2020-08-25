package usi.dbdp.portal.dto;
/**
 * @author zhqnie
 * @version 2016年12月29日 上午10:15:26
 * 说明
 */
public class ResourceDto {

	private long resourceId;  //资源主键
	private String appCode;		//应用code
	private String resourceCode;	//资源编码
	private String resourceName;	//资源名称
	private String resourceDesc;	//资源描述
	private int delFlag;	//删除标识（0：未删除，1：已删除）
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String getResourceCode() {
		return resourceCode;
	}
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceDesc() {
		return resourceDesc;
	}
	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	
}
