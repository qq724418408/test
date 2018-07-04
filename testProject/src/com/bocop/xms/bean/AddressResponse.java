package com.bocop.xms.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class AddressResponse {
	
	@XStreamImplicit(itemFieldName="addresses")
	private List<AddressList> addressList;

	public List<AddressList> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<AddressList> addressList) {
		this.addressList = addressList;
	}

}
