package com.bocop.yfx.xml.repayment;

public class RepaymentBean {

	private String period;//期号
	private String repayDate;//还款日期
	private String repayAmt;//本期应还金额
	private String repayCpt;//应还本金
	private String repayIst;//应还利息
	private String loanBal;//贷款余额
	
	public RepaymentBean() { 
		
	}
	
	public RepaymentBean(String period, String repayDate, String repayAmt, String repayCpt, String repayIst,
			String loanBal) {
		super();
		this.period = period;
		this.repayDate = repayDate;
		this.repayAmt = repayAmt;
		this.repayCpt = repayCpt;
		this.repayIst = repayIst;
		this.loanBal = loanBal;
	}
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}
	public String getRepayAmt() {
		return repayAmt;
	}
	public void setRepayAmt(String repayAmt) {
		this.repayAmt = repayAmt;
	}
	public String getRepayCpt() {
		return repayCpt;
	}
	public void setRepayCpt(String repayCpt) {
		this.repayCpt = repayCpt;
	}
	public String getRepayIst() {
		return repayIst;
	}
	public void setRepayIst(String repayIst) {
		this.repayIst = repayIst;
	}
	public String getLoanBal() {
		return loanBal;
	}
	public void setLoanBal(String loanBal) {
		this.loanBal = loanBal;
	}
	
}
