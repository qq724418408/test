package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 贷款剩余可使用额度查询
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class RemainningSumResponse extends BaseResponse {

	@XStreamAlias("DATA_AREA")
	private RemainSum remainSum;

	public RemainSum getRemainningSum() {
		return remainSum;
	}

	public void setRemainningSum(RemainSum remainSum) {
		this.remainSum = remainSum;
	}

}
