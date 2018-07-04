package com.bocop.jxplatform.bean;

import java.io.Serializable;

public class SchoolBean implements Serializable {
	/**
	 * 
	 */
	
	private String name;
	private String iconId;
	private int intDraweid;
	private int unReadNum;// 未读数量
	private boolean isEnable;

	public boolean isEnable() {
		return isEnable;
	}
	
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public int getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(int unReadNum) {
		this.unReadNum = unReadNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIntDraweid() {
		return intDraweid;
	}

	public void setIntDraweid(int intDraweid) {
		this.intDraweid = intDraweid;
	}

	public String getIconId() {
		return iconId;
	}

	public void setIconId(String iconId) {
		this.iconId = iconId;
	}

	public SchoolBean(String name, int intDraweid) {
		this.name = name;
		this.intDraweid = intDraweid;
	}

	public SchoolBean(String name, int intDraweid, int unReadNum) {
		this.name = name;
		this.intDraweid = intDraweid;
		this.unReadNum = unReadNum;
	}

	public SchoolBean(String name, int intDraweid, String iconId, int unReadNum) {
		this.name = name;
		this.intDraweid = intDraweid;
		this.iconId = iconId;
		this.unReadNum = unReadNum;
	}
}
