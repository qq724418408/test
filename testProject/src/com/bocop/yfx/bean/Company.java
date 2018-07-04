package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 公司列表
 * 
 * @author rd
 * 
 */
public class Company {
	@XStreamAlias("WLS_COMPANY_ID")
	private String companyId;

	@XStreamAlias("WLS_COMPANY_NAME")
	private String companyName;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Company(String companyId, String companyName) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
	}

	public Company() {
		super();
	}

}
