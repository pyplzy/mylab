package usi.dbdp.uic.dto;

import java.io.Serializable;

/**
 * 分页对象
 * @author lmwang
 * 创建时间：2015-2-6 上午10:46:23
 */
public class PageObj implements Serializable{
	
	/** 默认serialVersionUID*/
	private static final long serialVersionUID = 1L;
	
	/**每一页的记录数*/
	private int rows;
	
	/**当前页*/
	private int page;
	
	/**总记录数*/
	private int total;
	
	public PageObj() {
		super();
	}
	
	public PageObj(int rows, int page) {
		super();
		this.rows = rows;
		this.page = page;
	}

	/**
	 * 获取每一页的记录数
	 * @return 每一页的记录数
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * 设置每一页的记录数
	 * @param rows 每一页的记录数
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * 获取当前页数
	 * @return 当前页数
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 设置当前页数
	 * @param page 当前页数
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * 获取记录总数
	 * @return 记录总数
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设置记录总数
	 * @param total 记录总数
	 */
	public void setTotal(int total) {
		this.total = total;
	}
}
