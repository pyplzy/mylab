package usi.dbdp.portal.task.dto;

import java.io.Serializable;
/**
 * 表格sys_node的实例：用于集群同步刷新内存等功能访问各应用节点
 * @author zhouyan
 *
 */
public class SysNodeDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private	Integer	appNodeId;		//应用节点ID	
	private String	nodeName;		//节点名称
	private Integer	nodeType;			//1：应用节点
	private	String	ipAddress;			//主机IP
	private	Integer	portValue;			//端口号
	private	String	content;				//说明
	private	String	ipport;				//ip+:+port
	public String getIpport() {
		return ipport;
	}
	public void setIpport(String ipport) {
		this.ipport = ipport;
	}
	private	Integer	shardingId;	//用于cobar分库字段，填省份编码
	
	public Integer getAppNodeId() {
		return appNodeId;
	}
	public void setAppNodeId(Integer appNodeId) {
		this.appNodeId = appNodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getPortValue() {
		return portValue;
	}
	public void setPortValue(Integer portValue) {
		this.portValue = portValue;
	}
	public Integer getNodeType() {
		return nodeType;
	}
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
	public Integer getShardingId() {
		return shardingId;
	}
	public void setShardingId(Integer shardingId) {
		this.shardingId = shardingId;
	}
	

}
