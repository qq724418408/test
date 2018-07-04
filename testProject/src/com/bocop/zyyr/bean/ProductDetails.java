package com.bocop.zyyr.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 产品详情
 * 
 * @author lh
 * 
 */
public class ProductDetails implements Serializable {

	@XStreamAlias("PDT_ID")
	private String proId;
	// 产品名字
	@XStreamAlias("PDT_NM")
	private String proName;
	// 最快放款时间
	@XStreamAlias("FAST_LOAN_TIME")
	private String proLoanTime;
	// 每期利息
	@XStreamAlias("INTEREST")
	private String interest;
	// 月还款
	@XStreamAlias("PAY_PER_MONTH")
	private String payPM;
	// 利率
	@XStreamAlias("PDT_RATE")
	private String rate;
	// 介绍
	@XStreamAlias("PDT_DETAIL")
	private String details;
	// 最小金额
	@XStreamAlias("PDT_MIN_VAL")
	private String minAmt;
	// 最大金额
	@XStreamAlias("PDT_MAX_VAL")
	private String maxAmt;
	

	public String getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(String minAmt) {
		this.minAmt = minAmt;
	}

	public String getMaxAmt() {
		return maxAmt;
	}

	public void setMaxAmt(String maxAmt) {
		this.maxAmt = maxAmt;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProLoanTime() {
		return proLoanTime;
	}

	public void setProLoanTime(String proLoanTime) {
		this.proLoanTime = proLoanTime;
	}

	/**
	 * 每期利息
	 * 
	 * @return
	 */
	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getPayPM() {
		return payPM;
	}

	public void setPayPM(String payPM) {
		this.payPM = payPM;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
