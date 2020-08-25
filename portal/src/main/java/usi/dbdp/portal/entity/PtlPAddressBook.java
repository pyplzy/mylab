package usi.dbdp.portal.entity;

/**
 * 个人常用联系人
 * @author lxci
 * 创建时间：2015年3月24日 下午13:40:00
 */
public class PtlPAddressBook {
	private Integer padId;          //主键
	private String userId;          //登录账号
	private Integer addressBookId;  //通讯录主键
	private Integer groupId;        //分组主键

	public Integer getPadId() {
		return padId;
	}

	public void setPadId(Integer padId) {
		this.padId = padId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(Integer addressBookId) {
		this.addressBookId = addressBookId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

}
