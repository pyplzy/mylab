package usi.dbdp.portal.dto;

/**
 * 联系我们
 * @author zhang.dechang
 * 2016年12月29日 上午10:40:57
 */
public class ContactUsInfo {
	private Long id;		//主键
	private String tel;		//维护电话
	private String mail;	//维护邮箱
	private String coInfo;	//公司信息
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getCoInfo() {
		return coInfo;
	}
	public void setCoInfo(String coInfo) {
		this.coInfo = coInfo;
	}
	
}
