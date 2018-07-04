package com.bocop.xms.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class AddressList {
	
	@XStreamAlias("cityId")
	private String cityId;
	@XStreamAlias("cityName")
	private String cityName;
	@XStreamImplicit(itemFieldName="infoList")
	private List<AddressDetail> list;
	
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public List<AddressDetail> getList() {
		return list;
	}
	public void setInfoList(List<AddressDetail> list) {
		this.list = list;
	}
	

}
