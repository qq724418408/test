package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 贷款、还款、申请记录
 * 
 * @author lh
 * 
 */
public class RepaymentRecords {

//	@XStreamAlias("WLS_ACCT_NO")
//	private String recordId;
//	@XStreamAlias("WLS_REPAYMENT_AMOUNT")
//	private String reAmount;
//	@XStreamAlias("WLS_REPAYMENT_DATE")
//	private String reDate;
	
	@XStreamAlias("WLS_ACCT_NO")
	private String loanNo = "";//贷款账号
	@XStreamAlias("WLS_DATA_TK")
	private String drawDate = "";//提款日期
	@XStreamAlias("WLS_AMOUNT_TK")
	private String drawMoney = "";//提款金额
	@XStreamAlias("WLS_TIME_YK")
	private String useDeadline = "";//用款期限
	@XStreamAlias("WLS_DATA_DQ")
	private String expireDate = "";//到期日期
	@XStreamAlias("WLS_LOAN_STATE")
	private String loanStatus = "";//贷款状态

//	public String getRecordId() {
//		return recordId;
//	}
//
//	public void setRecordId(String recordId) {
//		this.recordId = recordId;
//	}
//
//	public String getReAmount() {
//		return reAmount;
//	}
//
//	public void setReAmount(String reAmount) {
//		this.reAmount = reAmount;
//	}
//
//	public String getReDate() {
//		return reDate;
//	}
//
//	public void setReDate(String reDate) {
//		this.reDate = reDate;
//	}
	
	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	public String getDrawMoney() {
		return drawMoney;
	}

	public void setDrawMoney(String drawMoney) {
		this.drawMoney = drawMoney;
	}

	public String getUseDeadline() {
		return useDeadline;
	}

	public void setUseDeadline(String useDeadline) {
		this.useDeadline = useDeadline;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}
	
}
