package com.bocop.yfx.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * 还款列表
 * 
 * @author lh
 * 
 */
public class RepaymentList {

	@XStreamImplicit(itemFieldName = "REPAYMENT_LIST")
	private List<Repayment> list;

	public List<Repayment> getList() {
		return list;
	}

	public void setList(List<Repayment> list) {
		this.list = list;
	}
}
