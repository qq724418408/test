package com.bocop.jxplatform.bean;

public class CarListXmlBean {

	private String errorcode;
	private String errormsg;
	
	private int noList;
	private String[] licenseNumList;
	
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public int getNoList() {
		return noList;
	}
	public void setNoList(int noList) {
		this.noList = noList;
	}
	public String[] getLicenseNumList() {
		return licenseNumList;
	}
	public void setLicenseNumList(String[] licenseNumList) {
		this.licenseNumList = licenseNumList;
	}
	
	
}
