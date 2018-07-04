package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 提前还款
 * 
 * @author lh
 *
 */
public class RefundData {

	@XStreamAlias("WLS_LOAN_AMOUNT")
	private String loanAmount;//贷款金额
	@XStreamAlias("WLS_SHOULD_PRINCIPAL")
	private String principal;//应还本金
	@XStreamAlias("WLS_SHOULD_IST")
	private String interest;//应还利息
	@XStreamAlias("WLS_TOTAL_ISTPRI")
	private String refund;//本息合计
	@XStreamAlias("WLS_REMAINING_AMOUNT")
	private String remainAmt;//剩余金额
	@XStreamAlias("WLS_REPAYMENT_CARD")
	private String repayCard;//还款账号
	@XStreamAlias("WLS_NOTOTALAMT")
	private String overdueAmt;//逾期金额

	/**
	 * 逾期金额
	 * @return
	 */
	public String getOverdueAmt() {
		return overdueAmt;
	}

	/**
	 * 逾期金额
	 * @return
	 */
	public void setOverdueAmt(String overdueAmt) {
		this.overdueAmt = overdueAmt;
	}

	/**
	 * 贷款金额
	 * @return
	 */
	public String getLoanAmount() {
		return loanAmount;
	}

	/**
	 * 贷款金额
	 * @return
	 */
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	/**
	 * 应还本金
	 * @return
	 */
	public String getPrincipal() {
		return principal;
	}

	/**
	 * 应还本金
	 * @return
	 */
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	/**
	 * 应还利息
	 * @return
	 */
	public String getInterest() {
		return interest;
	}

	/**
	 * 应还利息
	 * @return
	 */
	public void setInterest(String interest) {
		this.interest = interest;
	}

	/**
	 * 本息合计
	 * @return
	 */
	public String getRefund() {
		return refund;
	}

	/**
	 * 本息合计
	 * @return
	 */
	public void setRefund(String refund) {
		this.refund = refund;
	}

	/**
	 * 剩余金额
	 * @return
	 */
	public String getRemainAmt() {
		return remainAmt;
	}

	/**
	 * 剩余金额
	 * @return
	 */
	public void setRemainAmt(String remainAmt) {
		this.remainAmt = remainAmt;
	}

	/**
	 * 还款账号
	 * @return
	 */
	public String getRepayCard() {
		return repayCard;
	}

	/**
	 * 还款账号
	 * @return
	 */
	public void setRepayCard(String repayCard) {
		this.repayCard = repayCard;
	}
}
