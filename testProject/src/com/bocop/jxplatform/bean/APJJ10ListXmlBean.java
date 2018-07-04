package com.bocop.jxplatform.bean;

import java.util.List;

public class APJJ10ListXmlBean {

	private String errorcode;
	private String errormsg;
	
	private int noList;
	private String licenseNum;
	private List<CarPeccancyBean> carPeccancyListBean;
	
	
	
	public String getLicenseNum() {
		return licenseNum;
	}
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
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
	public List<CarPeccancyBean> getCarPeccancyListBean() {
		return carPeccancyListBean;
	}
	public void setCarPeccancyListBean(List<CarPeccancyBean> carPeccancyListBean) {
		this.carPeccancyListBean = carPeccancyListBean;
	}
	
	
}
