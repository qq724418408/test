package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 提前还款
 * 
 * @author lh
 *
 */
@XStreamAlias("UTILITY_PAYMENT")
public class RefundDataResponse extends BaseResponse {

	@XStreamAlias("DATA_AREA")
	private RefundData refundData;

	public RefundData getRefundData() {
		return refundData;
	}

	public void setRefundData(RefundData refundData) {
		this.refundData = refundData;
	}
}
