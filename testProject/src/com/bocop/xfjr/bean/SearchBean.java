package com.bocop.xfjr.bean;

import java.io.Serializable;
import java.util.List;

public class SearchBean implements Serializable{
	private int count;
	private String totalMoney;
	private List<BusinessBean> business;
	


	public SearchBean() {
		super();
	}
	
	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}



	public String getTotalMoney() {
		return totalMoney;
	}



	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}



	public List<BusinessBean> getBusiness() {
		return business;
	}



	public void setBusiness(List<BusinessBean> business) {
		this.business = business;
	}



	


}
