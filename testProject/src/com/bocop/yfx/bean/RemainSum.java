package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 剩余额度
 * 
 * @author lh
 * 
 */
public class RemainSum {
	@XStreamAlias("WLS_SURPLUS_AMOUNT")
	private String remainSum;//额度金额
	@XStreamAlias("WLS_AMOUNT_STATE")
	private String amtStatus;//金额状态
	@XStreamAlias("WLS_LOAN_LIMIT")
	private String totalSum;//总额度(授信额度)

	public String getAmtStatus() {
		return amtStatus;
	}

	public void setAmtStatus(String amtStatus) {
		this.amtStatus = amtStatus;
	}

	public String getRemainSum() {
		return remainSum;
	}

	public void setRemainSum(String remainSum) {
		this.remainSum = remainSum;
	}

	public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}

}
