package usi.dbdp.uic.entity;

import java.io.Serializable;

/**
 * 机构实体
 * @author lmwang
 * 创建时间：2015-2-5 下午5:08:19
 */
public class Org implements Serializable {

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

	
	
	/**
	 * 获取父亲节点（true：是，false：否）
	 * @return 父亲节点（true：是，false：否）
	 */
	public String getIsParent() {
		return isParent;
	}
	/**
	 * 设置父亲节点（true：是，false：否）
	 * @param isParent 父亲节点（true：是，false：否）
	 */
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	/**
	 * 获取机构id
	 * @return 机构id
	 */
	public long getOrgId() {
		return orgId;
	}

	/**
	 * 设置机构id
	 * @param orgId 机构id
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	/**
	 * 获取机构名称
	 * @return 机构名称
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * 设置机构名称
	 * @param orgName 机构名称
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * 获取父机构id
	 * @return 父机构id
	 */
	public long getpOrgId() {
		return pOrgId;
	}

	/**
	 * 设置父机构id
	 * @param pOrgId 父机构id
	 */
	public void setpOrgId(long pOrgId) {
		this.pOrgId = pOrgId;
	}

	/**
	 * 获取机构级别（0：未知，1：集团公司，2：省公司，3：市公司，4：县公司，5：班组）
	 * @return 机构级别（0：未知，1：集团公司，2：省公司，3：市公司，4：县公司，5：班组）
	 */
	public int getOrgGrade() {
		return orgGrade;
	}

	/**
	 * 设置机构级别（0：未知，1：集团公司，2：省公司，3：市公司，4：县公司，5：班组）
	 * @param orgGrade 机构级别（0：未知，1：集团公司，2：省公司，3：市公司，4：县公司，5：班组）
	 */
	public void setOrgGrade(int orgGrade) {
		this.orgGrade = orgGrade;
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

	/**
	 * 删除标识（1：删除，0：未删除）
	 * @return 机构状态（1：删除，0：未删除）
	 */
	public int getDelFlag() {
		return delFlag;
	}

	/**
	 *	删除标识（1：删除，0：未删除）
	 * @return 机构状态（1：删除，0：未删除）
	 */
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 获取备注
	 * @return 备注
	 */
	public String getOrgMemo() {
		return orgMemo;
	}

	/**
	 * 设置备注
	 * @param orgMemo 备注
	 */
	public void setOrgMemo(String orgMemo) {
		this.orgMemo = orgMemo;
	}

	/**
	 * 获取机构id序列
	 * @return 机构id序列
	 */
	public String getOrgIdSeq() {
		return orgIdSeq;
	}

	/**
	 * 设置机构id序列
	 * @param orgIdSeq 机构id序列
	 */
	public void setOrgIdSeq(String orgIdSeq) {
		this.orgIdSeq = orgIdSeq;
	}
	
	/**
	 * 获取机构名称序列
	 * @return 机构名称序列
	 */
	public String getOrgNameSeq() {
		return orgNameSeq;
	}
	
	/**
	 * 设置机构名称序列
	 * @param orgNameSeq 机构名称序列
	 */
	public void setOrgNameSeq(String orgNameSeq) {
		this.orgNameSeq = orgNameSeq;
	}

	/**
	 * 获取是否叶子节点（1：是，0：否）
	 * @return 是否叶子节点（1：是，0：否）
	 */
	public int getIsLeaf() {
		return isLeaf;
	}

	/**
	 * 设置机构是否叶子节点（1：是，0：否）
	 * @param isLeaf 是否叶子节点（1：是，0：否）
	 */
	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	/**
	 * 获取行政级别
	 * @return 行政级别（1：国级，2：部级，3：厅局级，4：处级，5：科级）
	 */
	public Integer getAdministrativeGrade() {
		return administrativeGrade;
	}
	/**
	 * 设置行政级别
	 * @param 行政级别（1：国级，2：部级，3：厅局级，4：处级，5：科级）
	 */
	public void setAdministrativeGrade(Integer administrativeGrade) {
		this.administrativeGrade = administrativeGrade;
	}
	
	/**
	 * 获取 机构编码
	 * @return 机构编码
	 */
	public String getOrgCode() {
		return orgCode;
	}
	/**
	 * 设置 机构编码
	 * @param orgCode 机构编码
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	/**
	 * 获取 机构编码序列
	 * @return 机构编码序列
	 */
	public String getOrgCodeSeq() {
		return orgCodeSeq;
	}
	/**
	 * 设置 机构编码序列
	 * @param orgCodeSeq 机构编码序列
	 */
	public void setOrgCodeSeq(String orgCodeSeq) {
		this.orgCodeSeq = orgCodeSeq;
	}
	
}
