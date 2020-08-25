package usi.dbdp.portal.dto;

import java.util.List;

/**
 * 查询通讯录和其联系人信息
 * 
 */

public class PtlPabGroupAndPersonInfo {
	private Integer groupId; // 常用联系人分组主键
	private String groupName; // 常用联系人分组名称
	private Integer addressBookId;
	private List<PtlAddressBookDto> ptladdressbook; // 通讯录联系人集合

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

	public Integer getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(Integer addressBookId) {
		this.addressBookId = addressBookId;
	}

	public List<PtlAddressBookDto> getPtladdressbook() {
		return ptladdressbook;
	}

	public void setPtladdressbook(List<PtlAddressBookDto> ptladdressbook) {
		this.ptladdressbook = ptladdressbook;
	}

}
