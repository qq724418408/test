package com.bocop.xms.bean;

public class DialogCostType {

	private int costIcon;// 缴费类型图标
	private String costName;// 缴费类型名称
	private String typeCode;//缴费类型编号

	public int getCostIcon() {
		return costIcon;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public void setCostIcon(int costIcon) {
		this.costIcon = costIcon;
	}

	public String getCostName() {
		return costName;
	}

	public void setCostName(String costName) {
		this.costName = costName;
	}
}
