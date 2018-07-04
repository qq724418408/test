package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 获取实名认证信息
 * 
 * @author rd
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class CityAndCompanyResponse extends BaseResponse {

	@XStreamAlias("DATA_AREA")
	private CityAndCompany cityAndCompany;

	public CityAndCompany getCityAndCompany() {
		return cityAndCompany;
	}

	public void setCityAndCompany(CityAndCompany cityAndCompany) {
		this.cityAndCompany = cityAndCompany;
	}

}
