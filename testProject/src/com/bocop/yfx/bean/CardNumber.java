package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 银行卡号
 * 
 * @author user
 * 
 */
public class CardNumber {
	@XStreamAlias("WLS_REPAYMENT_CARD")
	private String cardString;

	public String getCardString() {
		return cardString;
	}

	public void setCardString(String cardString) {
		this.cardString = cardString;
	}
}
