package com.bocop.xfjr.bean.detail;

import java.io.Serializable;

public class HouseBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String housesAddress; // 地址
	private String housesArea; // 面积
	 private String housesType;
	private String housesOwner; // 业主

	public HouseBean() {
		super();
	}

	public HouseBean(String housesOwner) {
		super();
		this.housesOwner = housesOwner;
	}

	public String getHousesAddress() {
		return housesAddress;
	}

	public void setHousesAddress(String housesAddress) {
		this.housesAddress = housesAddress;
	}

	public String getHousesArea() {
		return housesArea;
	}

	public void setHousesArea(String housesArea) {
		this.housesArea = housesArea;
	}

	public String getHousesType() {
		return housesType;
	}

	public void setHousesType(String housesType) {
		this.housesType = housesType;
	}

	public String getHousesOwner() {
		return housesOwner;
	}

	public void setHousesOwner(String housesOwner) {
		this.housesOwner = housesOwner;
	}

}
