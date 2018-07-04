package com.bocop.yfx.bean;

/**
 * 现金分期记录
 * 
 * @author rd
 * 
 */
public class CashStateRecord {
	private String cardNum;
	private String amount;
	private String count;
	private String perRepayment;
	private String loanUse;

	public CashStateRecord() {
		super();
	}

	public CashStateRecord(String cardNum, String amount, String count,
			String perRepayment, String loanUse) {
		super();
		this.cardNum = cardNum;
		this.amount = amount;
		this.count = count;
		this.perRepayment = perRepayment;
		this.loanUse = loanUse;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getamount() {
		return amount;
	}

	public void setamount(String amount) {
		this.amount = amount;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPerRepayment() {
		return perRepayment;
	}

	public void setPerRepayment(String perRepayment) {
		this.perRepayment = perRepayment;
	}

	public String getLoanUse() {
		return loanUse;
	}

	public void setLoanUse(String loanUse) {
		this.loanUse = loanUse;
	}

}
