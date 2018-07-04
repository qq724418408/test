package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 还款列表
 * 
 * @author lh
 * 
 */
public class Repayment {

	@XStreamAlias("WLS_ACCT_NO")
	private String accNo;
	/**
	 * 提款日期
	 */
	@XStreamAlias("WLS_DATA_TK")
	private String dataTK;
	/**
	 * 提款金额
	 */
	@XStreamAlias("WLS_AMOUNT_TK")
	private String amtTK;
	/**
	 * 用款期限
	 */
	@XStreamAlias("WLS_TIME_YK")
	private String timeYK;
	/**
	 * 到期日期
	 */
	@XStreamAlias("WLS_DATA_DQ")
	private String dataDQ;
	/**
	 * 贷款状态
	 */
	@XStreamAlias("WLS_LOAN_STATE")
	private String loanState;

	

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getDataTK() {
		return dataTK;
	}

	public void setDataTK(String dataTK) {
		this.dataTK = dataTK;
	}

	public String getAmtTK() {
		return amtTK;
	}

	public void setAmtTK(String amtTK) {
		this.amtTK = amtTK;
	}

	public String getTimeYK() {
		return timeYK;
	}

	public void setTimeYK(String timeYK) {
		this.timeYK = timeYK;
	}

	public String getDataDQ() {
		return dataDQ;
	}

	public void setDataDQ(String dataDQ) {
		this.dataDQ = dataDQ;
	}

	public String getLoanState() {
		return loanState;
	}

	public void setLoanState(String loanState) {
		this.loanState = loanState;
	}

	

}
