package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 提款数据
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class PickUpDataResponse extends BaseResponse {

	@XStreamAlias("DATA_AREA")
	private PickUpData pickUpData;

	public PickUpData getPickUpData() {
		return pickUpData;
	}

	public void setPickUpData(PickUpData pickUpData) {
		this.pickUpData = pickUpData;
	}

}
