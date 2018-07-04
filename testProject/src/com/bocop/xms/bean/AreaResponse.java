package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("UTILITY_PAYMENT")
public class AreaResponse {
	
	@XStreamAlias("CONST_HEAD")
	private ConstHead constHead;
	@XStreamAlias("DATA_LIST")
	private AddressResponse addressResponse;
	
	public ConstHead getConstHead() {
		return constHead;
	}
	public void setConstHead(ConstHead constHead) {
		this.constHead = constHead;
	}
	public AddressResponse getAddressResponse() {
		return addressResponse;
	}
	public void setAddressResponse(AddressResponse addressResponse) {
		this.addressResponse = addressResponse;
	}

}
