package com.bocop.gm.bean;

public class ContactsInfo {
	
	/**
	 * 姓名
	 */
	private String n;
	/**
	 * 电话号码
	 */
	private String p;
	/**
	 * 排序码
	 */
	private String sortKey;
	
	
	
	
	public String getSortKey() {
		return sortKey;
	}




	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}




	public String getN() {
		return n;
	}




	public void setN(String n) {
		this.n = n;
	}




	public String getP() {
		return p;
	}




	public void setP(String p) {
		this.p = p;
	}




	public String bean2String(){
		return "name : "+n+"----phone :"+p;
	}
	
	

}
