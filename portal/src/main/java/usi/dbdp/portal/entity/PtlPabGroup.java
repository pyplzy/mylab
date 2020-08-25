package usi.dbdp.portal.entity;

/**
 * 个人常用联系人分组
 * @author lxci
 * 创建时间：2015年3月24日 下午13:20:00
 */
public class PtlPabGroup {
	private Integer groupId;
	private String groupName;   // 分组名称
	private String userId;      // 登录账号

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
