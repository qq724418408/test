package com.bocop.jxplatform.bean;
/** 
 * @author luoyang  
 * @version 创建时间：2015-6-25 上午9:29:45 
 * 交通助手主界面LIST
 */

public class TrafficInfoBean {

	public String myLicenseNum;
	public String mycarpeccancy;
	public String myQuickPay;
	
	public TrafficInfoBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrafficInfoBean(String myLicenseNum, String mycarpeccancy,
			String myQuickPay) {
		super();
		this.myLicenseNum = myLicenseNum;
		this.mycarpeccancy = mycarpeccancy;
		this.myQuickPay = myQuickPay;
	}

	public String getMyLicenseNum() {
		return myLicenseNum;
	}

	public void setMyLicenseNum(String myLicenseNum) {
		this.myLicenseNum = myLicenseNum;
	}

	public String getMycarpeccancy() {
		return mycarpeccancy;
	}

	public void setMycarpeccancy(String mycarpeccancy) {
		this.mycarpeccancy = mycarpeccancy;
	}

	public String getMyQuickPay() {
		return myQuickPay;
	}

	public void setMyQuickPay(String myQuickPay) {
		this.myQuickPay = myQuickPay;
	}
	
	
}
