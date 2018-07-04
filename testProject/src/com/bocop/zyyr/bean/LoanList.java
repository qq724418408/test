package com.bocop.zyyr.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * 我的贷款
 * 
 * @author lh
 * 
 */
public class LoanList {

	@XStreamImplicit(itemFieldName = "MYPROLIST")
	private List<LoanListDetails> list;
	@XStreamAlias("PAGEDATA")
	
	private CommonResponse commonResponse;

	public CommonResponse getCommonResponse() {
		return commonResponse;
	}

	public void setCommonResponse(CommonResponse commonResponse) {
		this.commonResponse = commonResponse;
	}
	public List<LoanListDetails> getList() {
		return list;
	}

	public void setList(List<LoanListDetails> list) {
		this.list = list;
	}

}
