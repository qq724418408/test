package com.bocop.zyyr.bean;

import com.bocop.yfx.bean.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 我的贷款
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class LoanResponse extends BaseResponse{

	@XStreamAlias("DATA_AREA")
	private LoanList list;

	public LoanList getList() {
		return list;
	}

	public void setList(LoanList list) {
		this.list = list;
	}
	
	

}
