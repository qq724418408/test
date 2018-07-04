package com.bocop.yfx.xml.repayment;

import java.util.List;

public class RepaymentListXmlBean {

	private String errorcode;
	private String errormsg;
	private List<RepaymentBean> repaymentList;
	
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public List<RepaymentBean> getRepaymentList() {
		return repaymentList;
	}
	public void setRepaymentList(List<RepaymentBean> repaymentList) {
		this.repaymentList = repaymentList;
	}
	
}
