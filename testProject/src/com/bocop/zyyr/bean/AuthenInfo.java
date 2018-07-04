package com.bocop.zyyr.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 认证资料
 * 
 * @author rd
 * 
 */
public class AuthenInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@XStreamAlias("CAREER")
	String career;
	@XStreamAlias("CAREER_VAL")
	String careerVal;
	@XStreamAlias("PHONE")
	String phone;
	@XStreamAlias("CORP_TYPE")
	String corpType;
	@XStreamAlias("CORP_TYPE_VAL")
	String corpTpVal;
	@XStreamAlias("HOUSE_TP")
	String houseType;
	@XStreamAlias("HOUSE_TP_VAL")
	String houseTpVal;
	@XStreamAlias("HAS_FUND")
	String hasFund;
	@XStreamAlias("HAS_INSURE")
	String hasInsure;
	@XStreamAlias("HAS_CAR")
	String hasCar;
	@XStreamAlias("WORKING_YEARS")
	String workingYear;
	@XStreamAlias("SALARY")
	String salary;
	@XStreamAlias("CASH_INCOME")
	String cashIncome;
	@XStreamAlias("GJJ_PERIOD")
	String gjjPeriod;
	@XStreamAlias("SECURITY_PERIOD")
	String securityPeriod;
	@XStreamAlias("HOUSE_VAL")
	String houseVal;
	@XStreamAlias("CREDIT_STATUS")
	String creditStatus;
	@XStreamAlias("CREDIT_STATUS_VAL")
	String creditStaVal;

	// 拜托啦，更新好不好？
	@XStreamAlias("STATUS")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCareerVal() {
		return careerVal;
	}

	public void setCareerVal(String careerVal) {
		this.careerVal = careerVal;
	}

	public String getCorpTpVal() {
		return corpTpVal;
	}

	public void setCorpTpVal(String corpTpVal) {
		this.corpTpVal = corpTpVal;
	}

	public String getHouseTpVal() {
		return houseTpVal;
	}

	public void setHouseTpVal(String houseTpVal) {
		this.houseTpVal = houseTpVal;
	}

	public String getCreditStaVal() {
		return creditStaVal;
	}

	public void setCreditStaVal(String creditStaVal) {
		this.creditStaVal = creditStaVal;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCorpType() {
		return corpType;
	}

	public void setCorpType(String corpType) {
		this.corpType = corpType;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public String getHasFund() {
		return hasFund;
	}

	public void setHasFund(String hasFund) {
		this.hasFund = hasFund;
	}

	public String getHasInsure() {
		return hasInsure;
	}

	public void setHasInsure(String hasInsure) {
		this.hasInsure = hasInsure;
	}

	public String getHasCar() {
		return hasCar;
	}

	public void setHasCar(String hasCar) {
		this.hasCar = hasCar;
	}

	public String getWorkingYear() {
		return workingYear;
	}

	public void setWorkingYear(String workingYear) {
		this.workingYear = workingYear;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getCashIncome() {
		return cashIncome;
	}

	public void setCashIncome(String cashIncome) {
		this.cashIncome = cashIncome;
	}

	public String getGjjPeriod() {
		return gjjPeriod;
	}

	public void setGjjPeriod(String gjjPeriod) {
		this.gjjPeriod = gjjPeriod;
	}

	public String getSecurityPeriod() {
		return securityPeriod;
	}

	public void setSecurityPeriod(String securityPeriod) {
		this.securityPeriod = securityPeriod;
	}

	public String getHouseVal() {
		return houseVal;
	}

	public void setHouseVal(String houseVal) {
		this.houseVal = houseVal;
	}

	public String getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}

	public AuthenInfo(String career, String careerVal, String phone, String corpType, String corpTpVal,
			String houseType, String houseTpVal, String hasFund, String hasInsure, String hasCar, String workingYear,
			String salary, String cashIncome, String gjjPeriod, String securityPeriod, String houseVal,
			String creditStatus, String creditStaVal) {
		super();
		this.career = career;
		this.careerVal = careerVal;
		this.phone = phone;
		this.corpType = corpType;
		this.corpTpVal = corpTpVal;
		this.houseType = houseType;
		this.houseTpVal = houseTpVal;
		this.hasFund = hasFund;
		this.hasInsure = hasInsure;
		this.hasCar = hasCar;
		this.workingYear = workingYear;
		this.salary = salary;
		this.cashIncome = cashIncome;
		this.gjjPeriod = gjjPeriod;
		this.securityPeriod = securityPeriod;
		this.houseVal = houseVal;
		this.creditStatus = creditStatus;
		this.creditStaVal = creditStaVal;
	}

	public AuthenInfo() {
		super();
	}

}
