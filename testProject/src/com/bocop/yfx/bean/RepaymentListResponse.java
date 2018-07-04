package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 还款列表获取
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class RepaymentListResponse extends BaseResponse {

	@XStreamAlias("DATA_AREA")
	private RepaymentList list;

	public RepaymentList getList() {
		return list;
	}

	public void setList(RepaymentList list) {
		this.list = list;
	}
	
}
