package com.bocop.yfx.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 城市和公司
 * 
 * @author rd
 * 
 */
public class CityAndCompany {
	@XStreamImplicit(itemFieldName = "WLS_CITY_LIST")
	private List<City> city;
	@XStreamImplicit(itemFieldName = "WLS_COMPANY_LIST")
	private List<Company> company;

	public List<City> getCity() {
		return city;
	}

	public void setCity(List<City> city) {
		this.city = city;
	}

	public List<Company> getCompany() {
		return company;
	}

	public void setCompany(List<Company> company) {
		this.company = company;
	}

	public CityAndCompany(List<City> city, List<Company> company) {
		super();
		this.city = city;
		this.company = company;
	}

	public CityAndCompany() {
		super();
	}

}
