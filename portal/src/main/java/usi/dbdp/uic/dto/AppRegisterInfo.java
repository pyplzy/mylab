package usi.dbdp.uic.dto;

import java.io.Serializable;

import usi.dbdp.uic.entity.AppRegister;

public class AppRegisterInfo extends  AppRegister implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 代办数量
	 */
	private int num;
	
	/**
	 * 获取代办数量
	 * @return 
	 */
	public int getNum() {
		return num;
	}
	
	/**
	 * 设置代办数量
	 * @param num
	 */
	public void setNum(int num) {
		this.num = num;
	}

	
	
}
