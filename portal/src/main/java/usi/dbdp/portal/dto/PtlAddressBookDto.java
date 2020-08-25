package usi.dbdp.portal.dto;
/**
 * 通讯录dto，只要部分字段，减少传输压力
 * @author lmwang
 * 创建时间：2015-5-8 下午1:37:47
 */
public class PtlAddressBookDto {
	private Integer addressBookId; // 通讯录主键
	private String name; // 姓名
	private String deparment; // 部门
	public Integer getAddressBookId() {
		return addressBookId;
	}
	public void setAddressBookId(Integer addressBookId) {
		this.addressBookId = addressBookId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeparment() {
		return deparment;
	}
	public void setDeparment(String deparment) {
		this.deparment = deparment;
	}

	
}
