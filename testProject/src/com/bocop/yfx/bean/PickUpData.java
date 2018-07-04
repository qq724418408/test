package com.bocop.yfx.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * 提款数据
 * 
 * @author lh
 * 
 */
public class PickUpData {
	@XStreamAlias("WLS_SURPLUS_AMOUNT")
	private String remainningSum;
	@XStreamAlias("WLS_PHONE")
	private String phone;
	@XStreamImplicit(itemFieldName = "USE_LIST")
	private List<UseString> useList;
	@XStreamImplicit(itemFieldName = "REPAYMENT_METHOD_LIST")
	private List<Method> methodList;
	@XStreamImplicit(itemFieldName = "REPAYMENT_CARD_LIST")
	private List<CardNumber> cardList;
	@XStreamImplicit(itemFieldName = "REPAYMENT_PERIOD_LIST")
	private List<Period> periodList;
	@XStreamAlias("WLS_REPAYMENT_RATE_B")
	private String rateB;
	@XStreamAlias("WLS_REPAYMENT_RATE_X")
	private String rateX;
	@XStreamAlias("WLS_REPAYMENT_ETOKEN_EDU")
	private String etoken;

	public String getRateX() {
		return rateX;
	}

	public void setRateX(String rateX) {
		this.rateX = rateX;
	}

	public String getRateB() {
		return rateB;
	}

	public void setRateB(String rateB) {
		this.rateB = rateB;
	}

	public String getRemainningSum() {
		return remainningSum;
	}

	public void setRemainningSum(String remainningSum) {
		this.remainningSum = remainningSum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<UseString> getUseList() {
		return useList;
	}

	public void setUseList(List<UseString> useList) {
		this.useList = useList;
	}

	public List<Method> getMethodList() {
		return methodList;
	}

	public void setMethodList(List<Method> methodList) {
		this.methodList = methodList;
	}

	public List<CardNumber> getCardList() {
		return cardList;
	}

	public void setCardList(List<CardNumber> cardList) {
		this.cardList = cardList;
	}

	public List<Period> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<Period> periodList) {
		this.periodList = periodList;
	}

	public String getEtoken() {
		return etoken;
	}

	public void setEtoken(String etoken) {
		this.etoken = etoken;
	}
	
}
