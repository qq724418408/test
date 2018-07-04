package com.bocop.yfx.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 个人信息预览实体
 * 
 * @author rd
 * 
 */
public class PersonalInfo {
	
	@XStreamAlias("WLS_USER_ID")
	private String userId;
	@XStreamAlias("WLS_CUST_NAME")
	private String cutName;
	@XStreamAlias("WLS_SEX")
	private String custGender;
	@XStreamAlias("WLS_COMPANY_PHONE")
	private String companyPhone;
	@XStreamAlias("WLS_ID_CARD")
	private String idCard;
	@XStreamAlias("WLS_PHONE")
	private String phone;
	@XStreamAlias("WLS_CITY_ID")
	private String cityid;
	@XStreamAlias("WLS_COMPANY_ID")
	private String companyId;
	@XStreamAlias("WLS_CITY_NAME")
	private String cityName;
	@XStreamAlias("WLS_COMPANY_NAME")
	private String companyName;
	@XStreamAlias("WLS_ADDRESS")
	private String address;
	@XStreamImplicit(itemFieldName = "INFORMATION_LIST")
	private List<LinkManInfo> linkManInfos;

	public PersonalInfo(String userId, String cutName, String idCard,
			String phone, String cityid, String companyId, String cityName,
			String companyName, String address, List<LinkManInfo> linkManInfos) {
		super();
		this.userId = userId;
		this.cutName = cutName;
		this.idCard = idCard;
		this.phone = phone;
		this.cityid = cityid;
		this.companyId = companyId;
		this.cityName = cityName;
		this.companyName = companyName;
		this.address = address;
		this.linkManInfos = linkManInfos;
	}
	
	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCustGender() {
		return custGender;
	}

	public void setCustGender(String custGender) {
		this.custGender = custGender;
	}

	public PersonalInfo() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCutName() {
		return cutName;
	}

	public void setCutName(String cutName) {
		this.cutName = cutName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<LinkManInfo> getLinkManInfos() {
		return linkManInfos;
	}

	public void setLinkManInfos(List<LinkManInfo> linkManInfos) {
		this.linkManInfos = linkManInfos;
	}

}
