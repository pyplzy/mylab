package usi.dbdp.portal.task.dto;

import java.io.Serializable;
/**
 * 业务层面的定时任务，如启动工单：表格sys_job信息封装
 * 
 */
public class SysJobDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer	jobId;				//任务标识
	private String	jobCd;				//任务编码
	private	String	jobName;			//任务名称
	private	String	content;				//说明
	private Integer	enAble;				//0停用，1启用
	private Integer	delFlag;				//0未删除，1已删除
	private	Integer	shardingId;		//用于cobar分库字段，填省份编码
	
	public Integer getJobId() {
		return jobId;
	}
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	public String getJobCd() {
		return jobCd;
	}
	public void setJobCd(String jobCd) {
		this.jobCd = jobCd;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getEnAble() {
		return enAble;
	}
	public void setEnAble(Integer enAble) {
		this.enAble = enAble;
	}
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	public Integer getShardingId() {
		return shardingId;
	}
	public void setShardingId(Integer shardingId) {
		this.shardingId = shardingId;
	}
	
	
}
