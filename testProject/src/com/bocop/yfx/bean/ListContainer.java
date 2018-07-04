package com.bocop.yfx.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * 历史记录
 * 
 * @author lh
 * 
 */
public class ListContainer {

	@XStreamImplicit(itemFieldName = "LOAN_RECORD_LIST")
	private List<LoanDetail> loanList;
	@XStreamImplicit(itemFieldName = "REPAYMENT_RECORD_LIST")
	private List<RepaymentRecords> reList;
	@XStreamImplicit(itemFieldName = "REPAYMENT_LIST")
	private List<ApplyHistory> hiList;

	@XStreamAlias("PAGE_NUMS")
	private String pageNum;
	@XStreamAlias("NEXT_PAGE")
	private String nextPage;
	@XStreamAlias("WLS_CURR_PAGE")
	private String currPage;

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public String getCurrPage() {
		return currPage;
	}

	public void setCurrPage(String currPage) {
		this.currPage = currPage;
	}

	public List<LoanDetail> getLoanList() {
		return loanList;
	}

	public void setLoanList(List<LoanDetail> loanList) {
		this.loanList = loanList;
	}

	public List<RepaymentRecords> getReList() {
		return reList;
	}

	public void setReList(List<RepaymentRecords> reList) {
		this.reList = reList;
	}

	public List<ApplyHistory> getHiList() {
		return hiList;
	}

	public void setHiList(List<ApplyHistory> hiList) {
		this.hiList = hiList;
	}

}
