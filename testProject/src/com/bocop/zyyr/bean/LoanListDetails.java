package com.bocop.zyyr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 我的贷款
 * 
 * @author lh
 * 
 */
public class LoanListDetails {

	@XStreamAlias("APP_ID")
	private String appID;
	@XStreamAlias("PDT_ID")
	private String proID;
	@XStreamAlias("PDT_NM")
	private String proName;
	@XStreamAlias("PDT_DESC")
	private String proDesc;
	@XStreamAlias("APP_STATUS")
	private String appStatus;
	@XStreamAlias("PDT_LOGO")
	private String proLogo;

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

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

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getProLogo() {
		return proLogo;
	}

	public void setProLogo(String proLogo) {
		this.proLogo = proLogo;
	}

}
