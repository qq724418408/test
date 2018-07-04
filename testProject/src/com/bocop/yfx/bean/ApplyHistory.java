package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 申请记录
 * 
 * @author lh
 * 
 */
public class ApplyHistory {
//	@XStreamAlias("WLS_APPLICATION_INFORMATION")
//	private String historyString;
	
	private String serialNumber;//序号
	@XStreamAlias("WLS_REPAYMENT_DATA")
	private String repaymentDate;//还款日期
	@XStreamAlias("WLS_PRINCIPAL")
	private String principal;//本金
	@XStreamAlias("WLS_INTEREST")
	private String interest;//利息
	@XStreamAlias("WLS_PI_TOTAL")
	private String interestTotal;//利息合计

//	public String getHistoryString() {
//		return historyString;
//	}
//
//	public void setHistoryString(String historyString) {
//		this.historyString = historyString;
//	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getRepaymentDate() {
		return repaymentDate;
	}

	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getInterestTotal() {
		return interestTotal;
	}

	public void setInterestTotal(String interestTotal) {
		this.interestTotal = interestTotal;
	}
	
}
