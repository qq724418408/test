package com.bocop.xms.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 用于UserManagerActivity和SignContractActivity
 * 
 * @author lh
 * 
 */

public class SignContractInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("TYPE")
	private String costType;
	@XStreamAlias("USER_NAME")
	private String name;// 用户姓名
	@XStreamAlias("USER_ID")
	private String userId;// 中银用户号
	@XStreamAlias("SERV_ID")
	private String servId;//缴费类型
	@XStreamAlias("ADDRESS_ID")
	private String areaId;
	@XStreamAlias("ADDRESS")
	private String area;// 缴费地区
	@XStreamAlias("AGENT_CODE")
	private String unit;// 缴费单位
	@XStreamAlias("ORDER_DATE")
	private String orderDate;//缴费日期
	@XStreamAlias("USER_CODE")
	private String userCode;//用户号码
	@XStreamAlias("DEV_TYP")
	private String devTyp;//设备类型
	@XStreamAlias("SUBSCRIBERNO")
	private String subscriberno;//有线编号
	@XStreamAlias("TRN_CODE")
	private String trnCode;
	@XStreamAlias("SERVICETYPE")
	private String servicetype;//有线收费类型
	@XStreamAlias("PINPAI_N")
	private String pinpaiN;
	@XStreamAlias("SYS_ID")
	private String sysId;
	@XStreamAlias("AGENT_NAME")
	private String agentName;//单位名称
	

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getServId() {
		return servId;
	}

	public void setServId(String servId) {
		this.servId = servId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getDevTyp() {
		return devTyp;
	}

	public void setDevTyp(String devTyp) {
		this.devTyp = devTyp;
	}

	public String getSubscriberno() {
		return subscriberno;
	}

	public void setSubscriberno(String subscriberno) {
		this.subscriberno = subscriberno;
	}

	public String getTrnCode() {
		return trnCode;
	}

	public void setTrnCode(String trnCode) {
		this.trnCode = trnCode;
	}

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}

	public String getPinpaiN() {
		return pinpaiN;
	}

	public void setPinpaiN(String pinpaiN) {
		this.pinpaiN = pinpaiN;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Override
	public String toString() {
		return "SignContractInfo [costType=" + costType + ", name=" + name
				+ ", userId=" + userId + ", servId=" + servId + ", areaId="
				+ areaId + ", area=" + area + ", unit=" + unit + ", orderDate="
				+ orderDate + ", userCode=" + userCode + ", devTyp=" + devTyp
				+ ", subscriberno=" + subscriberno + ", trnCode=" + trnCode
				+ ", servicetype=" + servicetype + ", pinpaiN=" + pinpaiN
				+ ", sysId=" + sysId + ", agentName=" + agentName + "]";
	}
	
	

}
