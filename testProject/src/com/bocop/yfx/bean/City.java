package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 城市列表
 * 
 * @author rd
 * 
 */
public class City {
	@XStreamAlias("WLS_CITY_ID")
	private String cityId;
	@XStreamAlias("WLS_CITY_NAME")
	private String cityName;

	public City(String cityId, String cityName) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
	}

	public City() {
		super();
	}

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

}
