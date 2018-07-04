package com.bocop.zyyr.bean;

import com.bocop.yfx.bean.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 贷款详情
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class LoanDetailsResponse extends BaseResponse {
	@XStreamAlias("DATA_AREA")
	private LoanDetailsExtern detailsExtern;

	public LoanDetailsExtern getDetailsExtern() {
		return detailsExtern;
	}

	public void setDetailsExtern(LoanDetailsExtern detailsExtern) {
		this.detailsExtern = detailsExtern;
	}

}
