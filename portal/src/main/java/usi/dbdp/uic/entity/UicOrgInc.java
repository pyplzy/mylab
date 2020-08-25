package usi.dbdp.uic.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: UicOrgInc 
 * @Description: 机构增量表实体
 * @author johnDong
 * @date 2017年2月9日
 */
public class UicOrgInc implements Serializable{
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	/**机构ID*/
	private long orgId;
	
	/**机构名称*/
	private String orgName;
	
	/**父机构ID*/
	private long pOrgId;
	
	/**机构级别（0：未知，1：集团公司，2：省公司，3：市公司，4：县公司，5：班组）*/
	private int orgGrade;
	
	/**显示顺序*/
	private int displayOrder;
	
	/**删除标识（1：删除，0：未删除）*/
	private int delFlag;
	
	/**备注*/
	private String orgMemo;
	
	/**机构ID序列*/
	private String orgIdSeq;
	
	/**机构名称序列*/
	private String orgNameSeq;
	
	/**是否叶子节点（1：是，0：否）*/
	private int isLeaf;
	
	/**行政级别（1：国级，2：部级，3：厅局级，4：处级，5：科级）*/
	private Integer administrativeGrade;
	
	/**父亲节点（true：是，false：否）*/
	private String isParent;
	
	/**机构编码*/
	private String orgCode;
	
	/**机构编码序列*/
	private String orgCodeSeq;
	/**增量类型 (A、D、U)*/
	private String incType;
	/**操作当前时间*/
	private Date occurTime;
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public long getpOrgId() {
		return pOrgId;
	}
	public void setpOrgId(long pOrgId) {
		this.pOrgId = pOrgId;
	}
	public int getOrgGrade() {
		return orgGrade;
	}
	public void setOrgGrade(int orgGrade) {
		this.orgGrade = orgGrade;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	public String getOrgMemo() {
		return orgMemo;
	}
	public void setOrgMemo(String orgMemo) {
		this.orgMemo = orgMemo;
	}
	public String getOrgIdSeq() {
		return orgIdSeq;
	}
	public void setOrgIdSeq(String orgIdSeq) {
		this.orgIdSeq = orgIdSeq;
	}
	public String getOrgNameSeq() {
		return orgNameSeq;
	}
	public void setOrgNameSeq(String orgNameSeq) {
		this.orgNameSeq = orgNameSeq;
	}
	public int getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Integer getAdministrativeGrade() {
		return administrativeGrade;
	}
	public void setAdministrativeGrade(Integer administrativeGrade) {
		this.administrativeGrade = administrativeGrade;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgCodeSeq() {
		return orgCodeSeq;
	}
	public void setOrgCodeSeq(String orgCodeSeq) {
		this.orgCodeSeq = orgCodeSeq;
	}
	public String getIncType() {
		return incType;
	}
	public void setIncType(String incType) {
		this.incType = incType;
	}
	public Date getOccurTime() {
		return occurTime;
	}
	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}
}
