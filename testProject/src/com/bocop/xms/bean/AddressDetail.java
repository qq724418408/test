package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class AddressDetail {
	
	@XStreamAlias("cityId")
	private String cityId;
	@XStreamAlias("cityName")
	private String cityName;
	@XStreamAlias("agentCode")
	private String agentCode;
	@XStreamAlias("agentName")
	private String agentName;
	@XStreamAlias("trnCode")
	private String trnCode;
	@XStreamAlias("sysId")
	private String sysId;
	@XStreamAlias("type")
	private String type;
	
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
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getTrnCode() {
		return trnCode;
	}
	public void setTrnCode(String trnCode) {
		this.trnCode = trnCode;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
