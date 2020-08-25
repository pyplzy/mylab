package usi.dbdp.uic.dto;

import java.io.Serializable;

/**
 * 参与者
 * @author ma.guangming
 *
 */
public class ParticipantInfo implements Serializable   {
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	private int id;//节点id 
	private String text;//节点名称
	private boolean leaf;//是否叶子节点：true 是 false 否
	private String type;//类型：role、organization、person
	/**
	 * 获取节点id
	 * @return 节点id
	 */
	public int getId() {
		return id;
	}
	/**
	 * 设置节点id
	 * @param id 节点id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 获取节点名称
	 * @return 节点名称
	 */
	public String getText() {
		return text;
	}
	/**
	 * 设置节点名称
	 * @param text 节点名称
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * 获取是否叶子节点
	 * @return 是否叶子节点：true 是 false 否
	 */
	public boolean isLeaf() {
		return leaf;
	}
	/**
	 * 设置是否叶子节点
	 * @param leaf 是否叶子节点：true 是 false 否
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	/**
	 * 获取类型
	 * @return 类型：role、organization、person
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置类型
	 * @param type 类型：role、organization、person
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
