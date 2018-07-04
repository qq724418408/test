package com.bocop.zyyr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 贷款详情
 * 
 * @author lh
 * 
 */
public class LoanDetails {

	@XStreamAlias("PDT_ID")
	private String proID;
	@XStreamAlias("PDT_NM")
	private String proName;
	@XStreamAlias("APP_AMT")
	private String appAmount;
	@XStreamAlias("APP_DATE")
	private String appDate;
	@XStreamAlias("APP_TIME")
	private String appTime;
	@XStreamAlias("APP_PERIOD")
	private String appPeriod;
	/**
	 * 贷款利息
	 */
	@XStreamAlias("APP_INT")
	private String appInterest;
	@XStreamAlias("PAY_PER_MONTH")
	private String payPM;

	public String getProID() {
		return proID;
	}

	public void setProID(String proID) {
		this.proID = proID;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getAppAmount() {
		return appAmount;
	}

	public void setAppAmount(String appAmount) {
		this.appAmount = appAmount;
	}

	public String getAppDate() {
		return appDate;
	}

	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}

	public String getAppTime() {
		return appTime;
	}

	public void setAppTime(String appTime) {
		this.appTime = appTime;
	}

	public String getAppPeriod() {
		return appPeriod;
	}

	public void setAppPeriod(String appPeriod) {
		this.appPeriod = appPeriod;
	}

	/**
	 * 贷款利息
	 */
	public String getAppInterest() {
		return appInterest;
	}

	public void setAppInterest(String appInterest) {
		this.appInterest = appInterest;
	}

	public String getPayPM() {
		return payPM;
	}

	public void setPayPM(String payPM) {
		this.payPM = payPM;
	}

}
