package usi.dbdp.portal.task.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class SysTimerDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer			timerId;				//定时器ID
	private	String			timerName;		//任务名称
	private Integer			jobId;				//任务Id
	private Integer			nodeId;				//任务标识
	private String			node;				//ip+:+port
	private	String			className;		//执行类全名
	private	String			excuteMethod;	//执行方法名
	private Integer			timerStatus;		//任务状态 0未启动 1启动
	private	String			cronExpression;	//任务运行时间表达式
	private	String			timerDesc;			//任务描述
	private	String			busiPara;			//业务参数
	private Timestamp		createTime;		//创建时间 数据库默认为current_times
	private Integer			enAble;				//1启用，0停用
	private Integer			isEnAble;			//判断当前定时器是否处于启用状态：定时器和任务同时启用为启用状态
	private Integer			delFlag;				//0未删除，1已删除
	private Integer			shardingId;		//用于cobar分库字段，填省份编码 配置文件有默认值
	
	public Integer getIsEnAble() {
		return isEnAble;
	}
	public void setIsEnAble(Integer isEnAble) {
		this.isEnAble = isEnAble;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public Integer getTimerId() {
		return timerId;
	}
	public void setTimerId(Integer timerId) {
		this.timerId = timerId;
	}
	public Integer getJobId() {
		return jobId;
	}
	public String getTimerName() {
		return timerName;
	}
	public void setTimerName(String timerName) {
		this.timerName = timerName;
	}
	public String getTimerDesc() {
		return timerDesc;
	}
	public void setTimerDesc(String timerDesc) {
		this.timerDesc = timerDesc;
	}
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getExcuteMethod() {
		return excuteMethod;
	}
	public void setExcuteMethod(String excuteMethod) {
		this.excuteMethod = excuteMethod;
	}
	public Integer getTimerStatus() {
		return timerStatus;
	}
	public void setTimerStatus(Integer timerStatus) {
		this.timerStatus = timerStatus;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getBusiPara() {
		return busiPara;
	}
	public void setBusiPara(String busiPara) {
		this.busiPara = busiPara;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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
